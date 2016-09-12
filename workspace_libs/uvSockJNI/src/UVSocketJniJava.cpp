#include <iostream>
#include <fstream>
#include <UVSC_C.h>
#include <string>
#include <map>
#include<vector>
#include <sstream>
#include <iterator>
#include <algorithm>
#include "UVSocketJniJava.h"
#define MIN_AUTO_PORT_NUMBER 5101
#define MAX_AUTO_PORT_NUMBER 5110
#define BREAKPOINT_RESPONSE_COUNT 128
using namespace std;


int connectionHandle = 0;
int port;
static BKRSP breakpointResponse[BREAKPOINT_RESPONSE_COUNT];
static int bkptRspIndexes[BREAKPOINT_RESPONSE_COUNT];
std::ifstream infile("C:\\Users\\jabbar\\Documents\\Praktikum\\Examples\\Blinky\\Flash\\Blinky.symbols");
std::map<std::string, std::string> m;
std::vector<std::string> v;
/*------- load project to target-----------*/
JNIEXPORT jint JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_load
  (JNIEnv *, jobject){

	UVSC_STATUS status = UVSC_PRJ_FLASH_DOWNLOAD(connectionHandle);

			if (status != UVSC_STATUS_SUCCESS)
	{
        cout<<"error downloading.."<<endl;

	
    }	
			return status;
}

/*--------reset CPU (target processor)------------*/
JNIEXPORT jint JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_reset
  (JNIEnv *, jobject){
	  
	  int temp = 0;
		int x=0;
	  UVSC_STATUS  debugState=UVSC_DBG_STATUS(connectionHandle, &temp);
	  UVSC_STATUS resetState=debugState;
	 do
		{
       	 debugState=  UVSC_DBG_STATUS(connectionHandle, &temp);
		}
	while (temp == 1);
	 
	if(temp == 0){
		 resetState = UVSC_DBG_RESET(connectionHandle);
	  	cout<<"reset called with state: "<<resetState<<endl;
	  }
	  
	return resetState;
}
/*--------------init:----------------------------------------------------------------------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_init
  (JNIEnv *, jobject){
	UVSC_STATUS status;
	
	status = UVSC_Init(MIN_AUTO_PORT_NUMBER, MAX_AUTO_PORT_NUMBER);
	

	if (status != UVSC_STATUS_SUCCESS)
	{
        cout<<"error initializing"<<endl;
		return status;
    }
  
	cout<<"initialized"<<endl;
 
	UVSC_RUNMODE uvRunmode;
	uvRunmode=UVSC_RUNMODE_NORMAL;
	//status = UVSC_OpenConnection(NULL, &connectionHandle, &port, "C:\\Keil_v5\\UV4\\uv4.exe", uvRunmode, uvsc_callback, NULL, "C:\\Users\\jabbar\\uvsclog.txt", false, uvsc_log_callback);
	status = UVSC_OpenConnection(NULL, &connectionHandle, &port, "C:\\Keil_v5\\UV4\\uv4.exe", uvRunmode, NULL, NULL, NULL, false, NULL);
	if (status != UVSC_STATUS_SUCCESS)
	{
        cout<<"error opening connection. handle: " << connectionHandle << ", port: " << port << endl;
		return status;
    }

	cout<<"connection opened. handle: " << connectionHandle << ", port: " << port << endl;
	return status;
}
/*---------show UVISION------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_showUVISION
  (JNIEnv *, jobject){
		UVSC_STATUS status = UVSC_GEN_SHOW(connectionHandle);
		 if (status == UVSC_STATUS_SUCCESS){
			cout<<"uvision opened"<<endl;
	}

return status;
}
/*-----------close connection-------------------------------------------------------------*/
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_closeConnection
  (JNIEnv *, jobject){
	  cout<<"About to close connection.\n" << endl;
	 UVSC_STATUS state= UVSC_CloseConnection(connectionHandle,TRUE);
	  cout<<"Connection closed with state: "<<state << endl;
}

/*-----------RUN--------------------------------------------------------------------------*/
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_run
  (JNIEnv *, jobject)
{ 
	UVSC_STATUS execState= UVSC_DBG_START_EXECUTION(connectionHandle);
	int temp;
	
	UVSC_STATUS debugState;

	do
		{
       	 debugState=  UVSC_DBG_STATUS(connectionHandle, &temp);
		}
	while (temp == 1);
	
}
/*-----------Enter Debug Mode--------------------------------------------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_enterDebugMode
  (JNIEnv *, jobject){
	cout<<"\nAbout to enter Debug Mode...\n"<<endl;

	UVSC_STATUS state= UVSC_DBG_ENTER(connectionHandle);
	if(state == 0)
	{
		cout<<"\nDebug Mode entered successfully...-> jump to main"<<endl;
	}

	return state;
}

/*--------Exit debug mode-------------------------------------------------------------------------------------------------------------------*/
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_exitDebugMode
	(JNIEnv *, jobject){

		UVSC_STATUS state= UVSC_DBG_EXIT(connectionHandle);
	  if(state == UVSC_STATUS_SUCCESS){
	  cout<<"\nDebug Mode exited successfully.."<<endl;
	  
	  }
}
/*-----------stop--------------------------------------------------------------------------------------------------------------------------------*/
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_stop
	(JNIEnv *, jobject){

	UVSC_STATUS state= 	UVSC_DBG_STOP_EXECUTION(connectionHandle);

	  if(state == UVSC_STATUS_SUCCESS){
	  cout<<"execution stoppped successfully.."<<endl;
	  
	  }

}
/*---------------call-----------------------------------------------------------------------------------------------------------*/
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_call
  (JNIEnv *, jobject, jint startAddr, jint stopAddr){

	 /* UVSC_STATUS state1 =  UVSC_DBG_RUN_TO_ADDRESS(connectionHandle, (UINT64)startAddr);
	  UVSC_STATUS state2 =  UVSC_DBG_RUN_TO_ADDRESS(connectionHandle, (UINT64)stopAddr);

	  UVSC_STATUS execState;
	  UINT64 currentAddress=startAddr;
	  int index=0;
	  UINT overallSize
	AMEM* amemData1 = (AMEM*)malloc(overallSize);
	AMEM* amemData2 = (AMEM*)malloc(overallSize);
	amemData->nAddr = startAddr;
		
	std::vector<byte> data;


	 do
		{
       	  execState= UVSC_DBG_START_EXECUTION(connectionHandle);
		  overallSize = sizeof(AMEM) + index;
		  amemData->nBytes = index;
		 UVSC_STATUS status = UVSC_DBG_MEM_READ(connectionHandle, amemData, overallSize);
		 currentAddress= amemData->nAddr;
		 
		  index++;
		 }
	
	 	while(currentAddress != stopAddr);*/
}

//----------run until----------
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_runUntil
  (JNIEnv *, jobject, jlong addr){

		
UVSC_STATUS state =  UVSC_DBG_RUN_TO_ADDRESS(connectionHandle, (UINT64)addr);

if(state == 0){
				cout<<" run until called successfully"<<endl;	
			 }
}

/*-------get data memory in address parameter----------------------------------------------------------------*/
JNIEXPORT jbyteArray JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_getValue (JNIEnv *env, jobject, jlong addr,jint nbytes)
{
	UINT overallSize = sizeof(AMEM) + nbytes - 1;
	AMEM* amemData = (AMEM*)malloc(overallSize);
	amemData->nAddr = addr;
	amemData->nBytes = nbytes;	
	std::vector<byte> data;
	UVSC_STATUS status = UVSC_DBG_MEM_READ(connectionHandle, amemData, overallSize);

	cout << "\nDONE READING." << endl;

	jbyteArray result= env->NewByteArray(nbytes);
	
 if (result == NULL) {
     return NULL; /* out of memory error thrown */
 }

 jbyte fill[64];
 for (int i = 0; i < nbytes; i++) {
	 data.push_back(amemData->aBytes[i]);
     fill[i] = amemData->aBytes[i];
 }
 // move from the temp structure to the java structure
 env->SetByteArrayRegion(result, 0, nbytes, fill);
 return result;

}

/*-------set the data -> address-------------------------------------------------------------------------------------------------*/
JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_setValue
  (JNIEnv* env, jobject, jlong addr, jbyteArray value,jint nbytes){

	UINT bytesToWrite = nbytes;
	UINT overallSize = sizeof(AMEM) + bytesToWrite - 1;
	AMEM* amemData = (AMEM*)malloc(overallSize);
			amemData->nAddr = addr;
			amemData->nBytes = bytesToWrite;


	  if (value != NULL) {
		cout<<"\nstart of copying..."<<endl;
			 jsize len  = env->GetArrayLength(value); 
			 jbyte* buffer = (jbyte *)malloc(len * sizeof(jbyte));

			 env->GetByteArrayRegion(value,0,len,buffer);

			 memcpy((xUC8*)(amemData->aBytes), buffer,len);
		     free(buffer);

		 printf("new data bytes: ");	

		  for ( int i = 0 ; i < nbytes ; ++i )  
		  {
			  printf(" %02lx", amemData->aBytes[i]);
				
		  }
	   cout<<"\ndata copy is done."<<endl;
    }

		UVSC_STATUS state = UVSC_DBG_MEM_WRITE(connectionHandle, amemData, overallSize);
		cout<<"0 = write method is called successfully: "<<state<<endl;
}

/*----------set breakpoint----------------------------------------------------------------------------------------------------*/
JNIEXPORT jstring JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_setBreakpoint
  (JNIEnv *env, jobject, jstring addr){

  int breakPointCreatedLen = sizeof(BKRSP);

  BKPARM breakPointNew;
  breakPointNew.type    = BRKTYPE_EXEC ;     // type of break := EXEC
  breakPointNew.count   = 1;                 // number of occurrances before hit
  breakPointNew.nCmdLen = 0;

  const char *nativeString = env->GetStringUTFChars( addr, 0);
  breakPointNew.nExpLen = env->GetStringLength(addr);
  strcpy(breakPointNew.szBuffer, nativeString);

  cout << "nExpLen: " << breakPointNew.nExpLen << endl;

  cout<<"about to create BP\n";
  flush(cout);

  UVSC_STATUS state = UVSC_DBG_CREATE_BP(connectionHandle,&breakPointNew,sizeof(breakPointNew),breakpointResponse,&breakPointCreatedLen);

  cout<<"done attempting to create BP\n";
  flush(cout);


   if (state == UVSC_STATUS_SUCCESS)
   {
		cout<<"\n Breakpoint prepared. Length of created structure: "<<breakPointCreatedLen<<endl;
   }
   else
   {
		cout<<"\n Breakpoint not created. Length of structure: "<<breakPointCreatedLen<<endl;
		cout<<"\n STATE. "<<state<<endl;
   }

   return addr;
}

JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_killBreakpoints
  (JNIEnv *, jobject){
	BKCHG pCbp;
	
		  	pCbp.type = CHG_KILLBP;
	         int  bBkptRspCount = 128;
			 int breakPointCreatedLen = sizeof(BKRSP);
			 UVSC_STATUS  stat = UVSC_DBG_ENUMERATE_BP(connectionHandle, breakpointResponse, bkptRspIndexes, &bBkptRspCount);
			 UVSC_DBG_CHANGE_BP(connectionHandle,&pCbp,breakPointCreatedLen,breakpointResponse,&breakPointCreatedLen);
	  memset (&pCbp, 0, sizeof (BKCHG));

	
	/*
	  CHG_KILLBP      = 1,    ///< Delete breakpoint
	  CHG_ENABLEBP    = 2,    ///< Enable breakpoint
	  CHG_DISABLEBP   = 3,    ///< Disable breakpoint
	 */
}

/**************************************set breakpoint alternativ get int as address*****************/
//JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_keil_UVSocketJniJava_setAddressBreakpoint
//	(JNIEnv *env, jobject, jint addr)
//
//{
//  int breakPointCreatedLen = sizeof(BKRSP);
//
//  BKPARM breakPointNew;
//  breakPointNew.type    = BRKTYPE_EXEC ;     // type of break := EXEC
//  breakPointNew.count   = 1;                 // number of occurrances before hit
//  breakPointNew.nCmdLen = 0;
//
//  char buf[64];
//  sprintf(buf, "%d", addr); 
//  strcpy(breakPointNew.szBuffer, buf);
//   breakPointNew.nExpLen = sizeof(breakPointNew.szBuffer);
//  cout << "nExpLen: " << breakPointNew.nExpLen << endl;
//
//  cout<<"about to create BP\n";
//  flush(cout);
//
//  UVSC_STATUS state = UVSC_DBG_CREATE_BP(connectionHandle,&breakPointNew,sizeof(breakPointNew),breakpointResponse,&breakPointCreatedLen);
//
//  cout<<"done attempting to create BP\n";
//  flush(cout);
//
//
//   if (state == UVSC_STATUS_SUCCESS)
//   {
//		cout<<"\n Breakpoint prepared. Length of created structure: "<<breakPointCreatedLen<<endl;
//   }
//   else
//   {
//		cout<<"\n Breakpoint not created. Length of structure: "<<breakPointCreatedLen<<endl;
//		cout<<"\n STATE. "<<state<<endl;
//   }
//}