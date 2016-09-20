include "Units.ets";

test ExampleHighTimeResolution
{
	realtime
	{
		
	}
	analyze
	{
		for (capture double i=0; i<1; i+=0.05)
		{
			wait 0.1ms;
		}
	}
	plot Dummy
	{
		Dummy: i;
	}
}