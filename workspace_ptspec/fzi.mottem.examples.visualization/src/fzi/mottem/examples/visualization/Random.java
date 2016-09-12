package fzi.mottem.examples.visualization;

public class Random
{
	
	private static final java.util.Random _random = new java.util.Random(42);
	
	public static double getNext(double min, double max)
	{
		double r = _random.nextDouble();
		return min - (r * (max - min));
	}

}
