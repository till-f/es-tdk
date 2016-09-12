#include "../stm32f4_discovery_helpers.h"

#include "IoHwA.h"
#include "IoHwA_API.h"

#include <math.h>


//private constants
float const a_curv=68;
float const b_curv=-45.84;
float const c_curv=9.03;

//private variables
float y_curv=0;

float SpannungADC = 0x0;
float SpannungVolt = 0x0;

MeasurementStatus status_DistanceSensorIF =0;
MeasurementStatus status_DistanceSensorUS =0;

uint32_t pwm_DutyCycle_valueIF;
uint32_t pwm_DutyCycle_valueUS;
uint32_t us_distance;
uint32_t ir_distance;

//private functions
uint32_t get_exact_IFdistance(uint32_t voltage);
void ek_setDistanceMeasurementStatusIF(MeasurementStatus status);
void ek_setDistanceMeasurementStatusUS(MeasurementStatus status);
void ek_sharpIF(void);
void ek_srf02_US(void);

//extern uint32_t ECU_Get_Distance();

void IoHwA_Init(void){

	STM_EVAL_LEDInit( LEDGREEN );
	STM_EVAL_LEDInit( LEDRED );

	STM_EVAL_LEDToggle( LEDGREEN );
	STM_EVAL_LEDToggle( LEDRED );

#if defined (__USE_STM32F4XX_SPD_I2C__) || defined (__USE_STM32F4XX_SPD_ALL_)

	#ifdef SETTING_DISTANCE_ULTRASONIC
	ek_I2C1_Init();
	#endif

#endif

#if defined (__USE_STM32F4XX_SPD_ADC__) || defined (__USE_STM32F4XX_SPD_ALL_)
	#ifdef SETTING_DISTANCE_INFRARED
	ek_ADC_Init();
	#endif
#endif


#if defined (__USE_STM32F4XX_SPD_TIM__) || defined (__USE_STM32F4XX_SPD_ALL_)

	ek_PWM3_Init();

#endif

}

float dist_sampleIR=0;
float dist_sampleUS=0;

float dist_sampleIRFiltered=0;
float dist_sampleUSFiltered=0;

float old_dist_sample=0;
float old_dist_sampleIRFiltered=0;
float old_dist_sampleUSFiltered=0;


#define SAMPLE_BUFFER 10

uint32_t avg_array[SAMPLE_BUFFER] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

uint8_t cnt_i=0;
uint32_t countRounds=0;
uint32_t result;


void ek_sharpIF(void){

	uint32_t count_delay;
	do{
		dist_sampleIR = get_exact_IFdistance(ek_getADC3_Voltage()); //get_IFdistance(ek_getADC3_Voltage());
		avg_array[countRounds] = dist_sampleIR;

		for(count_delay=0; count_delay <100; count_delay++){}

		countRounds++;
	}while(countRounds<SAMPLE_BUFFER);

	for(cnt_i=0;cnt_i<=countRounds; cnt_i++){
		dist_sampleIRFiltered = dist_sampleIRFiltered + avg_array[cnt_i];
	}

	dist_sampleIRFiltered = dist_sampleIRFiltered / countRounds /10;

	dist_sampleIRFiltered = 0.2*old_dist_sampleIRFiltered + 0.8*dist_sampleIRFiltered;


	ek_setDistanceMeasurementStatusIF(Stable);
	old_dist_sampleIRFiltered = dist_sampleIRFiltered;
	ir_distance = dist_sampleIRFiltered;
	dist_sampleIRFiltered=0;

	countRounds=0;

}
uint32_t get_exact_IFdistance(uint32_t voltage){
	SpannungADC = voltage;
	SpannungVolt = SpannungADC/1000;

//	if(SpannungVolt>0.7){
//		y_curv = SpannungVolt*SpannungVolt*c_curv + SpannungVolt*b_curv+a_curv;
//	}else{
//		y_curv = -1.6024844720496830e+002 * SpannungVolt + 1.4577639751552766e+002;
//	}

	if(SpannungVolt>0.7){
		y_curv = SpannungVolt*SpannungVolt*c_curv + SpannungVolt*b_curv+a_curv;
	}else{
		y_curv = 11.6 * SpannungVolt*SpannungVolt - 54.6*SpannungVolt+73.2; //-1.6024844720496830e+002 * SpannungVolt + 1.4577639751552766e+002;
	}

	return (uint32_t)(roundf(y_curv*10));
}


void ek_setDistanceMeasurementStatusIF(MeasurementStatus status){
	status_DistanceSensorIF = status;
}

void ek_setDistanceMeasurementStatusUS(MeasurementStatus status){
	status_DistanceSensorUS = status;
}

MeasurementStatus ek_getDistanceMeasurementStatusUS(){
	ek_srf02_US(); //refresh nur alle 65ms
	return status_DistanceSensorUS;
}

MeasurementStatus ek_getDistanceMeasurementStatusIF(){
	ek_sharpIF();
	return status_DistanceSensorIF;
}

uint32_t ek_getIFdistance(){
	//return pwm_DutyCycle_valueIF;
	return ir_distance;
}

uint32_t ek_getUSdistance(){
	//return pwm_DutyCycle_valueUS;
	return us_distance;
}



void ek_srf02_US(void){

	initUltrasonicSensorI2C(SENSOR1_ADDRESS);
	dist_sampleUSFiltered = readDistance(SENSOR1_ADDRESS);

	dist_sampleUSFiltered = 0.8*dist_sampleUSFiltered + 0.2*old_dist_sampleUSFiltered;

	ek_setDistanceMeasurementStatusUS(Stable);

	//	if(dist_sampleUSFiltered<=150){
	//		pwm_DutyCycle_valueUS= 0;
	//	}else if(dist_sampleUSFiltered>250){
	//		pwm_DutyCycle_valueUS= 800;
	//	}else if(dist_sampleUSFiltered >4000){
	//
	//		ek_setDistanceMeasurementStatusUS(Unstable);
	//	}else{
	//		result = (uint32_t)   ((8*(dist_sampleUSFiltered))-1000); //+200 offset
	//		pwm_DutyCycle_valueUS= result;
	//	}

	old_dist_sampleUSFiltered = dist_sampleUSFiltered;
	us_distance=dist_sampleUSFiltered;
}

uint32_t ECU_Get_Distance(){

	uint32_t pwm_DC=0;

#ifdef IOHWA_DISTANCE_IR

	if(ek_getDistanceMeasurementStatusIF()==Stable){
		return ek_getIFdistance();
		//ek_setPWM3_DutyCycle(2,pwm_DC);
	}
	else{
		//ignore
		return ERROR;
	}

#else
	//ifdef IOHWA_DISTANCE_US
	if(ek_getDistanceMeasurementStatusUS()==Stable){
		return ek_getUSdistance();
		//ek_setPWM3_DutyCycle(1,pwm_DC);
	}
	else{
		//ignore
		return ERROR;
	}
#endif

	//if()
	return 10;
}

uint32_t ECU_Set_Speed(uint32_t value){

	if(value>100){
		return ERROR; //mod
	}else if(value<5){
		pwm_DutyCycle_valueIF = 0;
		ek_setPWM3_DutyCycle(1,pwm_DutyCycle_valueIF);
		ek_setPWM3_DutyCycle(2,pwm_DutyCycle_valueIF);
		return SUCCESS;
	}else{
		pwm_DutyCycle_valueIF = value*10;//(uint32_t)   ((50*(value))-490);
		ek_setPWM3_DutyCycle(1,pwm_DutyCycle_valueIF);
		ek_setPWM3_DutyCycle(2,pwm_DutyCycle_valueIF);
		return SUCCESS;
	}
}


