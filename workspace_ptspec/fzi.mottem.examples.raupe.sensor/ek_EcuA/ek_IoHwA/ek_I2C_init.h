/*
 * ek_I2C_init.h
 *
 *  Created on: Dec 20, 2015
 *      Author: lore_
 */

//PinS 		PACK 1 	PINS PACK 2 	PINS PACK 3
//I2Cx 		SCL 	SDA 	SCL 	SDA 	SCL 	SDA 	APB
//I2C1 		PB6 	PB7 	PB8 	PB9 	PB6 	PB9 	1
//I2C2 		PB10 	PB11 	PF1 	PF0 	PH4 	PH5 	1
//I2C3 		PA8 	PC9 	PH7 	PH8 			1



#ifndef EK_ECUA_EK_IOHWA_EK_I2C_INIT_H_
#define EK_ECUA_EK_IOHWA_EK_I2C_INIT_H_

#include "stm32f4xx_i2c.h"

#include "../../ek_EcuA/add_lib/SRF02.h"
#include "../ek_MCAL/ek_GPIOx_init.h"

typedef struct {
  GPIO_TypeDef* PORT;     // Port
  const uint16_t PIN;     // Pin
  const uint32_t CLK;     // Clock
  const uint8_t SOURCE;   // Source
}I2C1_PIN_t;

typedef struct {
  I2C1_PIN_t  SCL;       // Clock-Pin
  I2C1_PIN_t  SDA;       // Data-Pin
}I2C1_DEV_t,I2C3_DEV_t;




void ek_I2C1_Init(void);
void ek_I2C2_Init(void);
void ek_I2C3_Init(void);


void initSRF02(void);
uint32_t readAutotuneDistance(uint8_t sensorAddress);
uint32_t readDistance(uint8_t sensorAddress);
void setSensorI2CAddress(uint8_t address, uint8_t newAddress);
void initUltrasonicSensorI2C(uint8_t address);


//i2c.h
uint16_t I2C_read_nack(I2C_TypeDef* I2Cx);
uint16_t I2C_stop(I2C_TypeDef* I2Cx);
uint16_t I2C_read_ack(I2C_TypeDef* I2Cx);
uint16_t I2C_write(I2C_TypeDef* I2Cx, uint8_t data);
uint16_t I2C_start(I2C_TypeDef* I2Cx, uint8_t address, uint8_t direction);
uint16_t i2cSendByte(I2C_TypeDef* I2Cx, uint8_t address, uint8_t registerAddress, uint8_t byte);

uint16_t ek_I2C3_timeout(int16_t wert);
uint16_t ek_I2C1_timeout(int16_t wert);

int16_t ek_I2C1_WriteByte(uint8_t slave_adr, uint8_t adr, uint8_t wert);
int16_t ek_I2C1_ReadByte(uint8_t slave_adr, uint8_t adr);

#endif /* EK_ECUA_EK_IOHWA_EK_I2C_INIT_H_ */
