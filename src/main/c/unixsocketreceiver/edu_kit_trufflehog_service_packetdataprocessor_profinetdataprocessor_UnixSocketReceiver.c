#include <sys/socket.h>
#include <sys/types.h>
#include <sys/un.h>
#include <unistd.h>
#include <stdlib.h>
#include <pwd.h>
#include "edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver.h"
#include "truffle.h"
#include "snortComm.h"

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
	char socketFile[108];

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
		if (pw == NULL)
			return -1;

		homeDir = pw->pw_dir;
	}

	if ((strlen(homeDir) + strlen(SOCKET_NAME)) > 107)
		return -1;

	sockData->address.sun_path[0] = '\0';
	strcat(sockData->address.sun_path, homeDir);
	strcat(sockData->address.sun_path, SOCKET_NAME);

	return 0;
}

int openSocket()
{
    printf("DEBUG: opening socket..\n");

	// Init socket data
	getSocketFile(&socketData);
	socketData.address.sun_family = AF_UNIX;

	socketData.socketFD = socket(AF_UNIX, SOCK_SEQPACKET, 0);
	if (socketData.socketFD < 0)
	{
		printf("ERROR: could not create socket!\n");
		return -1;
	}

	int32_t len = strlen(socketData.address.sun_path) + sizeof(socketData.address.sun_family);
	if (connect(socketData.socketFD, (struct sockaddr*) &socketData.address, len) < 0)
	{
		printf("ERROR: could not connect to socket \"%s\", \"%d\"\n", socketData.address.sun_path, socketData.address.sun_family);
		return -1;
	}

	char *buf = "Hello Snort";

	int32_t check = write(socketData.socketFD, buf, strlen(buf));

	if (check == -1)
	{
		printf("Error sending hello snort\n");
		return -1;
	}

	printf("Successfully connected to snort");

	return 0;
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    openIPC
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_openIPC (JNIEnv *env, jobject thisObj)
{
	if (openSocket() < 0)
		throwSnortPluginNotRunningException(env, "Nope");
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
		return throwNoClassDefError(env, truffleClassName);

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
