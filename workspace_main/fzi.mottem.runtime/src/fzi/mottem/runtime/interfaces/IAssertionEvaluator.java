package fzi.mottem.runtime.interfaces;

public interface IAssertionEvaluator
{
	
	/*
	 * Used to report a failed assertion and adds assertion object to test execution protocol.
	 */
	public void assertionFailed(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, int severity);
	
	/*
	 * Used to report a failed assertion and adds assertion object to test execution protocol with additional message.
	 */
	public void assertionFailed(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, int severity, String message);

	/*
	 * Used to report a failed assertion and adds assertion object to test execution protocol with additional message and context.
	 */
	public void assertionFailed(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, int severity, String message, Object context);

}
