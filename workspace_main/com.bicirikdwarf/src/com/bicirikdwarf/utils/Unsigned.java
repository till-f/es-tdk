package com.bicirikdwarf.utils;

import java.nio.ByteBuffer;

// taken from:
// http://stackoverflow.com/questions/9883472/is-it-possible-to-have-an-unsigned-bytebuffer-in-java
// 15.11.2016 - tfischer - removed "put" interface (not needed), added support for inverted byte order.
public class Unsigned {
	public static short getU8(ByteBuffer bb) {
		return ((short) (bb.get() & 0xff));
	}

	public static short getU8(ByteBuffer bb, int position) {
		return ((short) (bb.get(position) & (short) 0xff));
	}

	// ---------------------------------------------------------------

	public static int getU16(ByteBuffer bb, boolean invertByteOrder) {
		if (invertByteOrder)
			return (Short.reverseBytes(bb.getShort()) & 0xffff);
		else
			return (bb.getShort() & 0xffff);
	}

	public static int getU16(ByteBuffer bb, int position, boolean invertByteOrder) {
		if (invertByteOrder)
			return (Short.reverseBytes(bb.getShort(position)) & 0xffff);
		else
			return (bb.getShort(position) & 0xffff);
	}

	// ---------------------------------------------------------------

	public static long getU32(ByteBuffer bb, boolean invertByteOrder) {
		if (invertByteOrder)
			return (Integer.reverseBytes(bb.getInt()) & 0xffffffffL);
		else
			return (bb.getInt() & 0xffffffffL);
	}

	public static long getU32(ByteBuffer bb, int position, boolean invertByteOrder) {
		if (invertByteOrder)
			return (Integer.reverseBytes(bb.getInt(position)) & 0xffffffffL);
		else
			return (bb.getInt(position) & 0xffffffffL);
	}

}
