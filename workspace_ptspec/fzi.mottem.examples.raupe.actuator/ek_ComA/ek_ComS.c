/*
 * ek_ComS.c
 *
 *  Created on: Feb 4, 2016
 *      Author: lore_
 */

#include "../ek_ComA/ek_ComS.h"
#include "../ek_ComA/ek_ComHwA/ek_CAN_init.h"


void Com_Init()//( const Com_ConfigType* config )
{
	#if defined (__USE_STM32F4XX_SPD_CAN__) || defined (__USE_STM32F4XX_SPD_ALL_)
		ek_CANx_Init();
	#endif
}

uint8 Com_SendSignal( Com_SignalIdType SignalId, const uint32 SignalData )
{
	uint32 temp_var;
	CanTxMsg txMsg;

	temp_var = SignalData;

	txMsg.StdId = 0x1;
	txMsg.ExtId = 0x00;
	txMsg.RTR = CAN_RTR_DATA;
	txMsg.IDE = CAN_ID_STD;
	txMsg.DLC = 8;

	txMsg.Data[0] = SignalId;
	txMsg.Data[3] = 0;
	txMsg.Data[2] = 0;
	txMsg.Data[1] = 0;

	txMsg.Data[7] = temp_var&0xff;
	txMsg.Data[6] = temp_var&0xff00;
	txMsg.Data[5] = temp_var&0xff0000;
	txMsg.Data[4] = temp_var&0xff000000;

	CAN_Transmit(CANx, &txMsg);

	//PDU...keine Periodische Tx und auch kein shed. vom TX
	//keine Reception Deadline Monitoring
	return E_OK;

}

uint8 Com_ReceiveSignal( Com_SignalIdType SignalId, uint32 SignalData )
{
	if(Rte_Vfb_update_extern_Value(SignalId, SignalData)==E_OK){
		return E_OK;
	}else{
		return E_NOT_OK;
	}
}
