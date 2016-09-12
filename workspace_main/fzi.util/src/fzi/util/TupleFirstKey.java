package fzi.util;

public class TupleFirstKey<X, Y>
{ 
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Ele1.hashCode();
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		TupleFirstKey other = (TupleFirstKey) obj;
		if (!Ele1.equals(other.Ele1))
			return false;

		return true;
	}

	public final X Ele1; 
	public final Y Ele2; 

	public TupleFirstKey(X ele1, Y ele2)
	{
	    Ele1 = ele1; 
	    Ele2 = ele2; 
	}
} 
