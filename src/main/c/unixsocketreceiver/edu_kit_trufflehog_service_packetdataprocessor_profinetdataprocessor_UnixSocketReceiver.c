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
int getSocketFile(struct SocketData *sockData)
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

	sockData->address.sun_path[0] = '\0';
	strcat(sockData->address.sun_path, homeDir);
	strcat(sockData->address.sun_path, SOCKET_NAME);

	return 0;

error:
    return -1;
}

int openSocket()
{
    debug("opening socket..");

	// Init socket data
	check(!getSocketFile(&socketData), "could not get the socket file");
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

	char buf[] = "Hello Snort";

	check(write(socketData.socketFD, buf, strlen(buf)), "error on sending \"hello snort\"");

	debug("Successfully connected to snort");

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
    debug("initializing unix socket receiver");

    check(openSocket() != -1, "throwing exception, because we could not initialize the receiver");

error:
    throwSnortPluginNotRunningException(env, "Could not connect to snort!");
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    closeIPC
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_closeIPC(JNIEnv *env, jobject thisObj)
{
	//TODO close socket
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
	if (truffleClass == NULL)
		throwNoClassDefError(env, truffleClassName);

	jmethodID ctor = (*env)->GetMethodID(env, truffleClass, "<init>", "(JJ)V");
	if (ctor == NULL)
		throwNoSuchMethodError(env, "Constructor for class Truffle not found");

	jobject truffleObject = (*env)->NewObject(env, truffleClass, ctor);

	//TODO fill truffle with data

	return truffleObject;
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
