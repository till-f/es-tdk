package fzi.mottem.runtime;

import java.util.ArrayList;

import fzi.mottem.runtime.interfaces.IFailedAssertion;
import fzi.mottem.runtime.interfaces.IReportMessage;
import fzi.mottem.runtime.interfaces.ITest;

public abstract class AbstractTest implements ITest
{
	// List of plots that have been drawn in the context of this test
	protected final ArrayList<TraceDB> _pts_plots = new ArrayList<TraceDB>();

	// List of assertions that have been violated in the context of this test
	private final ArrayList<IFailedAssertion> _pts_failedAssertions = new ArrayList<IFailedAssertion>();

	// List of assertions that have been violated in the context of this test
	private final ArrayList<IReportMessage> _pts_reportMessages = new ArrayList<IReportMessage>();
	
	private final TestStatistics _stats = new TestStatistics();

	private int traceIdx = 0;
	@Override
	public String getNextTraceName()
	{
		return this.getName() + "_" + traceIdx++;
	}
	
	@Override
	public ArrayList<TraceDB> getTestPlots()
	{
		return _pts_plots;
	}
	
	@Override
	public ArrayList<IFailedAssertion> getFailedAssertions() 
	{
		return _pts_failedAssertions;
	}
	
	@Override
	public ArrayList<IReportMessage> getReportMessages()
	{
		return _pts_reportMessages;
	}
	
	@Override
	public void addFailedAssertion(FailedAssertion fa)
	{
		synchronized(messageLock)
		{
			_pts_failedAssertions.add(fa);
		  	_stats.notifyAssertion(fa);
		}
	}
	
	Object messageLock = new Object();

	@Override
	public void addReportMessage(ReportMessage rm)
	{
		synchronized(messageLock)
		{
			_pts_reportMessages.add(rm); //RACE CONDITIONS ALSO HERE!!!
			_stats.notifyReport(rm);
		} 
		
	}
	
	
	@Override
	public ETestState getTestState()
	{
		if (_stats.get_errorCount() > 0 || _stats.get_fatalCount() > 0)
			return ETestState.Failed;
		else if (_stats.get_warningCount() > 0)
			return ETestState.Indecisive;
		else
			return ETestState.Passed;
	}
	
	@Override
	public TestStatistics getTestStatistics()
	{
		return _stats;
	}
	
}
