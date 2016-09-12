/*
 * This is an exemplary PTS file.
 * Before starting here you have to set up a valid
 * test environment in the testrig description
 * (.etm-testrig.diagram file).
 */

include "../model/OsziDemo.etm-testrig";
include "Units.pts";

target Target { OsziDemo.FuncGen.PinA }

test Example01 : Target default PinA
{
	report info: "started" ;
	
	realtime timeout 5000
	{
		PinA.Value:TriggerAbove = 0.5;
	}
	plot Example
	{
		Signal : PinA.Value;
	}
	
	report info: "finished" ;
}
