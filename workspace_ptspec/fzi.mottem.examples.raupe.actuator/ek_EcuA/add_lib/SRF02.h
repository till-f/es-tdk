#ifndef ULTRASONICSENSORCOMPONENT_H_
#define ULTRASONICSENSORCOMPONENT_H_
#include <stm32f4xx_gpio.h>


//I2C1_DEV_t I2C1DEV = {
//// PORT , PIN      , Clock              , Source
//  {GPIOB,GPIO_Pin_6,RCC_AHB1Periph_GPIOB,GPIO_PinSource6}, // SCL an PB6
//  {GPIOB,GPIO_Pin_7,RCC_AHB1Periph_GPIOB,GPIO_PinSource7}, // SDA an PB7
//};

#define ULTRASONICSENSOR_I2C I2C1
#define ULTRASONICSENSOR_AF GPIO_AF_I2C1
#define ULTRASONICSENSOR_I2C_CLKSPEED 400000 // 40KHz
#define ULTRASONICSENSOR_RCC_I2C_CLOCK RCC_APB1Periph_I2C1
#define ULTRASONICSENSOR_SCL_PIN GPIO_Pin_6
#define ULTRASONICSENSOR_RCC_SCL_PORT_CLOCK RCC_AHB1Periph_GPIOB
#define ULTRASONICSENSOR_SCL_PORT GPIOB
#define ULTRASONICSENSOR_SCL_PINSOURCE GPIO_PinSource6

#define ULTRASONICSENSOR_SDA_PIN GPIO_Pin_7
#define ULTRASONICSENSOR_SDA_PORT GPIOB
#define ULTRASONICSENSOR_RCC_SDA_PORT_CLOCK RCC_AHB1Periph_GPIOB
#define ULTRASONICSENSOR_SDA_PINSOURCE GPIO_PinSource7

#define SENSOR1_ADDRESS 0xE0
#define SENSOR2_ADDRESS 0xE2
#define SENSOR3_ADDRESS 0xE4
#define SENSOR4_ADDRESS 0xE6

#define COMMAND_REGISTER 0x00

//void initSRF02(void);
//uint16_t readAutotuneDistance(uint8_t sensorAddress);
//uint16_t readDistance(uint8_t sensorAddress);
//void setSensorI2CAddress(uint8_t address, uint8_t newAddress);
#endif
