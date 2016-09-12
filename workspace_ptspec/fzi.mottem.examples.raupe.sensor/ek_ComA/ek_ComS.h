/*
 * ek_ComS.h
 *
 *  Created on: Feb 4, 2016
 *      Author: lore_
 */




#ifndef EK_COMA_EK_COMS_H_
#define EK_COMA_EK_COMS_H_

#include "../ee_Types/Std_Types.h"
#include "../ek_RTE/Rte_Vfb.h"
#include "ee.h"
#include "../ek_ComA/ek_ComStack_Types.h"
#include "ek_ComHwA/ek_CAN_init.h"

#if defined (__USE_STM32F4XX_SPD_CAN__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_can.h"
#include "ek_ComHwA/ek_CAN_init.h"
#endif


void Com_Init();
uint8 Com_SendSignal( Com_SignalIdType SignalId, const uint32 SignalData );
uint8 Com_ReceiveSignal( Com_SignalIdType SignalId, uint32 SignalDataPtr );

#endif /* EK_COMA_EK_COMS_H_ */
