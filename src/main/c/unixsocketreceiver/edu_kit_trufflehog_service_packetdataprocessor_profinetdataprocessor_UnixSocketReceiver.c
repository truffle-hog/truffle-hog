#include <sys/socket.h>
#include <sys/types.h>
#include <sys/un.h>
#include <unistd.h>
#include <stdlib.h>
#include <pwd.h>
#include "edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver.h"
#include "truffle.h"
#include "snortComm.h"
#include "dbg.h"

/////////////
//         //
// Defines //
//         //
/////////////

#define SOCKET_NAME "/socket.sock"

//////////////////////////
//                      //
// Forward declarations //
//                      //
//////////////////////////

jint throwNoClassDefError(JNIEnv*, char*);
jint throwNoSuchMethodError(JNIEnv*, char*);
jint throwSnortPluginNotRunningException(JNIEnv*, char*);
jint throwSnortPluginDisconnectFailedException(JNIEnv *, char *);
jint throwReceiverReadError(JNIEnv *, char *);

/////////////
//         //
// Structs //
//         //
/////////////

struct SocketData
{
	int socketFD;
	struct sockaddr_un address;
};


//////////////////////
//                  //
// Global variables //
//                  //
//////////////////////

// The socket file descriptor
struct SocketData socketData;


/**
 * @brief Gets the socket file path and writes it to sockData.address.
 *
 * @param sockData the SocketData struct to write to
 *
 * @return Returns 0 on success and -1 on error.
 */
int getSocketFile(char *sockFilePath)
{
	char *homeDir;
	if ((homeDir = getenv("HOME")) == NULL)
	{
		int uid = getuid();
		struct passwd *pw = getpwuid(uid);
		check(pw != NULL, "couldn't get user home dir");

		homeDir = pw->pw_dir;
	}

	check((strlen(homeDir) + strlen(SOCKET_NAME)) < 108, "socket file path is too long! Max length is 108 chars");

	sockFilePath[0] = '\0';
	strcat(sockFilePath, homeDir);
	strcat(sockFilePath, SOCKET_NAME);

	return 0;

error:
    return -1;
}

int openSocket()
{
    debug("opening socket..");

	// Init socket data
	check(!getSocketFile(socketData.address.sun_path), "could not get the socket file");
	socketData.address.sun_family = AF_UNIX;

	socketData.socketFD = socket(AF_UNIX, SOCK_SEQPACKET, 0);
	if (socketData.socketFD < 0)
	{
		printf("ERROR: could not create socket!\n");
		return -1;
	}
	// end of init socket data

    // connect to snort
	int32_t len = strlen(socketData.address.sun_path) + sizeof(socketData.address.sun_family);
	check(!(connect(socketData.socketFD, (struct sockaddr*) &socketData.address, len) < 0),
	      "could not connect to socket \'%s\', \'%d\'",
	      socketData.address.sun_path,
	      socketData.address.sun_family);

	// end of connect to snort

	return 0;

error:
    return -1;
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    openIPC
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_openIPC (JNIEnv *env, jobject thisObj)
{
	debug("initializing ipc");

    check(openSocket() != -1, "throwing exception, because we could not initialize the receiver");

	check(write(socketData.socketFD, &TRUFFLEHOG_CONNECT_REQUEST, sizeof(TRUFFLEHOG_CONNECT_REQUEST)), "error writing connect request");

	int buffer = -1;
	check(read(socketData.socketFD, (void*) &buffer, sizeof(buffer)), "error reading from socket");

	check(buffer == SNORT_CONNECT_RESPONSE, "incorrect snort response");

	debug("initialization done... returning to java");

	return;

error:
    throwSnortPluginNotRunningException(env, "Could not connect to snort!");
	return;
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    closeIPC
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_closeIPC(JNIEnv *env, jobject thisObj)
{
    debug("starting disconnect sequence");

	check (write(socketData.socketFD, &TRUFFLEHOG_DISCONNECT_REQUEST, sizeof(TRUFFLEHOG_DISCONNECT_REQUEST)), "error on sending disconnect request");

    debug("disconnect successful");

	return;

error:
	throwSnortPluginDisconnectFailedException(env, "failed to disconnect");
	return;

timeout:
    throwSnortPluginDisconnectFailedException(env, "failed to disconnect (took too long)");
    return;
}

int getNextTruffle(JNIEnv *env, Truffle *truffle)
{

	debug("reading truffle");

    ssize_t len = read(socketData.socketFD, (void*) (truffle), sizeof(Truffle));

    if (len < 0)
		goto noMessageReceived;
    else if (len != sizeof(Truffle))
        goto error;

	return 0;

error:
	throwReceiverReadError(env, "could not read the correct number of bytes from the socket");
	return -1;
noMessageReceived:
	debug("read failed");
	return -2;
}


/**
 * @brief Gets the class id by name
 *
 * @param env the java environment
 * @param className the name of the class to get
 *
 * @return Returns the class id on success and NULL on error.
 */
jclass getClassByName(JNIEnv *env, char *className)
{
    jclass clazz = (*env)->FindClass(env, className);
    check(clazz != NULL, "class not found");

    return clazz;
error:
    throwNoClassDefError(env, className);
    return NULL;
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    getTruffle
 * Signature: ()Ledu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/Truffle;
 */
JNIEXPORT jobject JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_getTruffle(JNIEnv *env, jobject thisObj)
{
	char *truffleClassName = "edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/Truffle";

	jclass truffleClass = (*env)->FindClass(env, truffleClassName);
	check_to(truffleClass != NULL, noClass, "Truffle class could not be found");

	jmethodID ctor = (*env)->GetMethodID(env, truffleClass, "<init>", "()V");
	check_to(ctor != NULL, noCtor, "constructor for class Truffle not found");

	jobject truffleObject = (*env)->NewObject(env, truffleClass, ctor);

	Truffle truffle;

	check(getNextTruffle(env, &truffle) >= 0, "getNextTruffle failed!");

    // get method
    jmethodID setAttributeMID = (*env)->GetMethodID(env, truffleClass, "setAttribute", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;");
    check_to(setAttributeMID != 0, noSetAttribute, "setAttribute method not found");

    // get the long class
    jclass longClass = getClassByName(env, "java/lang/Long");
    check(longClass != NULL, "could not get long class");

    // create the id string

    jstring idStr = (*env)->NewStringUTF(env, "sourceMacAddress");
    check(idStr != NULL, "could not create id string");

    jlong srcMacAddr = (jlong) truffle.etherHeader.sourceMacAddress; //TODO NEED TO CREATE AN ACTUAL LONG OBJECT!!!

    debug("calling setAttribute...");
    check((*env)->CallObjectMethod(env, truffleObject, setAttributeMID, longClass, idStr, srcMacAddr) != NULL, "could not call setAttribute");

	truffle.etherHeader.destMacAddress;

	return truffleObject;

noClass:
	throwNoClassDefError(env, truffleClassName);
	return NULL;

noSetAttribute:
    throwNoSuchMethodError(env, "SetAttribute method not found");
    return NULL;

noCtor:
	throwNoSuchMethodError(env, "Constructor for class Truffle not found");
	return NULL;

error:
	return NULL;
}



////////////////////////////////
//                            //
// Exception handling methods //
//                            //
////////////////////////////////

jint throwNoClassDefError(JNIEnv *env, char *message)
{
	jclass exClass;
	char *className = "java/lang/NoClassDefFoundError";

	exClass = (*env)->FindClass(env, className);
	if (exClass == NULL)
		return throwNoClassDefError(env, className);

	return (*env)->ThrowNew(env, exClass, message);
}

jint throwNoSuchMethodError(JNIEnv *env, char *message)
{
	jclass exClass;
	char *className = "java/lang/NoSuchMethodError";

	exClass = (*env)->FindClass(env, className);
	if (exClass == NULL)
		return throwNoClassDefError(env, className);

	return (*env)->ThrowNew(env, exClass, message);
}

jint throwSnortPluginNotRunningException(JNIEnv *env, char *message)
{
	jclass exClass;
	char *className = "edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/SnortPNPluginNotRunningException";

	exClass = (*env)->FindClass(env, className);
	if (exClass == NULL)
		return throwNoClassDefError(env, className);
	return (*env)->ThrowNew(env, exClass, message);
}

jint throwSnortPluginDisconnectFailedException(JNIEnv *env, char *message)
{
	jclass exClass;
	char *className = "edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/SnortPNPluginDisconnectFailedException";

	exClass =(*env)->FindClass(env, className);
	if (exClass == NULL)
		return throwNoClassDefError(env, className);
	return (*env)->ThrowNew(env, exClass, message);
}

jint throwReceiverReadError(JNIEnv *env, char *message)
{
	jclass exClass;
	char *className = "edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/ReceiverReadError";

	exClass =(*env)->FindClass(env, className);
	if (exClass == NULL)
		return throwNoClassDefError(env, className);
	return (*env)->ThrowNew(env, exClass, message);
}
