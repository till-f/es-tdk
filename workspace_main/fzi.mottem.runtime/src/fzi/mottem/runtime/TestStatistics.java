package fzi.mottem.runtime;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;

public class TestStatistics
{
	private int _infoCount = 0;
	private int _warningCount = 0;
	private int _errorCount = 0;
	private int _fatalCount = 0;
	
	public void notifyAssertion(FailedAssertion fa)
	{
		increment(fa.getSeverity());
	}
	public void notifyReport(ReportMessage rm)
	{
		increment(rm.getSeverity());
	}
	
	public int get_infoCount()
	{
		return _infoCount;
	}
	public int get_warningCount()
	{
		return _warningCount;
	}
	public int get_errorCount()
	{
		return _errorCount;
	}
	public int get_fatalCount()
	{
		return _fatalCount;
	}
	
	private void increment(PTS_ESEVERITY severity)
	{
		switch(severity)
		{
		case INFO:
			_infoCount++;
			break;
		case WARNING:
			_warningCount++;
			break;
		case ERROR:
			_errorCount++;
			break;
		case FATAL:
			_fatalCount++;
			break;
		}
	}
}