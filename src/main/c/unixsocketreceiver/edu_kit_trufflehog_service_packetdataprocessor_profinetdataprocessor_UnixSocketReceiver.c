#include <sys/socket.h>
#include <sys/types.h>
#include <sys/un.h>
#include <unistd.h>
#include <stdlib.h>
#include "edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver.h"
#include "truffle.h"

//////////////////////////
//                      //
// Forward declarations //
//                      //
//////////////////////////

jint throwNoClassDefError(JNIEnv*, char*);
jint throwNoSuchMethodError(JNIEnv*, char*);
jint throwSnortPluginNotRunningException(JNIEnv*, char*);





typedef struct
{
	int server_sockfd;
	int client_sockfd;

	int client_detected;

	struct sockaddr_un serv_addr;
	struct sockaddr_un cli_addr;
	socklen_t clilen;
} SocketData;

int openSocket()
{
	//TODO implement
}

/*
 * Class:     edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver
 * Method:    openIPC
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver_openIPC (JNIEnv *env, jobject thisObj)
{
	//TODO initialize socket/connection
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
