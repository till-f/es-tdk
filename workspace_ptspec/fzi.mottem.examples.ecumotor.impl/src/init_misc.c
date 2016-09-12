#include <mpc5643l.h>
#include <mpc5643l_extras.h>

#include "init_misc.h"

void _interrupt_table();
extern unsigned int IVOR0trap;
extern unsigned int IVOR1trap;
extern unsigned int IVOR2trap;
extern unsigned int IVOR3trap;
extern unsigned int IVOR4trap;
extern unsigned int IVOR5trap;
extern unsigned int IVOR6trap;
extern unsigned int IVOR7trap;
extern unsigned int IVOR8trap;
extern unsigned int IVOR9trap;
extern unsigned int IVOR10trap;
extern unsigned int IVOR11trap;
extern unsigned int IVOR12trap;
extern unsigned int IVOR13trap;
extern unsigned int IVOR14trap;
extern unsigned int IVOR15trap;
extern unsigned int IVOR32trap;
extern unsigned int IVOR33trap;
extern unsigned int IVOR34trap;

__attribute__((section(".boot"))) void clear_wdg()
{
    SWT.SR.R = 0x0000c520;  /* Write keys to clear soft lock bit */
    SWT.SR.R = 0x0000d928;
    SWT.CR.R = 0xFF00000A;  /* Clear watchdog enable (WEN) */
}

__attribute__((section(".boot"))) void init_interrupt_controller()
{
#ifdef __CORE0__
    INTC.BCR.B.VTES_PRC0 = 0;
    INTC.BCR.B.HVEN_PRC0 = 1;       /* HVEN_PRC0 = 1: Hardware Vector Mode */
                                    /* HVEN_PRC0 = 0: Software Vector Mode */

    INTC.IACKR_PRC0.R = (unsigned int)_interrupt_table;
#else
    INTC_1.BCR.B.VTES_PRC0 = 0;
    INTC_1.BCR.B.HVEN_PRC0 = 1;     /* HVEN_PRC0 = 1: Hardware Vector Mode */
                                    /* HVEN_PRC0 = 0: Software Vector Mode */

    INTC_1.IACKR_PRC0.R = (unsigned int)_interrupt_table;
#endif

    asm volatile ("mtspr   400, %0"::"r" (&IVOR0trap));
    asm volatile ("mtspr   401, %0"::"r" (&IVOR1trap));
    asm volatile ("mtspr   402, %0"::"r" (&IVOR2trap));
    asm volatile ("mtspr   403, %0"::"r" (&IVOR3trap));
    asm volatile ("mtspr   404, %0"::"r" (&IVOR4trap));
    asm volatile ("mtspr   405, %0"::"r" (&IVOR5trap));
    asm volatile ("mtspr   406, %0"::"r" (&IVOR6trap));
    asm volatile ("mtspr   407, %0"::"r" (&IVOR7trap));
    asm volatile ("mtspr   408, %0"::"r" (&IVOR8trap));
    asm volatile ("mtspr   409, %0"::"r" (&IVOR9trap));
    asm volatile ("mtspr   410, %0"::"r" (&IVOR10trap));
    asm volatile ("mtspr   411, %0"::"r" (&IVOR11trap));
    asm volatile ("mtspr   412, %0"::"r" (&IVOR12trap));
    asm volatile ("mtspr   413, %0"::"r" (&IVOR13trap));
    asm volatile ("mtspr   414, %0"::"r" (&IVOR14trap));
    asm volatile ("mtspr   415, %0"::"r" (&IVOR15trap));

    asm volatile ("mtspr   528, %0"::"r" (&IVOR32trap));
    asm volatile ("mtspr   529, %0"::"r" (&IVOR33trap));
    asm volatile ("mtspr   530, %0"::"r" (&IVOR34trap));
}
