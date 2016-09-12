/*
 * ek_GPIOx_Init.h
 *
 *  Created on: Dec 15, 2015
 *      Author: lore_
 */

#include "stm32f4xx_gpio.h"
#include "stm32f4xx_rcc.h"
#include "stm32f4xx.h"


#ifndef EK_MCAL_EK_MCAL_CONF_EK_GPIOX_INIT_H_
#define EK_MCAL_EK_MCAL_CONF_EK_GPIOX_INIT_H_

typedef enum
{
	AHB1 = 0,  //
	AHB2,   	//
	APB1,
	APB2
}AMBA_BUS;


typedef struct {
	AMBA_BUS GPIOx_BUS_CLK;
	GPIO_TypeDef* GPIO_PORT; // Port
	GPIO_InitTypeDef GPIO_InitStructure;
	uint16_t GPIO_PIN; // Pin
	uint32_t RCCGPIO_CLK; // Clock
	uint16_t GPIO_PIN_SOURCE;   // ADC-Kanal
	uint16_t GPIO_AF;   // ADC-Kanal
} GPIO_CONF_PARAM;

GPIO_InitTypeDef GPIO_InitStructure;

void ek_GPIOx_Init();

#endif /* EK_MCAL_EK_MCAL_CONF_EK_GPIOX_INIT_H_ */
