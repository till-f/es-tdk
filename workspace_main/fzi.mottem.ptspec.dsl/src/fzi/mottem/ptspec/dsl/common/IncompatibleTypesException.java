package fzi.mottem.ptspec.dsl.common;

public class IncompatibleTypesException extends Exception
{
	private static final long serialVersionUID = -2048183014082872373L;
	
	private final String _reason;

	public IncompatibleTypesException(String reason)
	{
		_reason = reason;
	}

	public String getReason()
	{
		return _reason;
	}
}
