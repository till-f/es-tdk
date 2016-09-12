#ifndef Rte_SWC_A_h
#define Rte_SWC_A_h

#include "Rte_Type.h"
#include "Rte_Cfg.h"
#include "../ek_EcuA/IoHwA_API.h"

//START_VFB_SR_CON
#define Rte_S2Read_Distance()	        Rte_Vfb_read(VFB_DISTANCE_VALUE)
#define Rte_S2Write_SpeedLeft(value)	Rte_Vfb_write(VFB_SPEEDLEFT_VALUE, value)
#define Rte_S2Write_SpeedRight(value)	Rte_Vfb_write(VFB_SPEEDRIGHT_VALUE, value)
#define Rte_S1Read_SpeedLeft()	        Rte_Vfb_read(VFB_SPEEDLEFT_VALUE)
#define Rte_S1Read_SpeedLeftRight()	    Rte_Vfb_read(VFB_SPEEDRIGHT_VALUE)
//END_VFB_SR_CON

//START_VFB_CS_CON
#define HW_SET_SPEED_LEFT(VALUE)  ECU_Set_MotorSpeed(1, GPIO_Pin_8, VALUE)
#define HW_SET_SPEED_RIGHT(VALUE) ECU_Set_MotorSpeed(2, GPIO_Pin_9, VALUE)
//END_VFB_CS_CON

#endif
