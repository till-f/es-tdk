/*
 * ek_PWM_init.h
 *
 *  Created on: Jan 5, 2016
 *      Author: lore_
 */

#include "stm32f4xx_tim.h"

#include "../../ek_EcuA/ek_MCAL/ek_GPIOx_init.h"

#ifndef EK_MCAL_EK_MCAL_CONF_EK_PWM_INIT_H_
#define EK_MCAL_EK_MCAL_CONF_EK_PWM_INIT_H_


void ek_PWM3_Init(void);
void ek_setPWM3_DutyCycle(uint16_t TimChannel, uint16_t pulse);

#endif /* EK_MCAL_EK_MCAL_CONF_EK_PWM_INIT_H_ */
