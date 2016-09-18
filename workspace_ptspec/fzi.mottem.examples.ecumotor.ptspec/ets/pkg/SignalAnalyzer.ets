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

	analyze func float getMaximumAfter(ref float signal, physical[s] timeOffset)
	{
		wait timeOffset;
		
		float maxValue = signal;
		
		while (!Runtime:EOT)
		{
			wait until signal´written;
			if (signal > maxValue) maxValue = signal;
		}
		
		return maxValue;
	}

	analyze func float getMinimumAfter(ref float signal, physical[s] timeOffset)
	{
		wait timeOffset;
		
		float minValue = signal;
		
		while (!Runtime:EOT)
		{
			wait until signal´written;
			if (signal < minValue) minValue = signal;
		}
		
		return minValue;
	}
}