package fzi.mottem.runtime.interfaces;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;

public interface IFailedAssertion
{
	public String getSourceEcoreURI();
	public String getMessage();
	public String getPtsCode();
	public Object getContext();
	public int getOffset();
	public int getLength();
	public int getLine();
	public String getTimestamp();
	public PTS_ESEVERITY getSeverity();
}
