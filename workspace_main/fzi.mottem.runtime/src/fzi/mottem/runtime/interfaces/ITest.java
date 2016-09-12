package fzi.mottem.runtime.interfaces;

import java.util.ArrayList;

import fzi.mottem.runtime.ETestState;
import fzi.mottem.runtime.FailedAssertion;
import fzi.mottem.runtime.ReportMessage;
import fzi.mottem.runtime.TestStatistics;
import fzi.mottem.runtime.TraceDB;

public interface ITest extends Runnable, IReportSource
{
	public void init();
	
	public void cleanup();
	
	public void run();
	
	public String getName();
	
	public String getSourceFileUri();

	public String getProject();

	public int getSourceOffset();
	
	public int getSourceLength();

	public String getNextTraceName();

	public ArrayList<IFailedAssertion> getFailedAssertions();

	public ArrayList<IReportMessage> getReportMessages();

	public ArrayList<TraceDB> getTestPlots();

	public void addFailedAssertion(FailedAssertion fa);

	public void addReportMessage(ReportMessage rm);
	
	public ETestState getTestState();

	public TestStatistics getTestStatistics();

}
