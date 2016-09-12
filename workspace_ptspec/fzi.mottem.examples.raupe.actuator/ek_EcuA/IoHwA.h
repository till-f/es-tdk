/*
 * ek_mcal_init.h
 *
 *  Created on: Dec 12, 2015
 *      Author: eddy
 */


#ifndef EK_MCAL_EK_MCAL_INIT_H_
#define EK_MCAL_EK_MCAL_INIT_H_

#include "ee.h"
#include "stm32f4xx.h"

#include "misc.h"
#include "IoHwA_Types.h" //eddy


#if defined (__USE_STM32F4XX_SPD_ADC__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_adc.h"
#include "../ek_EcuA/ek_IoHwA/ek_ADC_init.h"
#endif


#if defined (__USE_STM32F4XX_SPD_DAC__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_dac.h"
#endif

#if defined (__USE_STM32F4XX_SPD_DMA__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_dma.h"
#endif

#if defined (__USE_STM32F4XX_SPD_GPIO__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_gpio.h"
#include "../ek_EcuA/ek_MCAL/ek_GPIOx_init.h"
#endif

#if defined (__USE_STM32F4XX_SPD_I2C__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_i2c.h"
#include "../ek_EcuA/ek_IoHwA/ek_I2C_init.h"
#endif

#if defined (__USE_STM32F4XX_SPD_RCC__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_rcc.h"
#endif

#if defined (__USE_STM32F4XX_SPD_SPI__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_spi.h"
#endif

#if defined (__USE_STM32F4XX_SPD_TIM__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_tim.h"
#include "../ek_EcuA/ek_IoHwA/ek_PWM_init.h"

#endif

#if defined (__USE_STM32F4XX_SPD_USART__) || defined (__USE_STM32F4XX_SPD_ALL_)
#include "stm32f4xx_usart.h"
#endif




//uint32_t Result_CAN; /* for return of the interrupt handling */


void IoHwA_Init(void);

uint32_t ek_getIFdistance();
uint32_t ek_getUSdistance();

MeasurementStatus ek_getDistanceMeasurementStatusUS();
MeasurementStatus ek_getDistanceMeasurementStatusIF();

#endif /* EK_MCAL_EK_MCAL_INIT_H_ */
