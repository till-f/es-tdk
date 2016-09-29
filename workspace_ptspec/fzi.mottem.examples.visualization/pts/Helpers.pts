package Helpers
{
	analyze func void SmoothAverage2(ref double signal, ref double result)
	{
		double[5] vals;
		int32 posToReplace=0;
		
		while (!Runtime:EOT)
		{
			wait until signal´written;
			
			vals[posToReplace] = signal;
			posToReplace = (posToReplace + 1) % 5;
			
			result = GetArrayAverage(vals);
		}
	}
	
	func double GetArrayAverage(double[5] arr)
	{
		double sum = 0;
		for (int32 i = 0; i<arr:Count; i+=1)
		{
			sum += arr[i];
		}
		
		if (arr:Count > 0)
			return sum / arr:Count;
		else
			return 0;
	}
	
	analyze func void SmoothAverage(ref double signal, ref double result, double t0)
	{
		while (!Runtime:EOT)
		{
			wait t0;
			result = GetTraceAverage(ref signal, Runtime:Timestamp - t0, Runtime:Timestamp + t0);
		}
	}
	
	analyze func double GetTraceAverage(ref double signal, double t0, double t1)
	{
		if (t0 < 0) t0 = 0;
		
		wait t0;
		
		double average = 0;
		
		double lastChangeValue = signal;
		double lastChangeTime = t0;
		
		while (Runtime:Timestamp < t1 && !Runtime:EOT)
		{
			wait until signal´written || Runtime:EOT;
			average = (average * (lastChangeTime - t0) + lastChangeValue * (Runtime:Timestamp - lastChangeTime)) / (Runtime:Timestamp - t0);
			lastChangeValue = signal;
			lastChangeTime = Runtime:Timestamp;
		}
		
		return average;
	}
	
	analyze func void Shift(ref double inSig, ref double outSig)
	{
		while (!Runtime:EOT)
		{
			wait until inSig´written || Runtime:EOT;
			outSig = inSig + 0.2;
		}
	}
}
