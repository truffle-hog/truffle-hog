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
#define _CHECK_JAVA_EXCEPTION(ENV) if ((*(ENV))->ExceptionOccurred((ENV))) {return NULL;}
#define _CHECK_JAVA_EXCEPTION_VOID(ENV) if ((*(ENV))->ExceptionOccurred((ENV))) {return;}

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

///////////////////////
//                   //
// JNI Class getters //
//                   //
///////////////////////

/**
 * @brief Gets the Truffle class reference
 * This method may throw java exceptions. Please check with _CHECK_JAVA_EXCEPTION(env) after calling.
 *
 * @param env the java environment
 *
 * @return returns a reference to the Truffle class and NULL on error
 */
jclass getTruffleClass(JNIEnv *env)
{
    static jclass truffleClass = NULL;

    if (truffleClass == NULL)
    {
        jclass localTruffleClass = (*env)->FindClass(env, "edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/Truffle");
        _CHECK_JAVA_EXCEPTION(env);
        check_to(localTruffleClass != NULL, noClass, "Truffle class could not be found");

        truffleClass = (jclass) (*env)->NewGlobalRef(env, localTruffleClass);
        _CHECK_JAVA_EXCEPTION(env);
        check_to(truffleClass != NULL, noClass, "Global truffle class reference could not be created");

        (*env)->DeleteLocalRef(env, localTruffleClass);
        _CHECK_JAVA_EXCEPTION(env);
    }
    return truffleClass;

noClass:
    throwNoClassDefError(env, "Truffle class could not be found");
    return NULL;
}

////////////////////////
//                    //
// JNI method getters //
//                    //
////////////////////////

/**
 * @brief Gets the buildTruffle method id.
 * This method may throw java exceptions. Please check with _CHECK_JAVA_EXCEPTION(env) after calling.
 *
 * @param env the java environment
 *
 * @return returns the method id of the buildTruffle method
 */
jmethodID getBuildTruffle(JNIEnv *env)
{
    static jmethodID buildTruffleMID = NULL;

    if (buildTruffleMID == NULL)
    {
        jclass truffleClass = getTruffleClass(env);
        _CHECK_JAVA_EXCEPTION(env);

        buildTruffleMID = (*env)->GetStaticMethodID(env, truffleClass, "buildTruffle", "(JJJJLjava/lang/String;S)Ledu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/Truffle;");
        _CHECK_JAVA_EXCEPTION(env);
        check_to(buildTruffleMID != 0, noBuildTruffle, "buildTruffle method not found");
    }

    return buildTruffleMID;

noBuildTruffle:
    throwNoSuchMethodError(env, "buildTruffle method not found");
    return NULL;
}

////////////////////
//                //
// Normal methods //
//                //
////////////////////

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

/**
 * @brief Opens the socket for communication.
 * Only call this method once before closing.
 *
 * @return returns 0 on success and -1 on error
 */
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

	check(write(socketData.socketFD, &TRUFFLEHOG_DISCONNECT_REQUEST, sizeof(TRUFFLEHOG_DISCONNECT_REQUEST)), "error on sending disconnect request");

    check(close(socketData.socketFD) == 0, "could not close socket!");

    memset(&socketData, 0, sizeof(struct SocketData));

    jclass truffleClass = getTruffleClass(env);
    _CHECK_JAVA_EXCEPTION_VOID(env);

    (*env)->DeleteGlobalRef(env, truffleClass);
    _CHECK_JAVA_EXCEPTION_VOID(env);

    debug("disconnect successful");

	return;

error:
	throwSnortPluginDisconnectFailedException(env, "failed to disconnect");
	return;
}

/**
 * @brief Gets the next truffle struct from the socket.
 *
 * @param env the java environment pointer.
 * @param the truffle struct to fill the data in.
 */
int getNextTruffle(JNIEnv *env, Truffle *truffle)
{
    fd_set read_fds, write_fds, except_fds;
    FD_ZERO(&read_fds);
    FD_ZERO(&write_fds);
    FD_ZERO(&except_fds);
    FD_SET(socketData.socketFD, &read_fds);

    struct timeval timeout;
    timeout.tv_sec = 1;
    timeout.tv_usec = 0;

    // wait for fd to become ready but only wait 1 second so that the disconnect method gets the chance to close the connection
    int rv = select(socketData.socketFD + 1, &read_fds, &write_fds, &except_fds, &timeout);
    if (rv > 0)
    {
        ssize_t len = read(socketData.socketFD, (void*) (truffle), sizeof(Truffle));

        check_to(len >= 0, noMessageReceived, "reading the turffle failed");
        check(len == sizeof(Truffle), "could not read the correct number of bytes from the socket");

	    return 0;
	}
	check_to(rv != 0, noMessageReceived, "no message received");

	debug("some error occurred while waiting for fd to become available: rv=%d", rv);
	throwReceiverReadError(env, "other error");
	return -3;

error:
	throwReceiverReadError(env, "could not read the correct number of bytes from the socket");
	return -1;
noMessageReceived:
	debug("read failed");
	return -2;
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    getTruffle
 * Signature: ()Ledu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/Truffle;
 */
JNIEXPORT jobject JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_getTruffle(JNIEnv *env, jobject thisObj)
{
    Truffle truffle;
    check(getNextTruffle(env, &truffle) >= 0, "getNextTruffle failed!");

    jclass truffleClass = getTruffleClass(env);
    _CHECK_JAVA_EXCEPTION(env);

	// get buildTruffle method
    jmethodID buildTruffleMID = getBuildTruffle(env);
    _CHECK_JAVA_EXCEPTION(env);

    // create the java string for the deviceName property
    jstring nameStr = (*env)->NewStringUTF(env, "put name here");
    _CHECK_JAVA_EXCEPTION(env);
    check(nameStr != NULL, "could not create deviceName string");

	jobject truffleObject = (*env)->CallStaticObjectMethod(env,
	                                                       truffleClass,
	                                                       buildTruffleMID,
	                                                       truffle.etherHeader.sourceMacAddress,
	                                                       truffle.etherHeader.destMacAddress,
	                                                       0,
	                                                       0,
	                                                       nameStr,
	                                                       truffle.etherHeader.etherType);
    _CHECK_JAVA_EXCEPTION(env);

    return truffleObject;

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
