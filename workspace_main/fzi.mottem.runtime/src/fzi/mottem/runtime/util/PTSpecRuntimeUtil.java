package fzi.mottem.runtime.util;

import java.util.ArrayList;

public class PTSpecRuntimeUtil
{
	public static <T> void resizeAndSet(ArrayList<T> arrayList, int insertIndex, T element)
	{
		int idx = arrayList.size();
		
		while (idx <= insertIndex)
		{
			arrayList.add(null);
			idx++;
		}
		
		arrayList.set(insertIndex, element);
	}

	public static void sleep(double milliSeconds) throws InterruptedException
	{
		Thread.sleep((long) milliSeconds);
	}

	public static void sleepThrowRT(double milliSeconds)
	{
		try
		{
			Thread.sleep((long) milliSeconds);
		}
		catch (InterruptedException e)
		{
			throw new ExecutionInterruptedException(e);
		}
	}
}
