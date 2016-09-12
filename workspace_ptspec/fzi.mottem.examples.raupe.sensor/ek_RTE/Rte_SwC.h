
#ifndef Rte_SWC_A_h
#define Rte_SWC_A_h

#include "Rte_Type.h"
#include "Rte_Cfg.h"
#include "../ek_EcuA/IoHwA_API.h"


//START_VFB_SR_CON
#define Rte_S1Write_Distance(value)	Rte_Vfb_write(VFB_DISTANCE_VALUE, value)
//END_VFB_SR_CON


//START_VFB_CS_CON
#define Rte_S1Call_getDistance	ECU_GET_DISTANCE
//END_VFB_CS_CON


#endif
