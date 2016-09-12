#include "ISR.h"

#include "ee_irq.h"
#include "ee.h"

#include "stm32f4xx.h"
#include "ek_ComA/ek_ComS.h"
#include "ek_ComA/ek_ComStack_Types.h"

uint32 can_rx_cntr = 0;

/*
 * SysTick ISR2
 */
ISR2(systick_handler)
{
	/* count the interrupts, waking up expired alarms */
	CounterTick(myCounter);
}


ISR2 (can_handler)
{
	uint32 cnt;
	uint32 msg_ID;
	uint32 msg_Value=0;
	CanRxMsg rxMsg;

	can_rx_cntr++;

	CAN_Receive(CANx, CAN_FIFO0, &rxMsg);

	msg_ID=rxMsg.Data[0];
	for(cnt=3; cnt<=rxMsg.DLC-1;cnt++)
	{
		msg_Value += rxMsg.Data[cnt];

		if(cnt<rxMsg.DLC-1) msg_Value = msg_Value<<8;
	}

	if(rxMsg.StdId == 0x1)
	{
		Com_ReceiveSignal(msg_ID,msg_Value);
	}
}
