/*
 * ek_CAN_init.h
 *
 *  Created on: Dec 12, 2015
 *      Author: eddy
 */

#include "stm32f4xx_can.h"

#include "../../ek_EcuA/ek_MCAL/ek_GPIOx_init.h"

#ifndef EK_MCAL_EK_MCAL_CONF_EK_CAN_INIT_H_
#define EK_MCAL_EK_MCAL_CONF_EK_CAN_INIT_H_

#define CANx            CAN1
#define CANx_AF         GPIO_AF_CAN1
#define CANx_GPIO_BANK  GPIOB
#define CANx_GPIO_CLK   RCC_AHB1Periph_GPIOB
#define CANx_PIN_RX     GPIO_Pin_8
#define CANx_PIN_RX_SRC GPIO_PinSource8
#define CANx_PIN_TX     GPIO_Pin_9
#define CANx_PIN_TX_SRC GPIO_PinSource9

void ek_CANx_Init();

#endif /* EK_MCAL_EK_MCAL_CONF_EK_CAN_INIT_H_ */
