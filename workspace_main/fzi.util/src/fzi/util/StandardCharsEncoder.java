package fzi.util;

import java.util.Base64.Encoder;

public class StandardCharsEncoder
{
	private static final StandardCharsEncoder _encoder = new StandardCharsEncoder();
	
	private final Encoder _base64Encoder = java.util.Base64.getEncoder();
	
	public static StandardCharsEncoder getEncoder()
	{
		return _encoder;
	}
	
	public String encode(byte[] bytes)
	{
		String base64Str = _base64Encoder.encodeToString(bytes);
		return base64Str.replace('/', '.');
	}
}
