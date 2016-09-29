package fzi.mottem.runtime;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;
import fzi.mottem.runtime.interfaces.IFailedAssertion;

public class FailedAssertion implements IFailedAssertion
{

	String _sourceEcoreURI;
	String _timestamp;
	int _offset;
	int _length;
	int _lineNr;
	String _ptsCode;
	String _message;
	Object _context;
	PTS_ESEVERITY _severity;
	
	public FailedAssertion(String timestamp,String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, PTS_ESEVERITY severity, String message, Object context)
	{
		_timestamp = timestamp;
		_sourceEcoreURI = sourceEcoreURI;
		_offset = offset;
		_length = length;
		_lineNr = lineNr;
		_ptsCode = ptsCode;
		_message = message;
		_context = context;
		_severity = severity;
	}

	@Override
	public String getMessage() {
		return _message;
	}

	@Override
	public String getPtsCode() {
		return _ptsCode;
	}

	@Override
	public Object getContext() {
		return _context;
	}

	@Override
	public int getOffset() {
		return _offset;
	}

	@Override
	public int getLength() {
		return _length;
	}
	
	@Override
	public int getLine() {
		return _lineNr;
	}
	
	@Override 
	public String getTimestamp() {
		return _timestamp;
	}

	@Override
	public String getSourceEcoreURI() {
		return _sourceEcoreURI;
	}

	@Override
	public PTS_ESEVERITY getSeverity() {
		return _severity;
	}
	
}
