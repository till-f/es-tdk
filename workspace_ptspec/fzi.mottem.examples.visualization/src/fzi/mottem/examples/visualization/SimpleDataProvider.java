package fzi.mottem.examples.visualization;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;

public class SimpleDataProvider
{	
	static long start = System.currentTimeMillis();
	
	public static void staticSend(String signalUID, double value)
	{
		Signal s = DataExchanger.getSignal(signalUID);
				
		DataExchanger.consume(new DEMessage(s, value, System.currentTimeMillis() - start));
	}
}
