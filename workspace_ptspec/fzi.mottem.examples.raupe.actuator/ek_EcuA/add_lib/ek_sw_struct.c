/*
 * ek_sw_struct.c
 *
 *  Created on: Jan 12, 2016
 *      Author: lore_
 */

#include "../../ek_EcuA/add_lib/ek_sw_struct.h"

void Delay_Init(void) {
    RCC_ClocksTypeDef RCC_Clocks;

    /* Get system clocks */
    RCC_GetClocksFreq(&RCC_Clocks);

    /* While loop takes 4 cycles */
    /* For 1 us delay, we need to divide with 4M */
    multiplier = RCC_Clocks.HCLK_Frequency / 4000000;
}

void DelayMicroS(uint32_t micros) {
    /* Multiply micros with multipler */
    /* Substract 10 */
    micros = micros * multiplier - 10;
    /* 4 cycles for one loop */
    while (micros--);
}


/**
 * @author stfwi
 *
 * FIR FIXED POINT FILTER EXAMPLE
 *
 * This example is equivalent to the floating point FIR filter example,
 * except that we use fixed point arithmetic.
 *
**/


/**
 * The size of our filter.
 */
#define FIR_FILTER_SIZE (8)

/**
 * These are coefficients of the FIR filter. In this case the size is 8 and
 * all values are 1/8. This means it works exactly like a sliding window
 * (moving average) filter.
 *
 * We have a signed 16 bit integer and the 16th bit is the sign bit, so
 * 15 bits are left for our numbers. As the filter coefficients are values
 * between -1 and 1 we represent these values as Q15 fixed point numbers, means
 * we set the period after the sign bit. This means if we scale everything by
 * 2^15 = 32768.
 */
#define MOV_AVG_COEFF ((1<<15) / FIR_FILTER_SIZE)

int16_t fir_coeffs[FIR_FILTER_SIZE] = {
  MOV_AVG_COEFF, MOV_AVG_COEFF, MOV_AVG_COEFF, MOV_AVG_COEFF,
  MOV_AVG_COEFF, MOV_AVG_COEFF, MOV_AVG_COEFF, MOV_AVG_COEFF
};

/**
 * This is the history buffer of the values. It is used as a ring buffer.
 */
int16_t fir_buffer[FIR_FILTER_SIZE];

/**
 * This is the actual position of the ring buffer.
 */
int16_t *fir_position;

/**
 * Here we initialise our FIR filter. In this case we only set the
 * initial position and set all buffer values to zero.
 */
void filter_init() {
  fir_position = fir_buffer;
  int i;
  for(i=0; i<FIR_FILTER_SIZE; i++) {
    fir_buffer[i] = 0;
  }
}

/**
 * As example, we "outsource" the MAC operation. DSP processors often have
 * a special CPU instruction to do this very fast. Because of the fixed point
 * multiplication with signed numbers the period moves from bit 15 to bit 30.
 * This is where we saturate the accumulator to protect overflowing.
 */
#define MAC(ACC, V1, V2) {                          \
  (ACC) += (int32_t)(V1) * (int32_t)(V2);           \
  if((ACC)      >  0x3fffffff) (ACC) = 0x3fffffff;  \
  else if((ACC) < -0x40000000) (ACC) = -0x40000000; \
}

/**
 * This function is called every cycle. It adds the new value (e.g. read from
 * an analog input) and accumulates the products of all history values with
 * their corresponding coefficient.
 *
 * The range of our input and output is -32768 to +32767, but we return
 * floating point to fit the requirements of the main() function.
 *
 */
real_t filter(real_t new_value) {

  // 1st coeff position is at pointer position fir_coeffs
  int16_t *p_coeff  = fir_coeffs;

  // 1st buffer position is at the actual write position
  int16_t *p_buffer = fir_position;

  // This is our function result
  int16_t result = 0;

  // This is the accumulator for the MAC operations.
  // It must be much bigger than our values and coefficients, on the one
  // hand because of the multiplications, on the other hand because of the
  // accumulation. In theory it is number of bits of the values plus number
  // of bits of the coefficients plus size of the coefficient vector in bits.
  // We preload the accumulator with 0.5 for rounding, and 0.5 expressed as
  // Q15 fixed point number is 1<<14 because 1<<15 would be 1.0.
  int32_t acc = (1<<14);

  // We overwrite the oldest value with the new value. This is now our 1st
  // position to start.
  *fir_position = new_value;

  // As the buffer has the same size as the coefficients, we can iterate to
  // the end of the buffer. The p_coeff pointer cannot overflow here.
  while(p_buffer < fir_buffer + FIR_FILTER_SIZE) {
    // Multiply and accumulate operation
    MAC(acc, *p_coeff, *p_buffer);
    // Next coeff, next buffer position
    p_coeff++;
    p_buffer++;
  }

  // Reset the pointer to the start of the ring buffer and iterate until the
  // p_coeff pointer reaches its end. After this we have MAC'ed all history
  // values with their corresponding coefficients.
  p_buffer = fir_buffer;
  while(p_coeff < fir_coeffs + FIR_FILTER_SIZE) {
    // MAC operation
    MAC(acc, *p_coeff, *p_buffer);
    // Increment buffer and coefficient position.
    p_coeff++;
    p_buffer++;
  }

  // Because of the multiplications your period shifted to bit 30, so we
  // shift it back to bit 15 to get a 16 bit signed number result.
  result = (int16_t) (acc >> 15);

  // To finish this, we advance our ring buffer write position and roll over
  // to the beginning if we reached the end of the memory block.
  if(--fir_position < fir_buffer) {
    fir_position = fir_buffer + FIR_FILTER_SIZE - 1;
  }

  // Return our filter result. It is a value between -1 and 1 and scaled by
  // 32768 (Q15 number).
  return result;
}
