package fzi.mottem.runtime.interfaces;

public interface IReportHandler
{
	
	/*
	 * Reports a message to the log.
	 */
	public void report(IReportSource test, String sourceEcoreURI, int offset, int length, int severity, String message);

	/*
	 * Reports a message to the log with additional context.
	 */
	public void report(IReportSource test, String sourceEcoreURI, int offset, int length, int severity, String message, Object context);
	
}
