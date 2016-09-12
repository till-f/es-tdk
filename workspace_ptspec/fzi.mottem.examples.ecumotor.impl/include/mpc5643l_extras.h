#ifndef MPC5643L_EXTRAS_H
#define MPC5643L_EXTRAS_H

/*******************************************************/
/* iSYSTEM ITMPC Leopard 257 BGA Board Specifics       */
/*******************************************************/

#define LED3_CR  SIUL.PCR1.R
#define LED4_CR  SIUL.PCR0.R
#define LED3_DR  SIUL.GPDO0_3.B.PDO1
#define LED4_DR  SIUL.GPDO0_3.B.PDO0


/***********************************/
/* Extra MPC543L Specifics         */
/***********************************/

// Masks for SIUL_PCR
#define PCR_OBE_MASK 0x0200                 // Output Enable
#define PCR_ODE_MASK 0x0020                 // Open Drain Enable
#define PCR_PA_ALTERNATE1_MASK 0x0400       // Alternate function 1
#define PCR_IBE_MASK 0x0100                 // Input Enable
#define PCR_HYS_MASK 0x0010                 // Hysteresis Enable

#define ISRAM_SIZE   (64*1024)				// size of RAM in DPM mode

#define INTC_EOIR_CORE0    (0xfff48018)
#define INTC_EOIR_CORE1    (0x8ff48018)


#endif
