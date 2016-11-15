package com.bicirikdwarf.elf;

import java.nio.ByteBuffer;

import com.bicirikdwarf.utils.Unsigned;

public class Nhdr {
	long n_namesz; // length of note's name - word
	long n_descsz; // length of note's "desc" - word
	long n_type; // type of note - word

	public void parse(ByteBuffer buffer, boolean invertByteOrder) {
		n_namesz = Unsigned.getU32(buffer, invertByteOrder);
		n_descsz = Unsigned.getU32(buffer, invertByteOrder);
		n_type = Unsigned.getU32(buffer, invertByteOrder);
	}
}