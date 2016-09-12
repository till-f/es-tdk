package fzi.mottem.ptspec.compiler.util;

import fzi.mottem.ptspec.dsl.pTSpec.PTSImplementation;

public class PTSConcurrentStatementWrapper
{

	private final String _concurrentThreadId;
	PTSImplementation _implementation;
	
	public PTSConcurrentStatementWrapper(String concurrentThreadId, PTSImplementation implementation)
	{
		_concurrentThreadId = concurrentThreadId;
		_implementation = implementation;
	}

	public String getAsyncId()
	{
		return _concurrentThreadId;
	}

	public PTSImplementation getImplementation()
	{
		return _implementation;
	}

}
