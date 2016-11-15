package com.bicirikdwarf.elf;

import java.nio.ByteBuffer;

public class Sym {
	public int st_name;
	public int st_value;
	public int st_size;
	public byte st_info;
	public byte st_other;
	public short st_shndx;

	public void parse(ByteBuffer buffer, boolean invertByteOrder) {
		st_name = invertByteOrder ? Integer.reverseBytes(buffer.getInt()) : buffer.getInt();
		st_value = invertByteOrder ? Integer.reverseBytes(buffer.getInt()) : buffer.getInt();
		st_size = invertByteOrder ? Integer.reverseBytes(buffer.getInt()) : buffer.getInt();
		st_info = buffer.get();
		st_other = buffer.get();
		st_shndx = invertByteOrder ? Short.reverseBytes(buffer.getShort()) : buffer.getShort();
	}
}