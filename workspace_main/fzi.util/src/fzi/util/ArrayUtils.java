package fzi.util;

public class ArrayUtils
{
	
	@SuppressWarnings("unchecked")
	public static <T> T[] initializeArray(T... elements)
	{
		return elements;
	}
	
	public static String toHexString(byte[] arr)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
	    for (int i = 0; i < arr.length; i++)
	    {
	    	if (i > 0)
	    		sb.append(", ");
	    	
	    	sb.append(String.format("%02x", arr[i]));
	    }

	    sb.append("]");
		return sb.toString();
	}

}
