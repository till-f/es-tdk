/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class fzi_mottem_runtime_rti_keysight_IVIWrapper */

#ifndef _Included_fzi_mottem_runtime_rti_keysight_IVIWrapper
#define _Included_fzi_mottem_runtime_rti_keysight_IVIWrapper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     fzi_mottem_runtime_rti_keysight_IVIWrapper
 * Method:    jniConnect
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniConnect
  (JNIEnv *, jobject, jstring, jboolean);


JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetScaledWaveform
  (JNIEnv *env, jobject, jdouble scalefactor);

JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniScaledTrigger
	(JNIEnv *env, jobject, jdouble triggerLvl, jboolean valPositive, jdouble scalefactor);

/*
 * Class:     fzi_mottem_runtime_rti_keysight_IVIWrapper
 * Method:    jniDisconnect
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniDisconnect
  (JNIEnv *, jobject);


JNIEXPORT jlong JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetTriggerDelay
	(JNIEnv *, jobject);
/*
 * Class:     fzi_mottem_runtime_rti_keysight_IVIWrapper
 * Method:    jniGetWaveform
 * Signature: ()[D
 */
JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetWaveform
  (JNIEnv *, jobject);

JNIEXPORT double JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetSampleRate
	(JNIEnv *, jobject);


JNIEXPORT double JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetStartTime
	(JNIEnv *, jobject);
/*
 * Class:     fzi_mottem_runtime_rti_keysight_IVIWrapper
 * Method:    jniTrigger
 * Signature: (DZ)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniTrigger
  (JNIEnv *, jobject, jdouble, jboolean);

/*
 * Class:     fzi_mottem_runtime_rti_keysight_IVIWrapper
 * Method:    jniWidthTrigger
 * Signature: (DDDII)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniWidthTrigger
  (JNIEnv *, jobject, jdouble, jdouble, jdouble, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
