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
        char *truffleClassName = "edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/Truffle";

        jclass localTruffleClass = (*env)->FindClass(env, truffleClassName);
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

//////////////////////
//                  //
// JNI Ctor getters //
//                  //
//////////////////////

/**
 * @brief Gets the constructor method id for the truffle object
 * This method may throw java exceptions. Please check with _CHECK_JAVA_EXCEPTION(env) after calling.
 *
 * @param env the java environment
 * @param truffleClass the truffle class reference
 *
 * @return returns the method id of the constructor
 */
jmethodID getTruffleCtor(JNIEnv *env, jclass truffleClass)
{
    static jmethodID ctor = NULL;

    if (ctor == NULL)
    {
        ctor = (*env)->GetMethodID(env, truffleClass, "<init>", "()V");
        _CHECK_JAVA_EXCEPTION(env);
        check_to(ctor != NULL, noCtor, "constructor for class Truffle not found");
    }

    return ctor;

noCtor:
   	throwNoSuchMethodError(env, "Constructor for class Truffle not found");
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

    ssize_t len = read(socketData.socketFD, (void*) (truffle), sizeof(Truffle));

}

  /*  fd_set read_fds, write_fds, except_fds;
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
}*/


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
    jclass truffleClass = getTruffleClass(env);
    _CHECK_JAVA_EXCEPTION(env);

	jmethodID truffleCtor = getTruffleCtor(env, truffleClass);
	_CHECK_JAVA_EXCEPTION(env);

	jobject truffleObject = (*env)->NewObject(env, truffleClass, truffleCtor);
    _CHECK_JAVA_EXCEPTION(env);
	check(truffleObject != NULL, "could not create Truffle");

	Truffle truffle;

	check(getNextTruffle(env, &truffle) >= 0, "getNextTruffle failed!");

    jstring idStr;

    // get setAttribute method
    jmethodID setAttributeMID = (*env)->GetMethodID(env, truffleClass, "setAttribute", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;");
    check_to(setAttributeMID != 0, noSetAttribute, "setAttribute method not found");


    ///////////////////////////
    // start of long objects //
    ///////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    // get the long class
    jclass longClass = getClassByName(env, "java/lang/Long");
    check(longClass != NULL, "could not get long class");

    // get the long ctor
    jmethodID longCtor = (*env)->GetMethodID(env, longClass, "<init>", "(J)V");
    check(longCtor != NULL, "Could not fetch long ctor");
    //////////////////////////////////////////////////////////////////////////////////////

    /////////// start of setAttribute(Long.class, "sourceMacAddress", srcAddr) ///////////
    // create the long object for srcMacAddr
    jobject srcMacAddr = (*env)->NewObject(env, longClass, longCtor, truffle.etherHeader.sourceMacAddress);
    check(srcMacAddr != NULL, "Could not create srcMacAddr Long object");

    // create the id string for sourceMacAddress
    idStr = (*env)->NewStringUTF(env, "sourceMacAddress");
    check(idStr != NULL, "could not create id string for sourceMacAddress");

    (*env)->CallObjectMethod(env, truffleObject, setAttributeMID, longClass, idStr, srcMacAddr);
    idStr = NULL;
    /////////// end of setAttribute(Long.class, "sourceMacAddress", srcAddr) ///////////


    /////////// start of setAttribute(Long.class, "destMacAddress", destAddr) ///////////
    // create the id string for destMacAddress
    idStr = (*env)->NewStringUTF(env, "destMacAddress");
    check(idStr != NULL, "could not create id string for destMacAddress");

    // create the long object for destMacAddr
    jobject destMacAddr = (*env)->NewObject(env, longClass, longCtor, truffle.etherHeader.destMacAddress);
    check(destMacAddr != NULL, "could not create destMacAddr Long object");

    (*env)->CallObjectMethod(env, truffleObject, setAttributeMID, longClass, idStr, destMacAddr);
    idStr = NULL;
    /////////// end of setAttribute(Long.class, "destMacAddress", destAddr) ///////////

    /////////////////////////
    // end of long objects //
    /////////////////////////


    ////////////////////////////
    // start of short objects //
    ////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    // get the short class
    jclass shortClass = getClassByName(env, "java/lang/Short");
    check(shortClass != NULL, "could not get short class");

    // get the long ctor
    jmethodID shortCtor = (*env)->GetMethodID(env, shortClass, "<init>", "(S)V");
    check(shortCtor != NULL, "Could not fetch short ctor");
    //////////////////////////////////////////////////////////////////////////////////////

    /////////// start of setAttribute(Long.class, "destMacAddress", destAddr) ///////////
    // create the id string for destMacAddress
    idStr = (*env)->NewStringUTF(env, "etherType");
    check(idStr != NULL, "could not create id string for etherType");

    // create the long object for destMacAddr
    jobject etherType = (*env)->NewObject(env, shortClass, shortCtor, truffle.etherHeader.etherType);
    check(etherType != NULL, "could not create etherType Short object");

    (*env)->CallObjectMethod(env, truffleObject, setAttributeMID, shortClass, idStr, etherType);
    idStr = NULL;
    /////////// end of setAttribute(Long.class, "destMacAddress", destAddr) ///////////

    //////////////////////////
    // end of short objects //
    //////////////////////////

	return truffleObject;

noSetAttribute:
    throwNoSuchMethodError(env, "SetAttribute method not found");
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
