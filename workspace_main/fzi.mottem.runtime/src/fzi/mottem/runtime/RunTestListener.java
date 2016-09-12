package fzi.mottem.runtime;

import java.net.MalformedURLException;
import java.util.LinkedList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaModelException;

import fzi.mottem.ptspec.runtestlistener.IRunTestListener;
import fzi.mottem.runtime.interfaces.ITest;
import fzi.mottem.runtime.util.PTSpecReflector;
import fzi.mottem.runtime.util.PTSpecRunTestsJob;

public class RunTestListener implements IRunTestListener
{

	@Override
	public void runTest(IProject project, String className)
	{
		PTSpecReflector reflector;
		try
		{
			reflector = new PTSpecReflector(project);
		}
		catch (JavaModelException | MalformedURLException e)
		{
			e.printStackTrace();
			return;
		}

		Runtime runtime = RuntimePlugin.Instance.getRuntime(project);
		
		LinkedList<Class<ITest>> testClasses = new LinkedList<Class<ITest>>();
		testClasses.add(reflector.getTestClass(className));
		PTSpecRunTestsJob runTestsJob = new PTSpecRunTestsJob(runtime, testClasses);
		runTestsJob.setPriority(Job.BUILD);
		runTestsJob.schedule();
	}

}
