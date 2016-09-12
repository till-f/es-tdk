#include <mpc5643l.h>
#include <mpc5643l_extras.h>

#include "app.h"

#include "../shared.h"

uint32_t etimer00ISRCtr = 0; /* Counter for eTimer interrupt */
uint32_t etimer01ISRCtr = 0; /* Counter for eTimer interrupt */
uint32_t etimer02ISRCtr = 0; /* Counter for eTimer interrupt */

unsigned int hall_counter;
unsigned int counter[3];
unsigned int volatile hall_sensors[3];
int volatile sensorsint;
int volatile sector;
int volatile sector_old;
int sector_table[8];
double volatile pwm_dc;
double volatile pwm_dc_old;
int volatile pwm_ena[3][8][3];
int volatile drv_ena[3][8][3];
int direction_table[8][8];
int volatile direction;
double rpm_double;
float rpm;
int rpm_tar;
double k_p;
double k_i;
double volatile deviation_i;
float rpm_act_CAN;

int word_count[3];
int counter0_record[100];
int counter1_record[100];
int counter2_record[100];
int trivial_counter[20];

struct core0infotype* core0info;
struct core1infotype *core1info;

void initLEDs(void)
{
    LED3_CR = (PCR_OBE_MASK);
    LED4_CR = (PCR_OBE_MASK);
    LED3_DR = 1;
    LED4_DR = 1;
}

void initSWIRQs(void)
{
    INTC.PSR3.R = 0x01;   					      /* Software interrupt IRQ 3 */
}

void initETIMER(void)
{
    SIUL.PCR30.B.PA = 1;                          /* Set and configure Pin B[14], P11 as input pin 4 */
    SIUL.PCR30.B.OBE = 0;
    SIUL.PCR30.B.IBE = 1;

    SIUL.PSMI4_7.B.PADSEL7 = 2;


    ETIMER_0_CFG.ENBL.B.ENBL0 = 1;                /* Enable eTimer Module 0*/

    ETIMER_00.CTRL1.B.CNTMODE = 1;                /* count rising edge */
    ETIMER_00.CTRL1.B.PRISRC = 16 | 8 | 4 | 2 ;   /* select IPBusClock as primary source */
    ETIMER_00.CTRL1.B.ONCE = 0;                   /* count repeatedly */
    ETIMER_00.CTRL1.B.LENGTH = 0;                 /* count on after compare value is reached */
    ETIMER_00.CTRL1.B.DIR = 0;                    /* count up */
    ETIMER_00.CTRL1.B.SECSRC = 2;                 /* secondary source (=interrupt source): input pin 2 */
    ETIMER_00.INTDMA.B.ICF1IE = 1;                /* Interrupt enable for capture*/
    ETIMER_00.INTDMA.B.TOFIE = 1;                 /* Interrupt enable for timer overflow */
    ETIMER_00.CCCTRL.B.CPT1MODE = 3;              /* Capture 1 on every Edge */
    ETIMER_00.CCCTRL.B.ARM = 1;                   /* Arm Capture */
    ETIMER_00.CTRL3.B.ROC = 1;                    /* Reload timer on capture */
    ETIMER_00.FILT.B.FILT_PER = 0;                /* Filter secondary source */
    ETIMER_00.FILT.B.FILT_CNT = 0;                /* Filter secondary source */

    ETIMER_01.CTRL1.B.CNTMODE = 1;                /* count rising edge */
    ETIMER_01.CTRL1.B.PRISRC = 16 | 8 | 4 | 2 ;   /* select IPBusClock as primary source */
    ETIMER_01.CTRL1.B.ONCE = 0;                   /* count repeatedly */
    ETIMER_01.CTRL1.B.LENGTH = 0;                 /* count on after compare value is reached */
    ETIMER_01.CTRL1.B.DIR = 0;                    /* count up */
    ETIMER_01.CTRL1.B.SECSRC = 3;                 /* secondary source (=interrupt source): input pin 3  */
    ETIMER_01.INTDMA.B.ICF1IE = 1;                /* Interrupt enable for capture */
    ETIMER_01.CCCTRL.B.CPT1MODE = 3;              /* Capture 1 on every Edge */
    ETIMER_01.CCCTRL.B.ARM = 1;                   /* Arm Capture */
    ETIMER_01.CTRL3.B.ROC = 1;                    /* Reload timer on capture */
    ETIMER_01.FILT.B.FILT_PER = 0;                /* Filter secondary source */
    ETIMER_01.FILT.B.FILT_CNT = 0;                /* Filter secondary source */

    ETIMER_02.CTRL1.B.CNTMODE = 1;                /* count rising edge */
    ETIMER_02.CTRL1.B.PRISRC = 16 | 8 | 4 | 2 ;   /* select IPBusClock as primary source */
    ETIMER_02.CTRL1.B.ONCE = 0;                   /* count repeatedly */
    ETIMER_02.CTRL1.B.LENGTH = 0;                 /* count on after compare value is reached */
    ETIMER_02.CTRL1.B.DIR = 0;                    /* count up */
    ETIMER_02.CTRL1.B.SECSRC = 4;                 /* secondary source (=interrupt source): input pin 4 */
    ETIMER_02.INTDMA.B.ICF1IE = 1;                /* Interrupt enable for capture*/
    ETIMER_02.CCCTRL.B.CPT1MODE = 3;              /* Capture 1 on every Edge */
    ETIMER_02.CCCTRL.B.ARM = 1;                   /* Arm Capture */
    ETIMER_02.CTRL3.B.ROC = 1;                    /* Reload timer on capture */
    ETIMER_02.FILT.B.FILT_PER = 0;                /* Filter secondary source */
    ETIMER_02.FILT.B.FILT_CNT = 0;                /* Filter secondary source */

    INTC.PSR157.R = 0x05;                         /* Interrupt priorities for Timer Interrupts 0 to 2 */
    INTC.PSR158.R = 0x06;
    INTC.PSR159.R = 0x07;
}

void initHall(void)
{
   hall_sensors[0]= !(SIUL.GPDI0_3.B.PDI2);       /* read Hall Signals from the input Pad */
   hall_sensors[1]= !(SIUL.GPDI0_3.B.PDI3);       /* Negative Logic because of Open-Collector! */
   hall_sensors[2]= !(SIUL.GPDI28_31.B.PDI30);
}

void initPWM(void)
{                                                 /* Pin configuration: */
    SIUL.PCR11.B.PA = 2;                          /* Set Pin A[11], D11 as PWM_0_0 Output A */
    SIUL.PCR12.B.PA = 2;                          /* Set Pin A[12], A10 as PWM_0_2 Output A */
    SIUL.PCR42.B.PA = 3;                          /* Set Pin C[10], A15 as PWM_0_3 Output A */

    SIUL.PCR59.B.PA = 1;                          /* Set Pin D[11], R16 as PWM_0_0 Output B */
    SIUL.PCR100.B.PA = 1;                         /* Set Pin G[4], F17 as PWM_0_2 Output B */
    SIUL.PCR103.B.PA = 1;                         /* Set Pin G[7], P17 as PWM_0_3 Output B */

    FLEXPWM_0_SUB0.CTRL2.B.INDEP = 1;
    FLEXPWM_0_SUB0.CTRL1.B.LDFQ = 0;
    FLEXPWM_0_SUB0.CTRL1.B.LMOD = 1;
    FLEXPWM_0_SUB0.CTRL1.B.HALF = 1;
    FLEXPWM_0_SUB0.CTRL1.B.FULL = 1;
    FLEXPWM_0_SUB0.CTRL1.B.PRSC = 2;
    FLEXPWM_0_SUB0.CTRL2.B.FRCEN = 1;
    FLEXPWM_0_SUB0.VAL0.R = 50;
    FLEXPWM_0_SUB0.VAL1.R = 100;
    FLEXPWM_0_SUB0.VAL2.R = 0;
    FLEXPWM_0_SUB0.VAL3.R = 0;
    FLEXPWM_0_SUB0.VAL4.R = 0;
    FLEXPWM_0_SUB0.VAL5.R = 0;
    FLEXPWM_0_SUB0.TCTRL.B.OUT_TRIG_EN = 0;
    FLEXPWM_0_SUB0.DISMAP.B.DISA = 0;
    FLEXPWM_0_SUB0.DISMAP.B.DISB = 0;
    FLEXPWM_0_SUB0.INTEN.B.RIE = 0;

    FLEXPWM_0_SUB2.CTRL2.B.INDEP = 1;
    FLEXPWM_0_SUB2.CTRL1.B.LDFQ = 0;
    FLEXPWM_0_SUB2.CTRL1.B.LMOD = 1;
    FLEXPWM_0_SUB2.CTRL1.B.HALF = 1;
    FLEXPWM_0_SUB2.CTRL1.B.FULL = 1;
    FLEXPWM_0_SUB2.CTRL1.B.PRSC = 2;
    FLEXPWM_0_SUB2.CTRL2.B.FRCEN = 1;
    FLEXPWM_0_SUB2.CTRL2.B.FORCE_SEL = 1;
    FLEXPWM_0_SUB2.VAL0.R = 50;
    FLEXPWM_0_SUB2.VAL1.R = 100;
    FLEXPWM_0_SUB2.VAL2.R = 0;
    FLEXPWM_0_SUB2.VAL3.R = 0;
    FLEXPWM_0_SUB2.VAL4.R = 0;
    FLEXPWM_0_SUB2.VAL5.R = 0;
    FLEXPWM_0_SUB2.TCTRL.B.OUT_TRIG_EN = 0;
    FLEXPWM_0_SUB2.DISMAP.B.DISA = 0;
    FLEXPWM_0_SUB2.DISMAP.B.DISB = 0;
    FLEXPWM_0_SUB2.INTEN.B.RIE = 0;

    FLEXPWM_0_SUB3.CTRL2.B.INDEP = 1;
    FLEXPWM_0_SUB3.CTRL1.B.LDFQ = 0;
    FLEXPWM_0_SUB3.CTRL1.B.LMOD = 1;
    FLEXPWM_0_SUB3.CTRL1.B.HALF = 1;
    FLEXPWM_0_SUB3.CTRL1.B.FULL = 1;
    FLEXPWM_0_SUB3.CTRL1.B.PRSC = 2;
    FLEXPWM_0_SUB3.CTRL2.B.FRCEN = 1;
    FLEXPWM_0_SUB3.CTRL2.B.FORCE_SEL = 1;
    FLEXPWM_0_SUB3.VAL0.R = 50;
    FLEXPWM_0_SUB3.VAL1.R = 100;
    FLEXPWM_0_SUB3.VAL2.R = 0;
    FLEXPWM_0_SUB3.VAL3.R = 0;
    FLEXPWM_0_SUB3.VAL4.R = 0;
    FLEXPWM_0_SUB3.VAL5.R = 0;
    FLEXPWM_0_SUB3.TCTRL.B.OUT_TRIG_EN = 0;
    FLEXPWM_0_SUB3.DISMAP.B.DISA = 0;
    FLEXPWM_0_SUB3.DISMAP.B.DISB = 0;
    FLEXPWM_0_SUB3.INTEN.B.RIE = 0;

    FLEXPWM_0.OUTEN.B.PWMA_EN = 8 | 4 | 1;
    FLEXPWM_0.OUTEN.B.PWMB_EN = 8 | 4 | 1;

    FLEXPWM_0.MCTRL.B.LDOK = 8 | 4 | 1;
    FLEXPWM_0.MCTRL.B.RUN = 8 | 4 | 1;
}

void initTables(void)
{
  sector_table[0] = 0;
  sector_table[1] = 6;
  sector_table[2] = 4;
  sector_table[3] = 5;
  sector_table[4] = 2;
  sector_table[5] = 1;
  sector_table[6] = 3;
  sector_table[7] = 0;

  direction_table[1][2] = 1;
  direction_table[2][3] = 1;
  direction_table[3][4] = 1;
  direction_table[4][5] = 1;
  direction_table[5][6] = 1;
  direction_table[6][1] = 1;
  direction_table[1][6] = -1;
  direction_table[2][1] = -1;
  direction_table[3][2] = -1;
  direction_table[4][3] = -1;
  direction_table[5][4] = -1;
  direction_table[6][5] = -1;

  //pwm_dc > 0:
  pwm_ena[0][0][0] = 0; pwm_ena[1][0][0] = 0; pwm_ena[2][0][0] = 0;
  pwm_ena[0][1][0] = 0; pwm_ena[1][1][0] = 0; pwm_ena[2][1][0] = 1;
  pwm_ena[0][2][0] = 0; pwm_ena[1][2][0] = 1; pwm_ena[2][2][0] = 0;
  pwm_ena[0][3][0] = 0; pwm_ena[1][3][0] = 0; pwm_ena[2][3][0] = 1;
  pwm_ena[0][4][0] = 1; pwm_ena[1][4][0] = 0; pwm_ena[2][4][0] = 0;
  pwm_ena[0][5][0] = 1; pwm_ena[1][5][0] = 0; pwm_ena[2][5][0] = 0;
  pwm_ena[0][6][0] = 0; pwm_ena[1][6][0] = 1; pwm_ena[2][6][0] = 0;
  pwm_ena[0][7][0] = 0; pwm_ena[1][7][0] = 0; pwm_ena[2][7][0] = 0;

  //pwm_dc < 0:
  pwm_ena[0][0][1] = 0;  pwm_ena[1][0][1] = 0;  pwm_ena[2][0][1] = 0;
  pwm_ena[0][1][1] = 0;  pwm_ena[1][1][1] = -1; pwm_ena[2][1][1] = 0;
  pwm_ena[0][2][1] = -1; pwm_ena[1][2][1] = 0;  pwm_ena[2][2][1] = 0;
  pwm_ena[0][3][1] = -1; pwm_ena[1][3][1] = 0;  pwm_ena[2][3][1] = 0;
  pwm_ena[0][4][1] = 0;  pwm_ena[1][4][1] = 0;  pwm_ena[2][4][1] = -1;
  pwm_ena[0][5][1] = 0;  pwm_ena[1][5][1] = -1; pwm_ena[2][5][1] = 0;
  pwm_ena[0][6][1] = 0;  pwm_ena[1][6][1] = 0;  pwm_ena[2][6][1] = -1;
  pwm_ena[0][7][1] = 0;  pwm_ena[1][7][1] = 0;  pwm_ena[2][7][1] = 0;

  //pwm_dc > 0:
  drv_ena[0][0][0] = 0; drv_ena[1][0][0] = 0; drv_ena[2][0][0] = 0;
  drv_ena[0][1][0] = 0; drv_ena[1][1][0] = 1; drv_ena[2][1][0] = 0;
  drv_ena[0][2][0] = 1; drv_ena[1][2][0] = 0; drv_ena[2][2][0] = 0;
  drv_ena[0][3][0] = 1; drv_ena[1][3][0] = 0; drv_ena[2][3][0] = 0;
  drv_ena[0][4][0] = 0; drv_ena[1][4][0] = 0; drv_ena[2][4][0] = 1;
  drv_ena[0][5][0] = 0; drv_ena[1][5][0] = 1; drv_ena[2][5][0] = 0;
  drv_ena[0][6][0] = 0; drv_ena[1][6][0] = 0; drv_ena[2][6][0] = 1;
  drv_ena[0][7][0] = 0; drv_ena[1][7][0] = 0; drv_ena[2][7][0] = 0;

  //drv_dc < 0:
  drv_ena[0][0][1] = 0; drv_ena[1][0][1] = 0; drv_ena[2][0][1] = 0;
  drv_ena[0][1][1] = 0; drv_ena[1][1][1] = 0; drv_ena[2][1][1] = 1;
  drv_ena[0][2][1] = 0; drv_ena[1][2][1] = 1; drv_ena[2][2][1] = 0;
  drv_ena[0][3][1] = 0; drv_ena[1][3][1] = 0; drv_ena[2][3][1] = 1;
  drv_ena[0][4][1] = 1; drv_ena[1][4][1] = 0; drv_ena[2][4][1] = 0;
  drv_ena[0][5][1] = 1; drv_ena[1][5][1] = 0; drv_ena[2][5][1] = 0;
  drv_ena[0][6][1] = 0; drv_ena[1][6][1] = 1; drv_ena[2][6][1] = 0;
  drv_ena[0][7][1] = 0; drv_ena[1][7][1] = 0; drv_ena[2][7][1] = 0;
}


void enableIRQ(void)
{
    INTC.CPR_PRC0.B.PRI = 0;        /* Single Core: Lower INTC's current priority */
    __asm__ volatile(" wrteei 1");  /* Enable external interrupts */
}


void updatePWM()
{
   //pwm_dc=0.5; /*pwm dc override*/
  if (rpm_tar < 200 && rpm_tar > -200 && rpm_double < 300 && rpm_double > -300) {pwm_dc = 0;} //stop procedure

  FLEXPWM_0_SUB0.VAL3.R =  pwm_ena[0][sensorsint][(pwm_dc < 0)] * pwm_dc * 100;
  FLEXPWM_0_SUB2.VAL3.R =  pwm_ena[1][sensorsint][(pwm_dc < 0)] * pwm_dc * 100;
  FLEXPWM_0_SUB3.VAL3.R =  pwm_ena[2][sensorsint][(pwm_dc < 0)] * pwm_dc * 100;

  FLEXPWM_0_SUB0.VAL5.R =  pwm_ena[0][sensorsint][(pwm_dc < 0)] * pwm_dc * 100 + drv_ena[0][sensorsint][(pwm_dc < 0)] * 100;
  FLEXPWM_0_SUB2.VAL5.R =  pwm_ena[1][sensorsint][(pwm_dc < 0)] * pwm_dc * 100 + drv_ena[1][sensorsint][(pwm_dc < 0)] * 100;
  FLEXPWM_0_SUB3.VAL5.R =  pwm_ena[2][sensorsint][(pwm_dc < 0)] * pwm_dc * 100 + drv_ena[2][sensorsint][(pwm_dc < 0)] * 100;

  FLEXPWM_0.MCTRL.B.LDOK = 8 | 4 | 1;
  FLEXPWM_0_SUB0.CTRL2.B.FORCE = 1;
}

void update_sector(unsigned int h0, unsigned int h1, unsigned int h2)
{
	  sensorsint = h0 * 4 + h1 * 2 + h2;
	  sector_old = sector;
	  sector = sector_table[sensorsint];
}

void pi_control(int i)
{
  update_sector(hall_sensors[0], hall_sensors[1], hall_sensors[2]);

  direction =  direction_table[sector][sector_old];
  rpm_double = 60 / (8 * 0.000004 * counter[i]) * direction;
  rpm = (float)rpm_double;

  LED3_DR = (rpm_double > rpm_tar);
  LED4_DR = (rpm_double < rpm_tar);

  deviation_i = deviation_i + (rpm_tar - rpm_double);
  deviation_i = (deviation_i > - 1000000) ? deviation_i : -1000000;
  deviation_i = (deviation_i < 1000000) ? deviation_i : 1000000;

  pwm_dc = k_p * (rpm_tar - rpm_double) +  k_i * deviation_i;
  pwm_dc = (pwm_dc < 1) ? pwm_dc : 1;
  pwm_dc = (pwm_dc > -1 ) ? pwm_dc : -1;

}

void etimer00ISR()
{

	if(ETIMER_00.STS.B.TOF == 1)
	{
		initHall();
		counter[0] = 0xFFFF;
		deviation_i = 0;
		pi_control(0);
		updatePWM();
		ETIMER_00.STS.B.TOF = 1;
	}
	else
	{
		initHall();
		counter[0] = ETIMER_00.CAPT1.R;
		pi_control(0);
		updatePWM();
		ETIMER_00.STS.B.ICF1 = 1;

		//debug information:
		etimer00ISRCtr++;
		word_count[0] = ETIMER_00.CTRL3.B.C1FCNT;
		counter0_record[etimer00ISRCtr % 10] = counter[0];
	}
}

void etimer01ISR()
{
  initHall();
  counter[1] = ETIMER_01.CAPT1.R;
  pi_control(1);
  updatePWM();
  ETIMER_01.STS.B.ICF1 = 1;

  //debug information:
  etimer01ISRCtr++;
  word_count[1] = ETIMER_01.CTRL3.B.C1FCNT ;
  counter1_record[etimer01ISRCtr % 10] = counter[1];
}

void etimer02ISR()
{
  initHall();
  counter[2] = ETIMER_02.CAPT1.R;
  pi_control(2);
  updatePWM();
  ETIMER_02.STS.B.ICF1 = 1;

  //debug information:
  etimer02ISRCtr++;
  word_count[2] = ETIMER_02.CTRL3.B.C1FCNT;
  counter2_record[etimer02ISRCtr % 10] = counter[2];
}

/*
 * software ISR for data exchange from core 1
 */
void SwIrq3ISR()
{
	//get data from second core
	rpm_tar = core1info->rpm_tar;
	k_p = core1info->k_p;
	k_i = core1info->k_i;

	//write information to second core
	core0info->k_p = k_p;
	core0info->k_i = k_i;
	core0info->rpm_act = rpm_double;
	core0info->pwm_dc = pwm_dc;

	INTC.SSCIR3.R = 1;              /* Clear channel's flag */
}

void init_app()
{
    initSWIRQs();
    initLEDs();
    initETIMER();
    initHall();
    initPWM();
    initTables();
    enableIRQ();

    rpm_tar = 0;
    k_p = 0.0005;
    k_i = 0.000005;
    deviation_i = 0;
    rpm_double=0;
    rpm=0;

    core0info = (void*)0x50001000;
    core1info = (void*)0x50002000;
}

void run_app()
{
    /* --- nothing to do; remaining logic is interrupt driven --- */
    while(1)
    {
    }
}
