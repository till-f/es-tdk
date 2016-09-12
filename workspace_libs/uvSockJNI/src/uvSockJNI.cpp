// uvSockJNI.cpp : Definiert den Einstiegspunkt für die Konsolenanwendung.
//
#include <iostream>
#include <UVSC_C.h>

#include "uvSocketWrapper.h"

using namespace std;
void main ()
{
	unsigned int v1, v2;
	UVSC_Version(&v1, &v2);
	cout << "Hello World. versions are: "<< v1 << ", " << v2 << endl;

	UVSC_STATUS status = init();
	cout << "Init status: "<< status << endl;
	 run();
	 cin.get();
	cout<<"run until: "<<endl;
	 runUntil(0x000004D0);
	 cin.get();
	cout<<"run until2: "<<endl;
	//runUntil2(&UVSC_DBG_ENUM_STACK);
	 cin.get();
	  UVSC_CloseConnection(0,true);
	
}

