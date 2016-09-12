package fzi.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BitMagic
{
	public enum EIntegralDataType
	{
		Integer, Float
	}
	
	public static byte[] toByteArray(long data)
	{
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(data);
	    return buffer.array();
	}
	
	public static int getByteCount(int bitCount)
	{
		return bitCount / 8 + ((bitCount % 8 == 0) ? 0 : 1);
	}
	
	public static byte[] getBits(byte[] src, int startBit, int bitLength)
	{
		int dstLen = getByteCount(bitLength);
		byte[] dst = new byte[dstLen];
		
		final int chunkOffset = startBit % 8;
		
		int bitsLeft = bitLength;
		for (int dstIdx = 0, srcIdx = startBit / 8; dstIdx < dstLen; dstIdx++, srcIdx++, bitsLeft -= 8)
		{
			if (chunkOffset == 0)
			{
				dst[dstIdx] = src[srcIdx];
			}
			else
			{
				short chunk = (short)(((short) src[srcIdx]) >> chunkOffset);
				int msk = ~((1 << (8-chunkOffset)) - 1);
				chunk |= (src[srcIdx + 1] << (8-chunkOffset)) & msk;
				if (bitsLeft >= 8)
				{
					dst[dstIdx] = (byte)chunk;
				}
				else
				{
					dst[dstIdx] = (byte)(chunk & ((1 << bitsLeft) - 1));
				}
			}
		}
		
		return dst;
	}
	
	public static byte[] swapByteOrder(byte[] source)
	{
		byte[] dst = new byte[source.length];
		for (int i=0; i<source.length; i++)
		{
			dst[i] = source[source.length - 1 - i]; 
		}
		return dst;
	}

	public static double getValueDouble(byte[] data, EIntegralDataType integralType, boolean swapByteOrder, double offset, double factor)
	{
		double value = 0;
		switch (integralType)
		{
			case Float:
				if (swapByteOrder)
					data = swapByteOrder(data);

				if (data.length == 4)
				{
					value = ByteBuffer.wrap(data).getFloat();
				}
				else if (data.length == 8)
				{
					value = ByteBuffer.wrap(data).getDouble();
				}
				else
				{
					throw new RuntimeException("Unsupported FLOAT format");
				}
				break;
			case Integer:
				if (swapByteOrder)
					data = swapByteOrder(data);
				BigInteger valueInt = new BigInteger(data);
				value = valueInt.doubleValue();
				break;
		}
		
		value *= factor;
		value += offset;
		
		return value;
	}

	public static byte[] getValueRAW(double value, EIntegralDataType integralType, boolean swapByteOrder, double offset, double factor, int byteCount)
	{
		value -= offset;
		value /= factor;
		
		ByteBuffer buffer = ByteBuffer.allocate(byteCount);

		switch (integralType)
		{
			case Float:
				if (byteCount == 4)
				{
					buffer.putFloat((float) value);
				}
				else if (byteCount == 8)
				{
					buffer.putDouble(value);
				}
				else
				{
					throw new RuntimeException("Unsupported FLOAT format");
				}
				break;
			case Integer:
				long valueRAW = (long)value;
			    for (int idx = 0; idx < byteCount; valueRAW >>= 8, idx++)
			    {
			    	buffer.put(idx, (byte)valueRAW);
			    }
				break;
		}

	    byte[] arrayRAW = buffer.array();
	    
	    if (swapByteOrder)
	    {
	    	arrayRAW = swapByteOrder(arrayRAW);
	    }
	    
	    return arrayRAW;
	}

	public static void mergeInto(byte[] originalData, byte[] newData, int startBit, int bitLength)
	{
    	if (startBit % 8 != 0 || (startBit + bitLength) % 8 != 0)
    	{
    		throw new RuntimeException("BitMagic.mergeInto does not support unaligned merge");
    	}

    	int startByte = startBit / 8;
    	int lastBit = startBit + bitLength - 1;
    	int lastByte = lastBit / 8;

	    for (int trgtIdx = startByte, srcIdx = 0; trgtIdx <= lastByte; trgtIdx++, srcIdx++)
		{
	    	originalData[trgtIdx] = newData[srcIdx];
		}
	}
}
