package fzi.mottem.runtime.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A method of class RangeValidator can be used to check
 * if a number is really belonging to a particular data type.
 * The numbers belong to the class java.lang.Number
 */
public class RangeValidator 
{
	/**
	 * The value of this constant is Byte.MIN_VALUE which will be used as the lowest 
	 * value that each variable of type byte should greater or equals 
	 */
	public static final BigInteger BYTE_MIN    = BigInteger.valueOf(Byte.MIN_VALUE);
	
	/**
	 * The value of this constant is Byte.MAX_VALUE which will be used as the biggest 
	 * value that each variable of type byte should less or equals 
	 */
	public static final BigInteger BYTE_MAX    = BigInteger.valueOf(Byte.MAX_VALUE);
	
	public static final BigInteger Short_MIN   = BigInteger.valueOf(Short.MIN_VALUE);
	public static final BigInteger Short_MAX   = BigInteger.valueOf(Short.MAX_VALUE);
	
	public static final BigInteger INTEGER_MIN = BigInteger.valueOf(Integer.MIN_VALUE);
	public static final BigInteger INTEGER_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
	
	public static final BigInteger LONG_MIN    = BigInteger.valueOf(Long.MIN_VALUE);
	public static final BigInteger LONG_MAX    = BigInteger.valueOf(Long.MAX_VALUE);
	
	public static final BigDecimal FLOAT_MIN   = BigDecimal.valueOf(Float.MIN_VALUE);
	public static final BigDecimal FLOAT_MAX   = BigDecimal.valueOf(Float.MAX_VALUE);
	
	public static final BigDecimal DOUBLE_MIN  = BigDecimal.valueOf(Double.MIN_VALUE);
	public static final BigDecimal DOUBLE_MAX  = BigDecimal.valueOf(Double.MAX_VALUE);
	
	/**
	 * Test whether the argument belongs to the data type Byte
	 * @param value the number to test
	 * @return return true if the argument belongs to the data type bytes,
	 *         false otherwise.
	 */
	public static boolean isInByteRange(Number value)
	{
		return isInRange(value, BYTE_MIN, BYTE_MAX);
	}
	
	/**
	 * Test whether the argument belongs to the data type short
	 * @param value the number to test
	 * @return return true if the argument belongs to the data type short,
	 *         false otherwise.
	 */
	public static boolean isInShortRange(Number value)
	{
		return isInRange(value, Short_MIN, Short_MAX);
		
	}
	
	/**
	 * Test whether the argument belongs to the data type integer
	 * @param value the number to test
	 * @return return true if the argument belongs to the data type integer,
	 *         false otherwise.
	 */
	public static boolean isInIntegerRange(Number value)
	{
		return isInRange(value, INTEGER_MIN, INTEGER_MAX);
		
	}
	
	/**
	 * Test whether the argument belongs to the data type long
	 * @param value the number to test
	 * @return return true if the argument belongs to the data type long,
	 *         false otherwise.
	 */
	public static boolean isInLongRange(Number value)
	{
		return isInRange(value, LONG_MIN, LONG_MAX);
	}
	
	/**
	 * This method build the body of each method of the form
	 * RangeValidator.isIn*Range(Number) where * represents 
	 * a fixed point data type. 
	 * @param value value the number to test
	 * @param min the lowest value of a data type
	 * @param max the biggest value of a data type
	 * @return see the return value of RangeValidator.isIn*Range(Number)
	 */
	public static boolean isInRange(Number value, BigInteger min, BigInteger max)
	{
		BigInteger castedValue = null;
		
		if (value instanceof Byte || value instanceof Short ||
		    value instanceof Integer || value instanceof Long)
		{
		     castedValue = BigInteger.valueOf(value.longValue());	
		}
		else if (value instanceof Float || value instanceof Double)
		{
			castedValue = BigDecimal.valueOf(value.doubleValue()).toBigInteger();	
		}
		
		boolean isInRange = max.compareTo(castedValue) >= 0 && min.compareTo(castedValue) <= 0;
		
		return isInRange;
	}
	
	/**
	 * Test whether the argument belongs to the data type float
	 * @param value the number to test
	 * @return return true if the argument belongs to the data type float,
	 *         false otherwise.
	 */
	public static boolean isInFloatRange(Number value)
	{
		return isInRange(value, FLOAT_MIN, FLOAT_MAX);
	}
	
	/**
	 * Test whether the argument belongs to the data type double
	 * @param value the number to test
	 * @return return true if the argument belongs to the data type double,
	 *         false otherwise.
	 */
	public static boolean isInDoubleRange(Number value)
	{
		return isInRange(value, DOUBLE_MIN, DOUBLE_MAX);
	}
	
	/**
	 * This method build the body of each method of the form
	 * RangeValidator.isIn*Range(Number) where * represents 
	 * a floating point data type. 
	 * @param value value the number to test
	 * @param min the lowest value of a data type
	 * @param max the biggest value of a data type
	 * @return see the return value of RangeValidator.isIn*Range(Number)
	 */
	public static boolean isInRange(Number value, BigDecimal min, BigDecimal max)
	{
		BigDecimal castedValue = null;
		
		if (value instanceof Byte || value instanceof Short ||
			    value instanceof Integer || value instanceof Long)
		{
			castedValue = BigDecimal.valueOf(value.longValue());
			
		}
		else if (value instanceof Float || value instanceof Double)
		{
			castedValue = BigDecimal.valueOf(value.doubleValue());
		}
		
        boolean isInRange = max.compareTo(castedValue) >= 0 && min.compareTo(castedValue) <= 0;
		
		return isInRange;
	}
	

}
