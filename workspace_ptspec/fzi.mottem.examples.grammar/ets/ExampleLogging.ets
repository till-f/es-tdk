include "Units.ets";

test ExampleLogging
{
	report info: "Test started";
	
	wait 1.5 s;
	
	report warning: "Unexpected precondition detected";
	
	for (int32 i=0; i<5; i+=1)
	{
		report info: "Staring iteration " + i;
		
		wait 2.13 s;		

		if (i == 2)
			report error: "Invaild measurement in iteration " + i;
		
		wait 3.35 s;		
	}
	
	report info: "Test completed";
}