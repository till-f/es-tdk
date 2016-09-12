/*
 * ek_sw_struct.h
 *
 *  Created on: Jan 12, 2016
 *      Author: lore_
 */

#ifndef EK_ECUA_ADD_LIB_EK_SW_STRUCT_H_
#define EK_ECUA_ADD_LIB_EK_SW_STRUCT_H_

#include "stm32f4xx.h"

#define real_t float

uint32_t multiplier;
void Delay_Init(void);
void DelayMicroS(uint32_t micros);

#define roundfloat(x) ((x)>=0?(long)((x)+0.5):(long)((x)-0.5))

// Implemented in the example header files. Initialises the filter.
void filter_init();
// Implemented in the example header files. Runs one filter (cycle) calculation.
real_t filter(real_t new_value);


#endif /* EK_ECUA_ADD_LIB_EK_SW_STRUCT_H_ */
