/*
 * ek_I2C_init.c
 *
 *  Created on: Dec 20, 2015
 *      Author: lore_
 */

#include "../../ek_EcuA/ek_IoHwA/ek_I2C_init.h"

#define I2C1_TIMEOUT 0x3000

uint32_t readRegister(uint8_t sensorAddress, uint8_t _register);
uint32_t receivedRanging_data[2];
uint32_t rangingData[4];                /* Sensoren 1 - 4 */
uint32_t readDistance(uint8_t sensorAddress);


void ek_I2C1_Init(void)
{
	GPIO_CONF_PARAM GPIO_conf_param_I2C1;

	//------------------------------------------------------SDA Pin
	//----------------------------------SET GPIO
	GPIO_conf_param_I2C1.GPIO_PORT = ULTRASONICSENSOR_SDA_PORT;
	GPIO_conf_param_I2C1.GPIO_InitStructure.GPIO_Pin = ULTRASONICSENSOR_SDA_PIN;
	GPIO_conf_param_I2C1.GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF;
	GPIO_conf_param_I2C1.GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_conf_param_I2C1.GPIO_InitStructure.GPIO_OType = GPIO_OType_OD;
	GPIO_conf_param_I2C1.GPIO_InitStructure.GPIO_PuPd  = GPIO_PuPd_NOPULL;

	GPIO_conf_param_I2C1.GPIOx_BUS_CLK = AHB1;
	GPIO_conf_param_I2C1.RCCGPIO_CLK = ULTRASONICSENSOR_RCC_SDA_PORT_CLOCK;

	GPIO_conf_param_I2C1.GPIO_AF = ULTRASONICSENSOR_AF;
	GPIO_conf_param_I2C1.GPIO_PIN_SOURCE = ULTRASONICSENSOR_SDA_PINSOURCE;
	ek_GPIOx_Init(GPIO_conf_param_I2C1);

	//------------------------------------------------------SCL Pin
	//----------------------------------SET GPIO
	GPIO_conf_param_I2C1.GPIO_PORT = ULTRASONICSENSOR_SCL_PORT;
	GPIO_conf_param_I2C1.GPIO_InitStructure.GPIO_Pin = ULTRASONICSENSOR_SCL_PIN;
	GPIO_conf_param_I2C1.GPIOx_BUS_CLK = AHB1;
	GPIO_conf_param_I2C1.RCCGPIO_CLK = ULTRASONICSENSOR_RCC_SCL_PORT_CLOCK; // | ULTRASONICSENSOR_RCC_SDA_PORT_CLOCK;

	GPIO_conf_param_I2C1.GPIO_AF = ULTRASONICSENSOR_AF;
	GPIO_conf_param_I2C1.GPIO_PIN_SOURCE = ULTRASONICSENSOR_SCL_PINSOURCE;
	ek_GPIOx_Init(GPIO_conf_param_I2C1);



	RCC_APB1PeriphClockCmd(RCC_APB1Periph_I2C1, ENABLE);
	// I2C init
	I2C_InitTypeDef  I2C_InitStructure;

	// I2C-Konfiguration
	I2C_InitStructure.I2C_Mode = I2C_Mode_I2C;
	I2C_InitStructure.I2C_DutyCycle = I2C_DutyCycle_2;
	I2C_InitStructure.I2C_OwnAddress1 = 0x00;
	I2C_InitStructure.I2C_Ack = I2C_Ack_Disable;
	I2C_InitStructure.I2C_AcknowledgedAddress = I2C_AcknowledgedAddress_7bit;
	I2C_InitStructure.I2C_ClockSpeed = 400000;

	// I2C enable
	I2C_Cmd(I2C1, ENABLE);

	// Init Struktur
	I2C_Init(I2C1, &I2C_InitStructure);
}

uint16_t ek_I2C1_timeout(int16_t wert)
{
	int16_t ret_wert=wert;

	// Stop und Reset
	I2C_GenerateSTOP(I2C1, ENABLE);
	I2C_SoftwareResetCmd(I2C1, ENABLE);
	I2C_SoftwareResetCmd(I2C1, DISABLE);

	// I2C deinit
	I2C_DeInit(I2C1);
	// I2C init
	ek_I2C1_Init();

	return(ret_wert);
}

uint16_t ek_I2C3_timeout(int16_t wert)
{
	int16_t ret_wert=wert;

	// Stop und Reset
	I2C_GenerateSTOP(I2C3, ENABLE);
	I2C_SoftwareResetCmd(I2C3, ENABLE);
	I2C_SoftwareResetCmd(I2C3, DISABLE);

	// I2C deinit
	I2C_DeInit(I2C3);
	// I2C init
	//ek_I2C3_Init();

	return(ret_wert);
}

//---------------------------------------------------------------------------I2C Fct
uint16_t I2C_start(I2C_TypeDef* I2Cx, uint8_t address, uint8_t direction) {
	while (I2C_GetFlagStatus(I2Cx, I2C_FLAG_BUSY))
		;

	I2C_GenerateSTART(I2Cx, ENABLE);

	//while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_MODE_SELECT));

	uint32_t timeout=I2C1_TIMEOUT;

	while (!I2C_GetFlagStatus(I2C3, I2C_FLAG_SB)) {
		if(timeout!=0) timeout--;
		else return(ek_I2C3_timeout(-1));
	}


	I2C_Send7bitAddress(I2Cx, address, direction);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C3, I2C_FLAG_ADDR)) {
		if(timeout!=0) timeout--;
		else return(ek_I2C3_timeout(-1));
	}

	// ADDR-Flag löschen
	I2C3->SR2;

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C3, I2C_FLAG_TXE)) {
		if(timeout!=0) timeout--;
		else return(ek_I2C3_timeout(-1));
	}
	return 1;

	//	if (direction == I2C_Direction_Transmitter) {
	//		while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_TRANSMITTER_MODE_SELECTED))
	//			;
	//	} else if (direction == I2C_Direction_Receiver) {
	//		while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_RECEIVER_MODE_SELECTED))
	//			;
	//	}

}

uint16_t I2C_write(I2C_TypeDef* I2Cx, uint8_t data) {
	I2C_SendData(I2Cx, data);
	while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_BYTE_TRANSMITTED))
		;
	return 0;
}

uint16_t I2C_read_ack(I2C_TypeDef* I2Cx) {
	I2C_AcknowledgeConfig(I2Cx, ENABLE);
	while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_BYTE_RECEIVED))
		;
	uint8_t data = I2C_ReceiveData(I2Cx);
	return data;
}

uint16_t I2C_read_nack(I2C_TypeDef* I2Cx) {
	I2C_AcknowledgeConfig(I2Cx, DISABLE);
	I2C_GenerateSTOP(I2Cx, ENABLE);
	while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_BYTE_RECEIVED))
		;
	uint8_t data = I2C_ReceiveData(I2Cx);
	return data;
}

uint16_t I2C_stop(I2C_TypeDef* I2Cx) {
	I2C_GenerateSTOP(I2Cx, ENABLE);
	while (!I2C_CheckEvent(I2Cx, I2C_EVENT_MASTER_BYTE_TRANSMITTED))
		;
	return 0;
}

uint16_t i2cSendByte(I2C_TypeDef* I2Cx, uint8_t address, uint8_t registerAddress, uint8_t byte){
	I2C_start(I2Cx, address, I2C_Direction_Transmitter);
	I2C_write(I2Cx, registerAddress);
	I2C_write(I2Cx, byte);
	I2C_stop(I2Cx);
	return 0;
}

//--------------------------------------------------------------------SR02

void setSensorI2CAddress(uint8_t address, uint8_t newAddress){
	i2cSendByte(ULTRASONICSENSOR_I2C, address, COMMAND_REGISTER, 0xA0);
	i2cSendByte(ULTRASONICSENSOR_I2C, address, COMMAND_REGISTER, 0xAA);
	i2cSendByte(ULTRASONICSENSOR_I2C, address, COMMAND_REGISTER, 0xA5);
	i2cSendByte(ULTRASONICSENSOR_I2C, address, COMMAND_REGISTER, newAddress);
}
uint16_t rckwert;
void initUltrasonicSensorI2C(uint8_t address) {
	rckwert=ek_I2C1_WriteByte(address,0x00,0x51);
	//	I2C_start(ULTRASONICSENSOR_I2C, address, I2C_Direction_Transmitter); // start in Master transmitter mode
	//	I2C_write(ULTRASONICSENSOR_I2C, 0x00);                               // command register address
	//	I2C_write(ULTRASONICSENSOR_I2C, 0x51);                               // measuring unit in [cm]
	//	I2C_stop(ULTRASONICSENSOR_I2C);                                      // stop the transmission
}

uint32_t readRegister(uint8_t sensorAddress, uint8_t _register){
	I2C_start(ULTRASONICSENSOR_I2C, sensorAddress, I2C_Direction_Transmitter);
	I2C_write(ULTRASONICSENSOR_I2C, _register);
	I2C_stop(ULTRASONICSENSOR_I2C);
	I2C_start(ULTRASONICSENSOR_I2C, sensorAddress, I2C_Direction_Receiver);
	return I2C_read_nack(ULTRASONICSENSOR_I2C);
}

uint32_t readDistance(uint8_t sensorAddress){
	//	receivedRanging_data[0] = readRegister(SENSOR1_ADDRESS, 0x03);  // low byte
	//	receivedRanging_data[1] = readRegister(SENSOR1_ADDRESS, 0x02);  // high byte

	receivedRanging_data[0] = ek_I2C1_ReadByte(sensorAddress,0x03);
	receivedRanging_data[1] = ek_I2C1_ReadByte(sensorAddress,0x02);
	return ((receivedRanging_data[1] << 8) | receivedRanging_data[0])*10;
}

uint32_t readAutotuneDistance(uint8_t sensorAddress){
	uint16_t minimumRange = readRegister(sensorAddress, 0x04);              // autotune minimum low byte
	minimumRange |= ((readRegister(sensorAddress, 0x05)) << 1);             // autotune minimum high byte
	return minimumRange;
}


int16_t ek_I2C1_WriteByte(uint8_t slave_adr, uint8_t adr, uint8_t wert)
{
	int16_t ret_wert=0;
	uint32_t timeout=I2C1_TIMEOUT;

	// begin sequence
	I2C_GenerateSTART(I2C1, ENABLE);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_SB)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-1));
	}

	// send slave-addr
	I2C_Send7bitAddress(I2C1, slave_adr, I2C_Direction_Transmitter);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_ADDR)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-2));
	}

	// del addr-flag
	I2C1->SR2;

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_TXE)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-3));
	}

	// send addr
	I2C_SendData(I2C1, adr);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_TXE)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-4));
	}

	// send data
	I2C_SendData(I2C1, wert);

	timeout=I2C1_TIMEOUT;
	while ((!I2C_GetFlagStatus(I2C1, I2C_FLAG_TXE)) || (!I2C_GetFlagStatus(I2C1, I2C_FLAG_BTF))) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-5));
	}

	// stop sequence
	I2C_GenerateSTOP(I2C1, ENABLE);

	ret_wert=0; // alles ok

	return(ret_wert);
}


int16_t ek_I2C1_ReadByte(uint8_t slave_adr, uint8_t adr)
{
	int16_t ret_wert=0;
	uint32_t timeout=I2C1_TIMEOUT;

	// begin sequence
	I2C_GenerateSTART(I2C1, ENABLE);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_SB)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-1));
	}

	// disable ack
	I2C_AcknowledgeConfig(I2C1, DISABLE);

	// send slave-addr
	I2C_Send7bitAddress(I2C1, slave_adr, I2C_Direction_Transmitter);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_ADDR)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-2));
	}

	// del addr-flag
	I2C1->SR2;

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_TXE)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-3));
	}

	// send addr
	I2C_SendData(I2C1, adr);

	timeout=I2C1_TIMEOUT;
	while ((!I2C_GetFlagStatus(I2C1, I2C_FLAG_TXE)) || (!I2C_GetFlagStatus(I2C1, I2C_FLAG_BTF))) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-4));
	}

	// begin sequence
	I2C_GenerateSTART(I2C1, ENABLE);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_SB)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-5));
	}

	// send slave-addr
	I2C_Send7bitAddress(I2C1, slave_adr, I2C_Direction_Receiver);

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_ADDR)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-6));
	}

	// del addr-flag 
	I2C1->SR2;

	timeout=I2C1_TIMEOUT;
	while (!I2C_GetFlagStatus(I2C1, I2C_FLAG_RXNE)) {
		if(timeout!=0) timeout--; else return(ek_I2C1_timeout(-7));
	}

	// end sequence
	I2C_GenerateSTOP(I2C1, ENABLE);

	// read data
	ret_wert=(int16_t)(I2C_ReceiveData(I2C1));

	// enable ack
	I2C_AcknowledgeConfig(I2C1, ENABLE);

	return(ret_wert);
}
