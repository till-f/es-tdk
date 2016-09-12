

#ifndef TASKS_H_
#define TASKS_H_

#include "ee.h"
#include "stm32f4xx.h"
#include "stm32f4_discovery.h"

void empty_FUNC();


//START_RTE_TASK_SLOTS
#define TASK_SLOT1_FUNCTION 	runnableSWC_Sensor
#define TASK_SLOT2_FUNCTION		empty_FUNC
#define TASK_SLOT3_FUNCTION		empty_FUNC
#define TASK_SLOT4_FUNCTION		empty_FUNC
#define TASK_SLOT5_FUNCTION		empty_FUNC
//END_RTE_TASK_SLOTS

#endif

