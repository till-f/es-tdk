#ifndef BASE_TYPES_H
#define BASE_TYPES_H

#if defined(_Windows) || defined(_MSC_VER) || defined (__GNUC__)
#define  STRICT
#include <windows.h>
#endif

struct canMsg
{
    short id;
    byte data[8];
};

#define RECEIVE_EVENT_SIZE 64

#endif