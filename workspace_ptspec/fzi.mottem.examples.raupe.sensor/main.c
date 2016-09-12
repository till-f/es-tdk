#include "ee.h"

#include "stm32f4xx.h"
#include "stm32f4_discovery.h"

#include "ek_ComA/ek_ComS.h"
#include "ISR.h"
#include "TASKs.h"

int main(void)
{
	/*
	 * Setup the microcontroller system.
	 * Initialize the Embedded Flash Interface and the PLL
	 */
	SystemInit();

	/*Update SystemCoreClock variable */
	SystemCoreClockUpdate();

	/*Initialize Erika related stuffs*/
	EE_system_init();

	/*Initialize systick */
	RCC_ClocksTypeDef clocks;
	RCC_GetClocksFreq(&clocks);
	EE_systick_set_period(MILLISECONDS_TO_TICKS(1, clocks.SYSCLK_Frequency));
	EE_systick_enable_int();
	EE_systick_start();


	/*Init Com Abstraction Layer*/
	Com_Init();

	/*Init ECU Abstraction Layer*/
	IoHwA_Init();

	/*Init Runtime Environment*/
	Rte_Start();

	for (;;);
}
