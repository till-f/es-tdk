#include "../../ek_ComA/ek_ComHwA/ek_CAN_init.h"

void ek_CANx_Init()
{
	//------------------------------------------------------ Enable CAN clock
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_CAN1, ENABLE);
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_CAN2, ENABLE);

	//------------------------------------------------------ Enable GPIO clock
	RCC_AHB1PeriphClockCmd(CANx_GPIO_CLK, ENABLE);

	//------------------------------------------------------ Configure Pins
	GPIO_PinAFConfig(CANx_GPIO_BANK, CANx_PIN_RX_SRC, CANx_AF);
	GPIO_PinAFConfig(CANx_GPIO_BANK, CANx_PIN_TX_SRC, CANx_AF);

	GPIO_InitTypeDef RxTxPin_InitStruct;
	RxTxPin_InitStruct.GPIO_Pin   = CANx_PIN_RX | CANx_PIN_TX;
	RxTxPin_InitStruct.GPIO_Mode  = GPIO_Mode_AF;
	RxTxPin_InitStruct.GPIO_Speed = GPIO_Speed_50MHz;
	RxTxPin_InitStruct.GPIO_OType = GPIO_OType_PP;
	RxTxPin_InitStruct.GPIO_PuPd  = GPIO_PuPd_UP;
	GPIO_Init(CANx_GPIO_BANK, &RxTxPin_InitStruct);

	//------------------------------------------------------ Reset CAN configuration
	CAN_DeInit(CANx);

	//------------------------------------------------------ Init CAN
	CAN_InitTypeDef        CAN_InitStruct;

	// Operation Mode
	CAN_InitStruct.CAN_TTCM = DISABLE;
	CAN_InitStruct.CAN_ABOM = ENABLE;
	CAN_InitStruct.CAN_AWUM = DISABLE;
	CAN_InitStruct.CAN_NART = DISABLE;
	CAN_InitStruct.CAN_RFLM = DISABLE;
	CAN_InitStruct.CAN_TXFP = DISABLE;
	CAN_InitStruct.CAN_Mode = CAN_Mode_Normal;
	CAN_InitStruct.CAN_SJW  = CAN_SJW_1tq;

	// Bit Timing
	// calculated with http://www.bittiming.can-wiki.info

	// 50 kBit/s
	//CAN_InitStruct.CAN_Prescaler = 56;
	//CAN_InitStruct.CAN_BS1       = CAN_BS1_12tq;
	//CAN_InitStruct.CAN_BS2       = CAN_BS2_2tq;

	// 125 kBit/s
	//CAN_InitStruct.CAN_Prescaler = 21;
	//CAN_InitStruct.CAN_BS1       = CAN_BS1_11tq;
	//CAN_InitStruct.CAN_BS2       = CAN_BS2_4tq;

	// 250 kBit/s
	//CAN_InitStruct.CAN_Prescaler = 8;
	//CAN_InitStruct.CAN_BS1       = CAN_BS1_15tq;
	//CAN_InitStruct.CAN_BS2       = CAN_BS2_5tq;

	// 500 kBit/s
	CAN_InitStruct.CAN_Prescaler = 7;
	CAN_InitStruct.CAN_BS1       = CAN_BS1_7tq;
	CAN_InitStruct.CAN_BS2       = CAN_BS2_4tq;

	uint8_t result = CAN_Init(CANx, &CAN_InitStruct);

	//------------------------------------------------------ Configure Filter
	CAN_FilterInitTypeDef  CAN_FilterInitStruct;

	CAN_FilterInitStruct.CAN_FilterNumber         = 0;
	CAN_FilterInitStruct.CAN_FilterMode           = CAN_FilterMode_IdMask;
	CAN_FilterInitStruct.CAN_FilterScale          = CAN_FilterScale_32bit;
	CAN_FilterInitStruct.CAN_FilterIdHigh         = 0x0000;
	CAN_FilterInitStruct.CAN_FilterIdLow          = 0x0000;
	CAN_FilterInitStruct.CAN_FilterMaskIdHigh     = 0x0000;
	CAN_FilterInitStruct.CAN_FilterMaskIdLow      = 0x0000;
	CAN_FilterInitStruct.CAN_FilterFIFOAssignment = CAN_Filter_FIFO0;
	CAN_FilterInitStruct.CAN_FilterActivation     = ENABLE;
	CAN_FilterInit(&CAN_FilterInitStruct);

	CAN_ITConfig(CANx, CAN_IT_FMP0, ENABLE);
}

