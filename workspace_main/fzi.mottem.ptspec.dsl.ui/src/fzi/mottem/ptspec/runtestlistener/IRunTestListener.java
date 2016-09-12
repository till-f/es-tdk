package fzi.mottem.ptspec.runtestlistener;

import org.eclipse.core.resources.IProject;

public interface IRunTestListener
{
	
	public void runTest(IProject project, String className);

}
