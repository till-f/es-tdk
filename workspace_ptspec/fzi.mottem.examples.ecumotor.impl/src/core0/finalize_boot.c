#include <mpc5643l.h>
#include <mpc5643l_extras.h>

#include "finalize_boot.h"

void init_modes_and_clocks()
{
    RGM.FES.R = 0xFFFF;               /* Clear any fault status */
    ME.IS.R = 0x0000000F;

    CGM.OCDS_SC.R = 0x02;             /* Set and enable output clock */
    CGM.OC_EN.R = 0x1;

    ME.MER.R = 0x000005FF;            /* Enable DRUN, RUN0, SAFE, RESET modes */

    ME.DRUN_MC.R = 0x001F0032;

    ME.RUN_PC0.R = 0xFE; 	          /* Peri. Cfg. for RUN0, DRUN, SAFE modes */
    ME.LP_PC0.R = 0x500;              /* Peri. Cfg. for Low Power */

    CGM.AC0_SC.R = 0;
    CGM.AC1_SC.R = 0;
    CGM.AC2_SC.R = 0;
    CGM.AC0_DC0.R = 0x80;
    CGM.AC0_DC1.R = 0x80;
    CGM.AC1_DC0.R = 0x80;
    CGM.AC2_DC0.R = 0x80;

    CMU_0.CSR.R = 0x6;
    CMU_0.LFREFR_A.R = 0x1;
    CMU_0.HFREFR_A.R = 0xFFE;

    ME.MCTL.R = 0x30005AF0;           /* Enter DRUN Mode & Key */
    ME.MCTL.R = 0x3000A50F;           /* Enter DRUN Mode & Inverted Key */
    while (ME.GS.B.S_MTRANS) {}       /* Wait for mode transition to complete */

    CGM.AC3_SC.B.SELCTL = 0x1;        /* Set PLL0 ref. clock */
    CGM.AC4_SC.B.SELCTL = 0x1;        /* Set PLL1 ref. clock */

    FMPLL_0.CR.R = 0x11280041;        /* 40 MHz xtal: Set PLL0 to 80 MHz */
    ME.DRUN_MC.R = 0x001F00F4;

    ME.MCTL.R = 0x30005AF0;           /* Enter DRUN Mode & Key */
    ME.MCTL.R = 0x3000A50F;           /* Enter DRUN Mode & Inverted Key */
    while (ME.GS.B.S_MTRANS) {}       /* Wait for mode transition to complete */

    SSCM.ERROR.R = 0x2;
}

void start_core1()
{
    SSCM.DPMBOOT.B.P2BOOT = 0x00001000; // byte address: 0x00004000
    SSCM.DPMBOOT.B.DVLE = 0;
    SSCM.DPMKEY.B.KEY = 0x5AF0;
    SSCM.DPMKEY.B.KEY = 0xA50F;
}
