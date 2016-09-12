/*
 * ek_GPIOx_init.c
 *
 *  Created on: Dec 15, 2015
 *      Author: eddy
 */

#include "../../ek_EcuA/ek_MCAL/ek_GPIOx_init.h"



void ek_GPIOx_Init(GPIO_CONF_PARAM GPIO_conf_param){

	switch(GPIO_conf_param.GPIOx_BUS_CLK) {
		case 0: RCC_AHB1PeriphClockCmd(GPIO_conf_param.RCCGPIO_CLK, ENABLE); break;
		case 1: RCC_AHB2PeriphClockCmd(GPIO_conf_param.RCCGPIO_CLK, ENABLE); break;
		case 2: RCC_APB1PeriphClockCmd(GPIO_conf_param.RCCGPIO_CLK, ENABLE); break;
		case 3: RCC_APB2PeriphClockCmd(GPIO_conf_param.RCCGPIO_CLK, ENABLE); break;
		default:  break;
	}

	//Initialize the Pins with the selected configuration
	GPIO_Init(GPIO_conf_param.GPIO_PORT, &(GPIO_conf_param.GPIO_InitStructure));

	if(GPIO_conf_param.GPIO_InitStructure.GPIO_Mode == GPIO_Mode_AF){
		GPIO_PinAFConfig(GPIO_conf_param.GPIO_PORT, GPIO_conf_param.GPIO_PIN_SOURCE, GPIO_conf_param.GPIO_AF);
	}

}




