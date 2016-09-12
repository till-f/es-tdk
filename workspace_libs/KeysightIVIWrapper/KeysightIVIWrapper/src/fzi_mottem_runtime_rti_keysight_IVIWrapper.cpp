#include <stdio.h>

#include "AgInfiniiVision.h"
#include "fzi_mottem_runtime_rti_keysight_IVIWrapper.h"
#include "stdafx.h"
#include <string.h>
#include <ctime>
#include "windows.h"


long triggerDelay;

inline void mySleep(clock_t sec) // clock_t is a like typedef unsigned int clock_t. Use clock_t instead of integer in this context
{
	clock_t start_time = clock();
	clock_t end_time = sec * 1000 + start_time;
	while(clock() != end_time);
}

long getTime() {
	SYSTEMTIME st;
	GetSystemTime(&st);
	WORD millis = (st.wSecond * 1000) + st.wMilliseconds;
	return millis;
}

ViStatus status;
	ViSession session;
	ViChar str[128];
	ViInt32 ErrorCode;
	ViChar ErrorMessage[256];
	ViBoolean simulate;
	bool sim;

	//8MB
	//const int size = 1024*1024*sizeof(double);

	//2MB
	//const int size = 256*1024*sizeof(double);

	//1MB
	const int size = 128*1024*sizeof(double);


	ViReal64* WaveformArray;
	ViInt32 WaveformSize = size/sizeof(double);

	const int resampleRate = 50;
	ViReal64 InitialX = 0.0;
	ViReal64 XIncrement = 0.0;
    ViInt32 ActualNumberOfPoints = 0;
	ViInt32 i = 0;

JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniConnect
	(JNIEnv * env, jobject, jstring javaString, jboolean simulation)
{
	
	char resource[] = "TCPIP0::ess-pitl-oszi-1.fzi.de::INSTR";
	//char resource[] = "TCPIP0::<ip or host name>::INSTR";

	sim = (bool)simulation;

	const char *nativeString = env->GetStringUTFChars(javaString, JNI_FALSE);
    char res[100]; 
	for( int k =0;k < strlen(nativeString);k++ ) { res[k] = nativeString[k]; }
	res[strlen(nativeString)] = 0;
	
	char options[] = "QueryInstrStatus=false, Simulate=false, DriverSetup= Model=DSO-X 2012A, Trace=false, TraceName=c:\\temp\\traceOut";
	if (sim) {
		strcpy(options, "QueryInstrStatus=false, Simulate=true, DriverSetup= Model=DSO-X 2012A, Trace=false, TraceName=c:\\temp\\traceOut");
	}  
	 
	
	ViBoolean idQuery = VI_TRUE;
	ViBoolean reset   = VI_FALSE;

	printf("-----------------------------\n \t  C++ OUTPUT\n-----------------------------\n\n");

	// Initialize the driver.  See driver help topic "Initializing the IVI-C Driver" for additional information - DIRECT DRIVER ACCESS
	status = AgInfiniiVision_InitWithOptions(res, idQuery, reset, options, &session);
	//status = AgInfiniiVision_InitWithOptions(res, idQuery, reset, options, &session);
	//status = AgInfiniiVision_InitWithOptions("TCPIP0::141.21.37.49::inst0::INSTR", VI_TRUE, VI_TRUE, "", &session);
	if(status)
	{
		// Initialization failed
		AgInfiniiVision_GetError(session, &ErrorCode, 255, ErrorMessage);
		printf("** InitWithOptions() Error: %d, %s\n", ErrorCode, ErrorMessage);
		printf("\nDone - Press Enter to Exit");
		getchar();  
		return;
		//return ErrorCode;
	}
	assert(session != VI_NULL);
	printf("Driver Initialized \n");

	// Read and output a few attributes
	// Return status checking omitted for example clarity
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_SPECIFIC_DRIVER_PREFIX, 127, str);
	assert(status == VI_SUCCESS);
	printf("DRIVER_PREFIX:      %s\n", str);
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_SPECIFIC_DRIVER_REVISION, 127, str);
	assert(status == VI_SUCCESS);
	printf("DRIVER_REVISION:    %s\n", str);
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_SPECIFIC_DRIVER_VENDOR, 127, str);
	assert(status == VI_SUCCESS);
	printf("DRIVER_VENDOR:      %s\n", str);
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_SPECIFIC_DRIVER_DESCRIPTION, 127, str);
	assert(status == VI_SUCCESS);
	printf("DRIVER_DESCRIPTION: %s\n", str);
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_INSTRUMENT_MODEL, 127, str);
	assert(status == VI_SUCCESS);
	printf("INSTRUMENT_MODEL:   %s\n", str);
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_INSTRUMENT_FIRMWARE_REVISION, 127, str);
	assert(status == VI_SUCCESS);
	printf("FIRMWARE_REVISION:  %s\n", str);
	status = AgInfiniiVision_GetAttributeViString(session, "", AGINFINIIVISION_ATTR_SYSTEM_SERIAL_NUMBER, 127, str);
	assert(status == VI_SUCCESS);
	printf("SERIAL_NUMBER:      %s\n", str);
	status = AgInfiniiVision_GetAttributeViBoolean(session, "", AGINFINIIVISION_ATTR_SIMULATE, &simulate);
	assert(status == VI_SUCCESS);
	if (simulate == VI_TRUE)
		wprintf(L"\nSIMULATE:           True\n\n");
	else
		wprintf(L"SIMULATE:           False\n\n");

	env->ReleaseStringUTFChars(javaString, nativeString);
}



JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniDisconnect
  (JNIEnv *, jobject)
{
	// Check instrument for errors
	ErrorCode = -1;
	printf("\n");
	while(ErrorCode!=0)
	{
		status = AgInfiniiVision_error_query( session, &ErrorCode, ErrorMessage);
		assert(status == VI_SUCCESS);
		printf("error_query: %d, %s\n", ErrorCode, ErrorMessage);
	}

	// Close the driver
	status = AgInfiniiVision_close(session);
	assert(status == VI_SUCCESS);
	session = VI_NULL;
	printf("Driver Closed \n");

//	printf("\nDone - Press Enter to Exit");
//	getchar();
}

JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniTrigger
	(JNIEnv *env, jobject, jdouble triggerLvl, jboolean valPositive) {
//	printf("TRIGGERRRR\n");
	AgInfiniiVision_AcquisitionStop(session);
	WaveformArray = (ViReal64*)malloc(size);
	AgInfiniiVision_ConfigureInitiateContinuous(session, VI_TRUE);
	mySleep(2);

	ViReal64 lvl = triggerLvl;
	printf("Trigger set at: %.15g\n\n", lvl);
	if (valPositive) {
		AgInfiniiVision_ConfigureEdgeTriggerSource(session, "Channel1",lvl ,AGINFINIIVISION_VAL_POSITIVE);
	} else {
		AgInfiniiVision_ConfigureEdgeTriggerSource(session, "Channel1",lvl ,AGINFINIIVISION_VAL_NEGATIVE);
	}

	long st = getTime();
	status = AgInfiniiVision_MeasurementFetchWaveform(session,"Channel1", WaveformSize, WaveformArray, &ActualNumberOfPoints, &InitialX, &XIncrement);
	long et = getTime();
	triggerDelay = et - st;

	//Array with new sampling rate	
	int buffsize = (size*sizeof(jdouble*))/(resampleRate*sizeof(double));
	jdouble *buff = (jdouble*)malloc(buffsize);
//	printf("Waveform Data: Size: \n\n" );
	int j = 0;
	for ( i = 1; i< ActualNumberOfPoints; i += resampleRate) { 
//		printf("\t%.15g\n\n",WaveformArray[i]);
		buff[j] = (jdouble)WaveformArray[i];
		j++;
	}
	printf("We are prepared for: %d points.\nActualNumberOfPoints: %d, SizeOFActualArray %d \nInitialX: %.15g, XIncrement: %.15g \n\n", buffsize, ActualNumberOfPoints, j, InitialX, XIncrement);
    
	AgInfiniiVision_AcquisitionStop(session);

    jdoubleArray retArray = env->NewDoubleArray(j);
	env->SetDoubleArrayRegion(retArray, 0,j, buff);
    return retArray;
}


JNIEXPORT jlong JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetTriggerDelay
	(JNIEnv *, jobject) 
{
	return (jlong)triggerDelay;
}

JNIEXPORT jdouble JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetSampleRate
	(JNIEnv *, jobject) 
{

	ViReal64 sr;
	status = AgInfiniiVision_SampleRate(session, &sr);
	return (jdouble)sr;
}

JNIEXPORT jdouble JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetStartTime
	(JNIEnv *, jobject) 
{

	ViReal64 sr;
	status = AgInfiniiVision_GetAttributeViReal64(session,"",AGINFINIIVISION_ATTR_ACQUISITION_START_TIME, &sr);
	return (jdouble)sr;
}

JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniScaledTrigger
	(JNIEnv *env, jobject, jdouble triggerLvl, jboolean valPositive, jdouble scalefactor) {
//	printf("TRIGGERRRR\n");
	AgInfiniiVision_AcquisitionStop(session);
	WaveformArray = (ViReal64*)malloc(size);
	AgInfiniiVision_ConfigureInitiateContinuous(session, VI_TRUE);
	mySleep(2);

	ViReal64 scale = scalefactor;
	
	status = AgInfiniiVision_SetAttributeViReal64(session, "", AGINFINIIVISION_ATTR_TIMEBASE_HORIZONTAL_SCALE, scale);

	ViReal64 lvl = triggerLvl;
	printf("Trigger set at: %.15g\n\n", lvl);
	if (valPositive) {
		AgInfiniiVision_ConfigureEdgeTriggerSource(session, "Channel1",lvl ,AGINFINIIVISION_VAL_POSITIVE);
	} else {
		AgInfiniiVision_ConfigureEdgeTriggerSource(session, "Channel1",lvl ,AGINFINIIVISION_VAL_NEGATIVE);
	}

	long st = getTime();
	status = AgInfiniiVision_MeasurementFetchWaveform(session,"Channel1", WaveformSize, WaveformArray, &ActualNumberOfPoints, &InitialX, &XIncrement);
	long et = getTime();
	triggerDelay = et - st;
	
	//Array with new sampling rate	
	int buffsize = (size*sizeof(jdouble*))/(resampleRate*sizeof(double));
	jdouble *buff = (jdouble*)malloc(buffsize);
//	printf("Waveform Data: Size: \n\n" );
	int j = 0;
	for ( i = 1; i< ActualNumberOfPoints; i += resampleRate) { 
//		printf("\t%.15g\n\n",WaveformArray[i]);
		buff[j] = (jdouble)WaveformArray[i];
		j++;
	}
	printf("We are prepared for: %d points.\nActualNumberOfPoints: %d, SizeOFActualArray %d \nInitialX: %.15g, XIncrement: %.15g \n\n", buffsize, ActualNumberOfPoints, j, InitialX, XIncrement);
    
	AgInfiniiVision_AcquisitionStop(session);

    jdoubleArray retArray = env->NewDoubleArray(j);
	env->SetDoubleArrayRegion(retArray, 0,j, buff);
    return retArray;
}

JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniWidthTrigger
	(JNIEnv *env, jobject, jdouble triggerLvl, jdouble lo, jdouble hi, jint polarity, jint mode) {
//	printf("TRIGGERRRR\n");
	AgInfiniiVision_AcquisitionStop(session);
	WaveformArray = (ViReal64*)malloc(size);
	AgInfiniiVision_ConfigureInitiateContinuous(session, VI_TRUE);
	mySleep(2);
	

	ViReal64 lvl = triggerLvl;
	ViReal64 lt = lo;
	ViReal64 ht = hi;
	ViInt32 pol = polarity;
	ViInt32 condition = mode;
	printf("Trigger set at: %.15g\n\n", lvl);
	AgInfiniiVision_ConfigureWidthTriggerSource(session, "Channel1",lvl ,lt,ht,pol ,condition);
	
	long st = getTime();
	status = AgInfiniiVision_MeasurementFetchWaveform(session,"Channel1", WaveformSize, WaveformArray, &ActualNumberOfPoints, &InitialX, &XIncrement);
	long et = getTime();
	triggerDelay = et - st;
	
	//Array with new sampling rate	
	int buffsize = (size*sizeof(jdouble*))/(resampleRate*sizeof(double));
	jdouble *buff = (jdouble*)malloc(buffsize);
//	printf("Waveform Data: Size: \n\n" );
	int j = 0;
	for ( i = 1; i< ActualNumberOfPoints; i += resampleRate) { 
//		printf("\t%.15g\n\n",WaveformArray[i]);
		buff[j] = (jdouble)WaveformArray[i];
		j++;
	}
	printf("We are prepared for: %d points.\nActualNumberOfPoints: %d, SizeOFActualArray %d \nInitialX: %.15g, XIncrement: %.15g \n\n", buffsize, ActualNumberOfPoints, j, InitialX, XIncrement);
    
	AgInfiniiVision_AcquisitionStop(session);

    jdoubleArray retArray = env->NewDoubleArray(j);
	env->SetDoubleArrayRegion(retArray, 0,j, buff);
    return retArray;
}


JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetWaveform
  (JNIEnv *env, jobject)
{

	printf("---------\nGet Waveform!\n---------\n");
	AgInfiniiVision_AcquisitionStop(session);
	WaveformArray = (ViReal64*)malloc(size);
	AgInfiniiVision_ConfigureInitiateContinuous(session, VI_TRUE);
	mySleep(2);

	//AgInfiniiVision_ConfigureEdgeTriggerSource(session, "Channel1",lvl ,AGINFINIIVISION_VAL_POSITIVE);
	status = AgInfiniiVision_MeasurementsAutoSetup(session);
	status = AgInfiniiVision_MeasurementsInitiate(session);

	long st = getTime();
	status = AgInfiniiVision_MeasurementFetchWaveform(session,"Channel1", WaveformSize, WaveformArray, &ActualNumberOfPoints, &InitialX, &XIncrement);
	long et = getTime();
	long diff = et-st;

	/*jclass iviTCclass = env->FindClass("fzi\mottem\runtime\rti\keysight\IVIWrapper");
	printf ("found?!!\n");
    jclass iviTCref = (jclass) env->NewGlobalRef(iviTCclass);
	jmethodID setTD = env->GetMethodID(iviTCref, "setTriggerDelay","(L)V");
	jobject javaObjectRef = env->NewObject(iviTCref, setTD);
	printf ("CAAAAAAALLLL!\n");
	env->CallVoidMethod(javaObjectRef, setTD, diff);
//	env->CallVoidMethod(jobject,"setTriggerDelay", (jlong) diff);
*/

	triggerDelay = diff;


	//Array with new sampling rate	
	int buffsize = (size*sizeof(jdouble*))/(resampleRate*sizeof(double));
	jdouble *buff = (jdouble*)malloc(buffsize);
//	printf("Waveform Data: Size: \n\n" );
	int j = 0;
	if (sim ) {
		for ( i = 1; i< ActualNumberOfPoints; i += 1) { 
	//		printf("\t%.15g\n\n",WaveformArray[i]);
			buff[j] = (jdouble)WaveformArray[i];
			j++;
		}
	} else {
		for ( i = 1; i< ActualNumberOfPoints; i += resampleRate) { 
//		printf("\t%.15g\n\n",WaveformArray[i]);
		buff[j] = (jdouble)WaveformArray[i];
		j++;
	}
	}
	
	printf("We are prepared for: %d points.\nActualNumberOfPoints: %d, SizeOFActualArray %d \nInitialX: %.15g, XIncrement: %.15g \n\n", buffsize, ActualNumberOfPoints, j, InitialX, XIncrement);
    
	AgInfiniiVision_AcquisitionStop(session);

    jdoubleArray retArray = env->NewDoubleArray(j);
	env->SetDoubleArrayRegion(retArray, 0,j, buff);
    return retArray;
/*	AgInfiniiVision_AcquisitionStop(session);
	WaveformArray = (ViReal64*)malloc(size);
	AgInfiniiVision_ConfigureInitiateContinuous(session, VI_TRUE);
	mySleep(2);

	status = AgInfiniiVision_MeasurementsAutoSetup(session);
	status = AgInfiniiVision_MeasurementsInitiate(session);
	status = AgInfiniiVision_MeasurementFetchWaveform(session,"Channel1", WaveformSize, WaveformArray, &ActualNumberOfPoints, &InitialX, &XIncrement);
	
	//Array with new sampling rate	
	int buffsize = (size*sizeof(jdouble*))/(resampleRate*sizeof(double));
	jdouble *buff = (jdouble*)malloc(buffsize);
//	printf("Waveform Data: Size: \n\n" );
	int j = 0;
	for ( i = 1; i< ActualNumberOfPoints; i += resampleRate) { 
//		printf("\t%.15g\n\n",WaveformArray[i]);
		buff[j] = (jdouble)WaveformArray[i];
		j++;
	}
	printf("We are prepared for: %d points.\nActualNumberOfPoints: %d, SizeOFActualArray %d \nInitialX: %.15g, XIncrement: %.15g \n\n", buffsize, ActualNumberOfPoints, j, InitialX, XIncrement);
    
	AgInfiniiVision_AcquisitionStop(session);

    jdoubleArray retArray = env->NewDoubleArray(j);
	env->SetDoubleArrayRegion(retArray, 0,j, buff);
    return retArray; */
}


JNIEXPORT jdoubleArray JNICALL Java_fzi_mottem_runtime_rti_keysight_IVIWrapper_jniGetScaledWaveform
  (JNIEnv *env, jobject, jdouble scalefactor)
{

	printf("---------\nGet Waveform!\n---------\n");
	AgInfiniiVision_AcquisitionStop(session);
	WaveformArray = (ViReal64*)malloc(size);
	AgInfiniiVision_ConfigureInitiateContinuous(session, VI_TRUE);
	mySleep(2);

	//AgInfiniiVision_ConfigureEdgeTriggerSource(session, "Channel1",lvl ,AGINFINIIVISION_VAL_POSITIVE);
	status = AgInfiniiVision_MeasurementsAutoSetup(session);

	ViReal64 scale = scalefactor;
	status = AgInfiniiVision_SetAttributeViReal64(session, "", AGINFINIIVISION_ATTR_TIMEBASE_HORIZONTAL_SCALE,scale);
	status = AgInfiniiVision_AcquisitionSingleAcquisition(session);
	long st = getTime();
	status = AgInfiniiVision_MeasurementFetchWaveform(session,"Channel1", WaveformSize, WaveformArray, &ActualNumberOfPoints, &InitialX, &XIncrement);
	long et = getTime();
	long diff = et-st;

	//Array with new sampling rate	
	int buffsize = (size*sizeof(jdouble*))/(resampleRate*sizeof(double));
	jdouble *buff = (jdouble*)malloc(buffsize);
//	printf("Waveform Data: Size: \n\n" );
	int j = 0;
	for ( i = 1; i< ActualNumberOfPoints; i += resampleRate) { 
//		printf("\t%.15g\n\n",WaveformArray[i]);
		buff[j] = (jdouble)WaveformArray[i];
		j++;
	}
	printf("We are prepared for: %d points.\nActualNumberOfPoints: %d, SizeOFActualArray %d \nInitialX: %.15g, XIncrement: %.15g \n\n", buffsize, ActualNumberOfPoints, j, InitialX, XIncrement);
    
	AgInfiniiVision_AcquisitionStop(session);

    jdoubleArray retArray = env->NewDoubleArray(j);
	env->SetDoubleArrayRegion(retArray, 0,j, buff);
    return retArray;
}