/* This file contains stub implementations of the AUTOSAR RTE functions.
   The stub implementations can be used for testing the generated code in
   Simulink, for example, in SIL/PIL simulations of the component under
   test. Note that this file should be replaced with an appropriate RTE
   file when deploying the generated code outside of Simulink.

   This file is generated for:
   Atomic software component:  "ASWC"
   ARXML schema: "4.0"
   File generated on: "08-Oct-2015 22:20:14"  */

#ifndef Std_Types_h
#define Std_Types_h

#include "Platform_Types.h"
#include "Compiler.h"

#define E_OK		0x00U
#define E_NOT_OK	0x01U

#define STD_HIGH	0x01U /*phys 5V or 3V*/
#define STD_LOW		0x00U /*phys 0V*/

#define STD_ACTIVE	0x01U
#define STD_LOW		0x00U

#define STD_ON		0x01U
#define STD_OFF		0x00U


/* define Std_ReturnType */
typedef uint8 Std_ReturnType;

#endif
