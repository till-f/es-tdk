/*
 * Rte_Vfb.c
 *
 *  Created on: Feb 9, 2016
 *      Author: lore_
 */

#include "Rte_Vfb.h"


uint32 Intra_Com_Channels[Intra_SR_Com_Channels];
uint32 Inter_Com_Channels[Inter_SR_Com_Channels];
uint32 status_of_value[Intra_SR_Com_Channels];
uint32 temp_var,temp_IDvar;


void Rte_Vfb_Init();
void Rte_Vfb_write(uint32 valueID, uint32_t value);
uint32 Rte_Vfb_read(uint32 valueID);
uint32 Rte_Status_Value(uint32 value);
//uint32 Rte_Vfb_call(uint32 operationID, uint32 value);
uint32 Rte_Vfb_update_extern_Value(uint32 valueID, uint32 value);


void Rte_Vfb_Init(){

	uint32 cnt=0;

	for(cnt=0; cnt<Intra_SR_Com_Channels;cnt++){
		Intra_Com_Channels[cnt]=0;
		status_of_value[cnt]=0;
	}

	for(cnt=0; cnt<Inter_SR_Com_Channels;cnt++){
		Inter_Com_Channels[cnt]=0;
	}

}

void Rte_Vfb_write(uint32 valueID, uint32 value){

	if((valueID)<0xF0){
		//handle intraValue
		Intra_Com_Channels[(valueID&0xF)-1]=value;

		status_of_value[(valueID&0xF)-1]=VALUE_NEW;
	}else{
		//handle interValue
		Inter_Com_Channels[(valueID)-1]=value;
		Com_SendSignal(valueID,value);
	}
}

uint32 Rte_Vfb_read(uint32 valueID){
	uint32 temp_var=0;

	if((valueID)<0xF0){
		//handle intraValue
		status_of_value[(valueID&&0xF)-1]=VALUE_OLD;
		return Intra_Com_Channels[(valueID)-1];

	}else{
		//handle interValue
		temp_var = Inter_Com_Channels[(valueID&0xF)-1];
		return temp_var;
	}

}
uint32 Rte_Vfb_update_extern_Value(uint32 valueID, uint32 value){

	temp_var=Inter_SR_Com_Channels;
	temp_IDvar=valueID&0xF;
	if(temp_IDvar>temp_var){
		return E_NOT_OK;
	}else{
		Inter_Com_Channels[(valueID&&0xF)-1]=value;
		return E_OK;
	}
}

uint32 Rte_Status_Value(uint32 value){

	return status_of_value[(value)-1];
}
