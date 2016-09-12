package fzi.mottem.runtime;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;
import fzi.mottem.runtime.interfaces.IReportMessage;

public class ReportMessage implements IReportMessage
{
	String _sourceEcoreURI;
	int _offset;
	int _length;
	String _message;
	Object _context;
	String _timestamp;
	PTS_ESEVERITY _severity;
	
	public ReportMessage(String timestamp, String sourceEcoreURI, int offset, int length, PTS_ESEVERITY severity, String message, Object context)
	{
		_timestamp = timestamp;
		_sourceEcoreURI = sourceEcoreURI;
		_offset = offset;
		_length = length;
		_message = message;
		_context = context;
		_severity = severity;
	}

	@Override
	public String getSourceEcoreURI() {
		return _sourceEcoreURI;
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
	public String getMessage() {
		return _message;
	}

	@Override
	public Object getContext() {
		return _context;
	}
	
	@Override
	public String getTimestamp() {
		return _timestamp;
	}
	
	@Override
	public PTS_ESEVERITY getSeverity() {
		return _severity;
	}
}
