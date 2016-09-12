#include <mpc5643l_extras.h>

.extern etimer00ISR
.extern etimer01ISR
.extern etimer02ISR
.extern SwIrq3ISR

#define INTR_PROLOG    bl interrupt_prolog
#define INTR_EPILOG    b  interrupt_epilog

/*
 * HANDLER: eTimer00IntHandler
 * ---------------------------------------------
 */
   .global eTimer00IntHandler
   .type eTimer00IntHandler, @function
eTimer00IntHandler:
   INTR_PROLOG
   bl  etimer00ISR
   INTR_EPILOG
.size eTimer00IntHandler, .-eTimer00IntHandler

/*
 * HANDLER: eTimer01IntHandler
 * ---------------------------------------------
 */
   .global eTimer01IntHandler
   .type eTimer01IntHandler, @function
eTimer01IntHandler:
   INTR_PROLOG
   bl etimer01ISR
   INTR_EPILOG
.size eTimer01IntHandler, .-eTimer01IntHandler

/*
 * HANDLER: eTimer02IntHandler
 * ---------------------------------------------
 */
   .global eTimer02IntHandler
   .type eTimer02IntHandler, @function
eTimer02IntHandler:
   INTR_PROLOG
   bl etimer02ISR
   INTR_EPILOG
.size eTimer02IntHandler, .-eTimer02IntHandler

/*
 * HANDLER: SwIrq3Handler
 * ---------------------------------------------
 */
   .global SwIrq3Handler
   .type SwIrq3Handler, @function
SwIrq3Handler:
   INTR_PROLOG
   bl SwIrq3ISR
   INTR_EPILOG
.size SwIrq3Handler, .-SwIrq3Handler

/*
 * HANDLER PROLOG
 * ---------------------------------------------
 */
	.type	interrupt_prolog, @function
interrupt_prolog:
    stwu    r1, -0x50 (r1)   	# Create stack frame and store back chain
    stw     r3,  0x28 (r1)   	# Store a working register
    mfsrr0  r3               	# Store SRR0:1 (must be done before enabling EE)
    stw     r3,  0x0C (r1)
    mfsrr1  r3
    stw     r3,  0x10 (r1)
    wrteei  1                 	# Set MSR[EE]=1       (must wait a couple clocks after reading IACKR)
    stw     r12, 0x4C (r1)    	# Store rest of gprs
    stw     r11, 0x48 (r1)
    stw     r10, 0x44 (r1)
    stw     r9,  0x40 (r1)
    stw     r8,  0x3C (r1)
    stw     r7,  0x38 (r1)
    stw     r6,  0x34 (r1)
    stw     r5,  0x30 (r1)
    stw     r4,  0x2C (r1)
    stw     r0,  0x24 (r1)
    mfcr    r3                 	# Store CR
    stw     r3,  0x20 (r1)
    mfxer   r3                 	# Store XER
    stw     r3,  0x1C (r1)
    mfctr   r3                 	# Store CTR
    stw     r3,  0x18 (r1)
    mflr    r3
    stw     r3,  0x14 (r1)     	# Store LR
    blr
.size	interrupt_prolog, .-interrupt_prolog


/*
 * HANDLER EPILOG
 * ---------------------------------------------
 */
	.type	interrupt_epilog, @function
interrupt_epilog:
    lwz     r3,  0x14 (r1)   	       # Restore LR
    mtlr    r3
    lwz     r3,  0x18 (r1)   	       # Restore CTR
    mtctr   r3
    lwz     r3,  0x1C (r1)   	       # Restore XER
    mtxer   r3
    lwz     r3,  0x20 (r1)   	       # Restore CR
    mtcrf   0xff, r3
    lwz     r0,  0x24 (r1)   	       # Restore other gprs except working registers
    lwz     r5,  0x30 (r1)
    lwz     r6,  0x34 (r1)
    lwz     r7,  0x38 (r1)
    lwz     r8,  0x3C (r1)
    lwz     r9,  0x40 (r1)
    lwz     r10, 0x44 (r1)
    lwz     r11, 0x48 (r1)
    lwz     r12, 0x4C (r1)
    mbar    0                 	       # Ensure store to clear flag bit has completed
    lis     r3, INTC_EOIR_CORE0@ha     # MPC551x: Load upper half of proc'r 0 INTC_EOIR addr. to r3
    li      r4, 0
    wrteei  0                 	       # Disable interrupts for rest of handler
                                       # Use one of the following lines:
    stw     r4, INTC_EOIR_CORE0@l(r3)  # MPC551x: Write 0 to proc'r 0 INTC_EOIR
    lwz     r3,  0x0C (r1)             # Restore SRR0
    mtsrr0  r3
    lwz     r3,  0x10 (r1)             # Restore SRR1
    mtsrr1  r3
    lwz     r4,  0x2C (r1)             # Restore working registers
    lwz     r3,  0x28 (r1)
    addi    r1, r1, 0x50               # Delete stack frame
    rfi                                # End of Interrupt
.size	interrupt_epilog, .-interrupt_epilog
