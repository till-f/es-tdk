#include <mpc5643l.h>
#include <mpc5643l_extras.h>

#include "app.h"

#include "../shared.h"

uint32_t Pit0Ctr = 0; /* Counter for PIT 0*/

float rpm_act;
float pwm_dc;
int rpm_tar_CAN;
int rpm_tar;
float k_p_CAN;
float k_i_CAN;

struct core0infotype* core0info;
struct core1infotype *core1info;

void initPIT()
{
    PIT.PITMCR.R = 0x00000001;       	/* Enable PIT */
    PIT.TIMER0_LDVAL.R = 80000*25;   	/* 25 ms */
    PIT.TIMER0_TCTRL.R = 0x000000003; 	/* Enable PIT0 interrupt & start PIT counting */
    INTC_1.PSR59.R = 0x03;              /* PIT 0 interrupt vector with priority */
}

void initCAN()
{
	//pin configuration
	SIUL.PCR16.B.PA = 1;        //TX pin configuration: Pin B[0], B15
	SIUL.PCR16.B.ODE = 0;

	SIUL.PSMI32_35.B.PADSEL33 = 1;  //RX pin configuration: Pin B[1], C14
	SIUL.PCR17.B.IBE = 1;

	FLEXCAN_0.MCR.B.HALT = 1;		//put module into freeze mode to get write access to certain registers

	//Module configuration register
	FLEXCAN_0.MCR.B.MDIS = 0;		//enable module
	FLEXCAN_0.MCR.B.FRZ = 1;		//enable freeze mode when cpu is halted
	FLEXCAN_0.MCR.B.FEN = 0;		//disable FIFO, normal message buffer operation
	FLEXCAN_0.MCR.B.HALT = 1;		//put module into freeze mode
	FLEXCAN_0.MCR.B.WAK_MSK = 0;	//disable wake up interrupt
	FLEXCAN_0.MCR.B.SUPV = 1;		//enable supervisor mode (=standard)
	FLEXCAN_0.MCR.B.SLF_WAK = 0;	//disable self wake up mode
	FLEXCAN_0.MCR.B.WRN_EN = 0;		//disable warning interrupt
	FLEXCAN_0.MCR.B.SRX_DIS = 0;	//enable self-reception for test mode
	FLEXCAN_0.MCR.B.BCC = 1;		//enable individual Rx masking and queue feature
	FLEXCAN_0.MCR.B.LPRIO_EN = 1;	//enable local priority
	FLEXCAN_0.MCR.B.AEN = 1;		//enable abort (not sure if this is correct...)
	FLEXCAN_0.MCR.B.IDAM = 0;		//accept one full ID per filter element
	FLEXCAN_0.MCR.B.MAXMB = 7;		//set maximum number of message buffers to 8

	//Control register
	FLEXCAN_0.CTRL.B.CLK_SRC = 1;	//select bus clock (80 MHz)
	FLEXCAN_0.CTRL.B.PRESDIV = 3;	//time quantum: 20 MHz -> 50 ns; 20 time quanta per bit
	FLEXCAN_0.CTRL.B.PROPSEG = 7;	//segment values from http://www.bittiming.can-wiki.info/
	FLEXCAN_0.CTRL.B.PSEG1 = 7;		//segment values from http://www.bittiming.can-wiki.info/
	FLEXCAN_0.CTRL.B.PSEG2 = 2;		//segment values from http://www.bittiming.can-wiki.info/
	FLEXCAN_0.CTRL.B.RJW = 1;		//resync jump width = 2 time quanta
	FLEXCAN_0.CTRL.B.BOFFMSK = 0;	//disable bus off interrupt
	FLEXCAN_0.CTRL.B.ERRMSK = 0;	//disable error interrupt
	FLEXCAN_0.CTRL.B.LPB = 0;		//disable loop back mode for test operation
	FLEXCAN_0.CTRL.B.TWRN_MSK = 0;	//disable Tx warn interrupt
	FLEXCAN_0.CTRL.B.RWRN_MSK = 0;	//disable Rx warn interrupt
	FLEXCAN_0.CTRL.B.SMP = 0;		//use regular sampling mode with one sample per bit
	FLEXCAN_0.CTRL.B.BOFFREC = 0;	//enable Bus Off Recovery
	FLEXCAN_0.CTRL.B.TSYN = 1;		//enable timer sync mode
	FLEXCAN_0.CTRL.B.LBUF = 0;		//buffer with highest priority is transmitted first
	FLEXCAN_0.CTRL.B.LOM = 0;		//disable listen-only mode

	//interrupt masks 1 register
	FLEXCAN_0.IMASK1.B.BUF00M = 1; 	//enable interrupts for message buffer 0 (recieving buffer)
	FLEXCAN_0.IMASK1.B.BUF01M = 1; 	//enable interrupts for message buffer 1 (recieving buffer)
	FLEXCAN_0.IMASK1.B.BUF02M = 1; 	//enable interrupts for message buffer 2 (recieving buffer)
	FLEXCAN_0.IMASK1.B.BUF03M = 1; 	//enable interrupts for message buffer 3 (recieving buffer)
	FLEXCAN_0.IMASK1.B.BUF04M = 0; 	//disable interrupts for message buffer 4 (sending buffer)
	FLEXCAN_0.IMASK1.B.BUF05M = 0; 	//disable interrupts for message buffer 5 (sending buffer)
	FLEXCAN_0.IMASK1.B.BUF06M = 0; 	//disable interrupts for message buffer 6 (sending buffer)
	FLEXCAN_0.IMASK1.B.BUF07M = 0; 	//disable interrupts for message buffer 7 (sending buffer)

	//initialize receiving message buffer
	FLEXCAN_0.MB0_CS.B.CODE = 0b0000;	//set message buffer inactive
	FLEXCAN_0.MB0_IF.B.ID_EXTENDED_STANDARD = 0;	//set frame id
	FLEXCAN_0.MB0_CS.B.IDE = 0;             //use standard ID length
	FLEXCAN_0.MB0_CS.B.CODE = 0b0100;	//set message buffer active

	FLEXCAN_0.MB1_CS.B.CODE = 0b0000;	//set message buffer inactive
	FLEXCAN_0.MB1_IF.B.ID_EXTENDED_STANDARD = 1;	//set frame id
	FLEXCAN_0.MB1_CS.B.IDE = 0;             //use standard ID length
	FLEXCAN_0.MB1_CS.B.CODE = 0b0100;	//set message buffer active

	FLEXCAN_0.MB2_CS.B.CODE = 0b0000;	//set message buffer inactive
	FLEXCAN_0.MB2_IF.B.ID_EXTENDED_STANDARD = 2;	//set frame id
	FLEXCAN_0.MB2_CS.B.IDE = 0;             //use standard ID length
	FLEXCAN_0.MB2_CS.B.CODE = 0b0100;	//set message buffer active

	FLEXCAN_0.MB3_CS.B.CODE = 0b0000;	//set message buffer inactive
	FLEXCAN_0.MB3_IF.B.ID_EXTENDED_STANDARD = 3;	//set frame id
	FLEXCAN_0.MB3_CS.B.IDE = 0;             //use standard ID length
	FLEXCAN_0.MB3_CS.B.CODE = 0b0100;	//set message buffer active

	//initialize sending message buffers
	FLEXCAN_0.MB4_CS.B.CODE = 0b1000;
	FLEXCAN_0.MB5_CS.B.CODE = 0b1000;
	FLEXCAN_0.MB6_CS.B.CODE = 0b1000;
	FLEXCAN_0.MB7_CS.B.CODE = 0b1000;

	// set interrupt priority for received messages
	INTC_1.PSR68.R = 0x08;			//set interrupt priority for message buffers 0-3

	FLEXCAN_0.MCR.B.HALT = 0; 		//wake module from freeze mode, start operation
}

void enableIRQ()
{
    INTC_1.CPR_PRC0.B.PRI = 0;        /* Single Core: Lower INTC_1's current priority */
    __asm__ volatile(" wrteei 1");  /* Enable external interrupts */
}

void CANreceive0()
{
	long controlstatus;
	int timer, data1;

	controlstatus = FLEXCAN_0.MB0_CS.R;		//read control and status word and lock buffer
	data1 = FLEXCAN_0.MD0_DF03.R;          	//read data fields
	timer = FLEXCAN_0.TIMER.B.TIMER;		//read timer and unlock buffer

	rpm_tar_CAN = *((int*)&data1);			//convert to float
	rpm_tar_CAN = ((rpm_tar_CAN < 300) && (rpm_tar_CAN > -300)) ? 0 : rpm_tar_CAN; //accept no target values smaller than 300rpm
}

void CANreceive1()
{
	long controlstatus;
	int timer, data1, data2;

	controlstatus = FLEXCAN_0.MB1_CS.R;		//read control and status word and lock buffer
	data1 = FLEXCAN_0.MD1_DF03.R;          	//read data fields
	data2 = FLEXCAN_0.MD1_DF47.R;
	timer = FLEXCAN_0.TIMER.B.TIMER;		//read timer and unlock buffer

	//convert to float
	k_p_CAN = *((float*)&data1);
	k_i_CAN = *((float*)&data2);
}

void CANreceive2()
{

}

void CANreceive3()
{

}

void CANtransmit4(float data1, float data2)
{
	FLEXCAN_0.IFLAG1.B.BUF04I = 1;          //clear interrupt flag to unlock buffer

	//transmission frame settings
	FLEXCAN_0.MB4_CS.B.CODE = 0b1001;				//set message buffer inactive
	FLEXCAN_0.MB4_CS.B.LENGTH = 8;				//set data length in bytes
	FLEXCAN_0.MB4_CS.B.IDE = 0;             		//use standard ID length
	FLEXCAN_0.MB4_CS.B.RTR = 0;             		//send data, not remote
	FLEXCAN_0.MB4_CS.B.SRR = 0;             		//Substitute Remote Request, only used in extended format
	FLEXCAN_0.MB4_IF.B.PRIO = 1;					//set Tx priority
	FLEXCAN_0.MB4_IF.B.ID_EXTENDED_STANDARD = 4;	//set frame id

	//convert transmission data to int format and write
	FLEXCAN_0.MD4_DF03.R = *((int*)&data1);					//write data1 to buffer
	FLEXCAN_0.MD4_DF47.R = *((int*)&data2);					//write data2 to buffer

	//start transmission
	FLEXCAN_0.MB4_CS.B.CODE = 0b1100;				//transmit data frame once
}

void CANtransmit5(int data1, int data2)
{
	FLEXCAN_0.IFLAG1.B.BUF05I = 1;          //clear interrupt flag to unlock buffer

	//transmission frame settings
	FLEXCAN_0.MB5_CS.B.CODE = 0b1001;				//set message buffer inactive
	FLEXCAN_0.MB5_CS.B.LENGTH = 8;				//set data length in bytes
	FLEXCAN_0.MB5_CS.B.IDE = 0;             		//use standard ID length
	FLEXCAN_0.MB5_CS.B.RTR = 0;             		//send data, not remote
	FLEXCAN_0.MB5_CS.B.SRR = 0;             		//Substitute Remote Request, only used in extended format
	FLEXCAN_0.MB5_IF.B.PRIO = 2;					//set Tx priority
	FLEXCAN_0.MB5_IF.B.ID_EXTENDED_STANDARD = 5;	//set frame id

	//convert transmission data to int format and write
	FLEXCAN_0.MD5_DF03.R = data1;					//write data1 to buffer
	FLEXCAN_0.MD5_DF47.R = data2;					//write data2 to buffer

	//start transmission
	FLEXCAN_0.MB5_CS.B.CODE = 0b1100;				//transmit data frame once
}

void CANtransmit6(float data1, float data2)
{
	FLEXCAN_0.IFLAG1.B.BUF06I = 1;          //clear interrupt flag to unlock buffer

	//transmission frame settings
	FLEXCAN_0.MB6_CS.B.CODE = 0b1001;				//set message buffer inactive
	FLEXCAN_0.MB6_CS.B.LENGTH = 8;				//set data length in bytes
	FLEXCAN_0.MB6_CS.B.IDE = 0;             		//use standard ID length
	FLEXCAN_0.MB6_CS.B.RTR = 0;             		//send data, not remote
	FLEXCAN_0.MB6_CS.B.SRR = 0;             		//Substitute Remote Request, only used in extended format
	FLEXCAN_0.MB6_IF.B.PRIO = 1;					//set Tx priority
	FLEXCAN_0.MB6_IF.B.ID_EXTENDED_STANDARD = 6;	//set frame id

	//convert transmission data to int format and write
	FLEXCAN_0.MD6_DF03.R = *((int*)&data1);					//write data1 to buffer
	FLEXCAN_0.MD6_DF47.R = *((int*)&data2);					//write data2 to buffer

	//start transmission
	FLEXCAN_0.MB6_CS.B.CODE = 0b1100;				//transmit data frame once
}

void Can0ISR()
{
	if (FLEXCAN_0.IFLAG1.B.BUF00I == 1)
	{
		CANreceive0();					//receive CAN message 0
		FLEXCAN_0.IFLAG1.B.BUF00I = 1; 	//reset interrupt flag message buffer 1
	}

	if (FLEXCAN_0.IFLAG1.B.BUF01I == 1)
	{
		CANreceive1();
		FLEXCAN_0.IFLAG1.B.BUF01I = 1;
	}

	if (FLEXCAN_0.IFLAG1.B.BUF02I == 1)
	{
		CANreceive2();
		FLEXCAN_0.IFLAG1.B.BUF02I = 1;
	}

	if (FLEXCAN_0.IFLAG1.B.BUF03I == 1)
	{
		CANreceive3();
		FLEXCAN_0.IFLAG1.B.BUF03I = 1;
	}
}

void Pit0ISR()
{
    Pit0Ctr++;                      /* Increment interrupt counter */

    if ((Pit0Ctr % 2) == 0) {CANtransmit4((float)core0info->rpm_act, (float)core0info->pwm_dc);} 	//transmit every 100ms
	  if ((Pit0Ctr % 2) == 0) {CANtransmit5(rpm_tar_CAN, rpm_tar);} 									//transmit every 100ms
		if ((Pit0Ctr % 2) == 0) {CANtransmit6((float)core0info->k_p, (float)core0info->k_i);}

	  if (rpm_tar_CAN != rpm_tar)		//ramp function
	  {
  		  if ( (rpm_tar_CAN < rpm_tar) && ((rpm_tar - rpm_tar_CAN) > 300) )
  		{
  			rpm_tar -= 300;
  		}
  		else if ( (rpm_tar_CAN > rpm_tar) && ((rpm_tar_CAN - rpm_tar) > 300) )
  		{
  			rpm_tar += 300;
  		}
  		else
  		{
  			rpm_tar = rpm_tar_CAN;
  		}

  		core1info->rpm_tar = rpm_tar;
	}

	core1info->k_p = k_p_CAN;
	core1info->k_i = k_i_CAN;
	INTC.SSCIR3.R = 2; 				// Set software interrupt on core 0
    PIT.TIMER0_TFLG.B.TIF = 1;      // MPC56xxP/B/S: CLear PIT 0 flag by writing 1 */
}

void init_app()
{
    initPIT();
    enableIRQ();
    initCAN();

	core0info = (void*)0x50001000;
	core1info = (void*)0x50002000;
	rpm_tar_CAN = 0;
	rpm_tar = 0;

	core1info->rpm_tar = 0;
	k_p_CAN = 0.001;
	k_i_CAN = 0.00001;
}

void run_app()
{
    /* --- nothing to do; remaining logic is interrupt driven --- */
    while(1)
    {
    }
}
