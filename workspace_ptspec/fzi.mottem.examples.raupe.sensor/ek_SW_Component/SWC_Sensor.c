/*
 * SWC_A.c
 *
 *  Created on: Feb 10, 2016
 *      Author: lore_
 */

#include "../stm32f4_discovery_helpers.h"

#include "SWC_Sensor.h"

extern void runnableSWC_Sensor(void);

uint32 distance_Measurement=0xFFFF;
uint32 previous_distance_Measurement=0;

void runnableSWC_Sensor(void){
	//do stuff

	STM_EVAL_LEDToggle( LEDGREEN );

	if(distance_Measurement==0xFFFF){
		//first measurement
		distance_Measurement = Rte_S1Call_getDistance();
		previous_distance_Measurement=distance_Measurement;
		//Rte_Write_Pport_Distance(previous_distance_Measurement);
		//don't send first measurement
	}else{
		// >1 measurements

		distance_Measurement = Rte_S1Call_getDistance();
		previous_distance_Measurement=distance_Measurement;
		Rte_S1Write_Distance(previous_distance_Measurement);

	}
}
