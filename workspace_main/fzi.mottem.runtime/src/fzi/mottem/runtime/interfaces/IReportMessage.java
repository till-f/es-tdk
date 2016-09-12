package fzi.mottem.runtime.interfaces;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;

public interface IReportMessage
{
	public String getSourceEcoreURI();
	public String getMessage();
	public Object getContext();
	public int getOffset();
	public int getLength();
	public String getTimestamp();
	public PTS_ESEVERITY getSeverity();
}
