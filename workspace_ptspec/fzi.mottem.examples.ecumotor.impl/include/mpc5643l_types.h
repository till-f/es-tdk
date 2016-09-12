#ifndef TYPEDEFS_H_
#define TYPEDEFS_H_

#ifdef __cplusplus
extern "C" {
#endif

typedef signed char int8_t;
typedef unsigned char uint8_t;
typedef volatile signed char vint8_t;
typedef volatile unsigned char vuint8_t;

typedef signed short int16_t;
typedef unsigned short uint16_t;
typedef volatile signed short vint16_t;
typedef volatile unsigned short vuint16_t;

typedef signed int int32_t;
typedef unsigned int uint32_t;
typedef volatile signed int vint32_t;
typedef volatile unsigned int vuint32_t;

typedef void (*resetfuncptr)(void);

#ifdef __cplusplus
}
#endif

#endif  /* TYPEDEFS_H_ */

