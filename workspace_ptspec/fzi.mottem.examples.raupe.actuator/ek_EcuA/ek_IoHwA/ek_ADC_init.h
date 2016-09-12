/*
 * ek_ADC_init.h
 *
 *  Created on: Jan 8, 2016
 *      Author: lore_
 */

#include "stm32f4xx_adc.h"
#include "stm32f4xx_dma.h"

#include "../../ek_EcuA/ek_MCAL/ek_GPIOx_init.h"

#ifndef EK_MCAL_EK_MCAL_CONF_EK_ADC_INIT_H_
#define EK_MCAL_EK_MCAL_CONF_EK_ADC_INIT_H_


//MCAL_ADC_START
#define ADC3_PIN	GPIO_Pin_2
#define ADC3_PORT	GPIOC
//MCAL_ADC_STOP


__IO uint16_t ADC3ConvertedValue;
__IO uint32_t ADC3ConvertedVoltage;

#define ADC3_DR_ADDRESS     ((uint32_t)0x4001224C)


void ek_ADC_Init(void);

uint32_t ek_getADC3_Value();
uint32_t ek_getADC3_Voltage();

#endif /* EK_MCAL_EK_MCAL_CONF_EK_ADC_INIT_H_ */


//Channel 			ADC1 	ADC2 	ADC3
//APB 				2 		2 		2
//ADC Channel 0 	PA0 	PA0 	PA0
//ADC Channel 1 	PA1 	PA1 	PA1
//ADC Channel 2 	PA2 	PA2 	PA2
//ADC Channel 3 	PA3 	PA3 	PA3
//ADC Channel 4 	PA4 	PA4 	PF6
//ADC Channel 5 	PA5 	PA5 	PF7
//ADC Channel 6 	PA6 	PA6 	PF8
//ADC Channel 7 	PA7 	PA7 	PF9
//ADC Channel 8 	PB0 	PB0 	PF10
//ADC Channel 9 	PB1 	PB1 	PF3
//ADC Channel 10 	PC0 	PC0 	PC0
//ADC Channel 11 	PC1 	PC1 	PC1
//ADC Channel 12 	PC2 	PC2 	PC2   ----- PC2 active as ADC3
//ADC Channel 13 	PC3 	PC3 	PC3
//ADC Channel 14 	PC4 	PC4 	PF4
//ADC Channel 15 	PC5 	PC5 	PF5
