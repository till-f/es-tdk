include "Units.ets";

package SignalAnalyzer
{
	
	analyze func physical[s] findRisingEdge(ref physical[V] signal, physical[s] timeOffset, physical[V] valueDelta)
	{
		wait timeOffset;
		
		physical[V] baseValue = signal;
		
		while(!Runtime:EOT)
		{
			wait until signal´written;
			if (signal > baseValue + valueDelta)
			{
				return Runtime:Timestamp;
			}
		}
		
		return 0s;
	}
	
	analyze func physical[s] findFallingEdge(ref physical[V] signal, physical[s] timeOffset, physical[V] valueDelta)
	{
		wait timeOffset;
		
		physical[V] baseValue = signal;
		
		while(!Runtime:EOT)
		{
			wait until signal´written;
			if (signal < baseValue - valueDelta)
			{
				return Runtime:Timestamp;
			}
		}
		
		return 0s;
	}
}