/*
 * Rte_Main.h
 *
 *  Created on: Jan 21, 2016
 *      Author: lore_
 */

#ifndef EK_RTE_RTE_MAIN_H_
#define EK_RTE_RTE_MAIN_H_

#include "Rte.h"
#include "ee.h"
#include "../TASKs.h"


#define NUMBER_OF_SLOTS	5


//START_RTE_ALARM
#define TASKSLOT1_ACTIVE
#define TASKSLOT1_TICK_OFFSET	110
#define TASKSLOT1_TICK_CYCLE	100

//#define TASKSLOT2_ACTIVE
#define TASKSLOT2_TICK_OFFSET	0
#define TASKSLOT2_TICK_CYCLE	0

//#define TASKSLOT3_ACTIVE
#define TASKSLOT3_TICK_OFFSET	0
#define TASKSLOT3_TICK_CYCLE	0

//#define TASKSLOT4_ACTIVE
#define TASKSLOT4_TICK_OFFSET	0
#define TASKSLOT4_TICK_CYCLE	0

//#define TASKSLOT5_ACTIVE
#define TASKSLOT5_TICK_OFFSET	0
#define TASKSLOT5_TICK_CYCLE	0
//STOP_RTE_ALARM


#ifdef __cplusplus
extern "C" {
#endif

Std_ReturnType Rte_Start(void);	//Init RTE
Std_ReturnType Rte_Stop(void);


#ifdef __cplusplus
} /* extern C */
#endif

#endif /* EK_RTE_RTE_MAIN_H_ */
