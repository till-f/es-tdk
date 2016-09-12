#include <stdio.h>

#include "fzi_mottem_runtime_rti_vector_VectorXLWrapper.h"

#include "globals.h"
#include "vxlapi.h"

#define RX_QUEUE_SIZE      4096

XLdriverConfig  g_xlDrvConfig;

JNIEXPORT jint JNICALL Java_fzi_mottem_runtime_rti_vector_VectorXLWrapper_jniConnect(JNIEnv* env, jobject thisObject)
{
    XLstatus xlStatus;
	XLaccess xlChanMaskTx = 0;	
    XLaccess xlPermissionMask = 0;
    unsigned int xlBaudRate = 1000000;  // 1000000 ^= 1000 kBps
	unsigned char xlChanIndex = 0;
	char g_AppName[XL_MAX_LENGTH+1]  = "VectorXLWrapper"; 
	jclass wrapper;
    jmethodID id_getChannel;
	XLaccess channelMask;
	jint channelNumber;
	XLportHandle portHandle;

    printf("VXL: Connecting\n");
    fflush(stdout);

    //each application has to call this function first to get access to the driver
	xlStatus = xlOpenDriver();
	
   	if(XL_SUCCESS != xlStatus)
    {
		printf("VXL: Could not open driver!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
    }

    // ------------------------------------
	// get/print the hardware configuration
	// ------------------------------------
	xlStatus = xlGetDriverConfig(&g_xlDrvConfig);

   	if(XL_SUCCESS != xlStatus)
    {
		printf("VXL: Could not get driver config!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
    }

    wrapper = (*env)->GetObjectClass(env,thisObject);
    id_getChannel = (*env)->GetMethodID(env, wrapper, "getChannel", "()I");

    channelNumber = (*env)->CallIntMethod(env, thisObject, id_getChannel);
	if (g_xlDrvConfig.channel[(int)channelNumber].channelBusCapabilities & XL_BUS_ACTIVE_CAP_CAN)
	{
	    channelMask = g_xlDrvConfig.channel[(int)channelNumber].channelMask;
	    xlPermissionMask = (XLaccess)channelMask;
	}
    else
	{
		printf("VXL: Channel is not CAN!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
	}

	//opens a port for can and grants access to all channels defined by accessMask  
    xlStatus = xlOpenPort(&portHandle, g_AppName, (XLaccess)channelMask, &xlPermissionMask, RX_QUEUE_SIZE, XL_INTERFACE_VERSION, XL_BUS_TYPE_CAN);

    if(XL_SUCCESS != xlStatus)
    {
		printf("VXL: Could not open port!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
    }

	if (XL_INVALID_PORTHANDLE == portHandle)
    {
        printf("VXL: Invalid port handle!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
    }

    // set the bus parameters (baudrate)
    if (channelMask == (channelMask & xlPermissionMask))
    {
        xlStatus = xlCanSetChannelBitrate(portHandle, channelMask, xlBaudRate);
    } 
    else
    {
        printf("VXL: No init access!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
    }

	if(XL_SUCCESS != xlStatus)
    {
        printf("VXL: Could not set channel bit rate!\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
	}

	// go with the selected channel on bus
	xlStatus = xlActivateChannel(portHandle, (XLaccess)channelMask, XL_BUS_TYPE_CAN, XL_ACTIVATE_RESET_CLOCK);

	if(XL_SUCCESS != xlStatus)
    {
        printf("VXL: Could not activate channel\n");
    	fflush(stdout);
        return XL_INVALID_PORTHANDLE;
	}

    printf("VXL: Connected\n");
    fflush(stdout);

    return portHandle;
}

JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_vector_VectorXLWrapper_jniDisconnect(JNIEnv* env, jobject thisObject, jint portHandle)
{
    XLstatus xlStatus;

    printf("VXL: Disconnecting\n");
    fflush(stdout);

    xlStatus = xlClosePort(portHandle);

    if(XL_SUCCESS != xlStatus)
    {
        printf("VXL: Could not close port\n");
    	fflush(stdout);
        return;
	}

    xlStatus = xlCloseDriver();

    if(XL_SUCCESS != xlStatus)
    {
        printf("VXL: Could not close driver\n");
    	fflush(stdout);
        return;
	}
}

JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_vector_VectorXLWrapper_jniSendMessage(JNIEnv* env, jobject thisObject, jint portHandle, jint channelNr, jint id, jbyteArray messageData)
{
    XLstatus xlStatus;
    XLevent xlEvent;
    XLportHandle pHandle = (XLportHandle)portHandle;
    int cNumber = channelNr;
    XLaccess cMask = (XLaccess)g_xlDrvConfig.channel[cNumber].channelMask;
    unsigned int messageCount = 1;

    // For Debugging purpose only:
    //printf("VXL TX: handle '%d', channel '%d', id '%d'\n", (int)portHandle, channelNr, id);
    //fflush(stdout);

    jsize byteCount = (*env)->GetArrayLength(env, messageData);
    unsigned char* srcData = (*env)->GetByteArrayElements(env, messageData, NULL);

    memset(&xlEvent, 0, sizeof(xlEvent));
    xlEvent.tag = XL_TRANSMIT_MSG;
    xlEvent.tagData.msg.id = (unsigned long)id;
    xlEvent.tagData.msg.dlc = (unsigned short)byteCount;
    xlEvent.tagData.msg.flags = 0;
    memcpy(xlEvent.tagData.msg.data, srcData, byteCount);

    (*env)->ReleaseByteArrayElements(env, messageData, srcData, JNI_ABORT);

    // For Debugging purpose only:
    //printf("DATA: ");
    //int i = 0;
    //for (i = 0; i < byteCount; i++)
    //{
    //    printf("%d ", xlEvent.tagData.msg.data[i]);
    //}
    //printf("\n");
    //fflush(stdout);

    xlStatus = xlCanTransmit(pHandle, cMask, &messageCount, &xlEvent);

    return;
}

JNIEXPORT void JNICALL Java_fzi_mottem_runtime_rti_vector_VectorXLWrapper_jniStartRxThread(JNIEnv* env, jobject thisObject, jint portHandle)
{
    XLevent xlEvent[RECEIVE_EVENT_SIZE];
    XLhandle hMsgEvent;
    XLstatus thread_xlStatus = XL_ERROR;
    unsigned int rxEventCount = 0;

    printf("VXL RX: entered with portHandle '%d'\n", (int)portHandle);
    fflush(stdout);

    // tell the driver to send an event in order to notify the application if there are events in the port's receive queue
    thread_xlStatus = xlSetNotification(portHandle, &hMsgEvent, 1);

    if (XL_SUCCESS != thread_xlStatus)
    {
        printf("VXL RX: could not set notifications for port\n");
        fflush(stdout);
        return;
    }

    jclass wrapperObj = (*env)->GetObjectClass(env, thisObject);
    jmethodID id_getCancelRXRequest = (*env)->GetMethodID(env, wrapperObj, "getCancelRXRequest", "()I");
    jmethodID id_notifyMessage = (*env)->GetMethodID(env, wrapperObj, "notifyMessage", "(I[B)V");

    while (1)
    {
        // wait for event on recieve queue (e.g. new can message)
        DWORD waitResult = WaitForSingleObject(hMsgEvent, 10);

        // check whether cancel was requested in the meantime; in this case exit the loop
        jint cancelRequested = (*env)->CallIntMethod(env, thisObject, id_getCancelRXRequest);
        if (cancelRequested)
        {
            printf("VXL RX: cancel requested\n");
            fflush(stdout);
            return;
        }

        // wait above timed out or another failure; in this case it is not desired to receive anything; wait again
        if (waitResult != WAIT_OBJECT_0)
        {
            continue;
        }

        rxEventCount = RECEIVE_EVENT_SIZE;
        thread_xlStatus = xlReceive((XLportHandle)portHandle, &rxEventCount, xlEvent);

        if (thread_xlStatus != XL_SUCCESS)
        {
            printf("VXL RX: RX failure\n");
            fflush(stdout);
            continue;
        }
        if (thread_xlStatus != XL_ERR_QUEUE_IS_EMPTY)
        {
            ResetEvent(hMsgEvent);
        }

        int eventIdx = 0;
        for (eventIdx = 0; eventIdx < rxEventCount; eventIdx++)
        {
            if (xlEvent[eventIdx].flags != 0 || xlEvent[eventIdx].tagData.msg.flags != 0)
            {
                break;
            }

            if (xlEvent[eventIdx].tagData.msg.dlc != 0)
            {
                unsigned int canID = (unsigned int)xlEvent[eventIdx].tagData.msg.id;
                unsigned int byteCount = xlEvent[eventIdx].tagData.msg.dlc;

                // For Debugging purpose only:
                //if (canID == 4)
                //{
                //    float fvalue = *((float*)(xlEvent[eventIdx].tagData.msg.data));
                //    printf("%d - %lu\n", eventIdx, xlEvent[eventIdx].timeStamp, fvalue);
                //    fflush(stdout);
                //}

                // For Debugging purpose only:
                //printf("VXL RX: CAN message with ID '%d' received (%d bytes)\n", canID, byteCount);
                //fflush(stdout);

                jbyteArray messageData = (*env)->NewByteArray(env, byteCount);
                (*env)->SetByteArrayRegion(env, messageData, 0, byteCount, xlEvent[eventIdx].tagData.msg.data);
                (*env)->CallVoidMethod(env, thisObject, id_notifyMessage, canID, messageData);
            }
        }
    }

    return;
}
