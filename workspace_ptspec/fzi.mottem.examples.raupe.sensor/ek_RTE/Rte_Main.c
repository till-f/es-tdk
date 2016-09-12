
#include "Rte_Main.h"

Std_ReturnType Rte_Start(void){

	//Init Vfb
	Rte_Vfb_Init();

	//Init set alams
#ifdef TASKSLOT1_ACTIVE
	SetRelAlarm(TaskRunnableSlot1, TASKSLOT1_TICK_OFFSET, TASKSLOT1_TICK_CYCLE); 	//Actor
#endif
#ifdef TASKSLOT2_ACTIVE
	SetRelAlarm(TaskRunnableSlot2, TASKSLOT2_TICK_OFFSET, TASKSLOT2_TICK_CYCLE); 	//Sensor
#endif
#ifdef TASKSLOT3_ACTIVE
	SetRelAlarm(TaskRunnableSlot3, TASKSLOT3_TICK_OFFSET, TASKSLOT3_TICK_CYCLE);	//Controller
#endif
#ifdef TASKSLOT4_ACTIVE
	SetRelAlarm(TaskRunnableSlot4, TASKSLOT4_TICK_OFFSET, TASKSLOT4_TICK_CYCLE);
#endif
#ifdef TASKSLOT5_ACTIVE
	SetRelAlarm(TaskRunnableSlot5, TASKSLOT5_TICK_OFFSET, TASKSLOT5_TICK_CYCLE);
#endif

	//SetRelAlarm(TaskRunnable1, 10, 100); //US 65ms - ultrasonic response time


	return RTE_E_OK;
}

Std_ReturnType Rte_Stop(void){

	return RTE_E_OK;
}
