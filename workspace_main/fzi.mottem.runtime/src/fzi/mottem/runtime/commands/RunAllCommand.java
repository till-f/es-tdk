package fzi.mottem.runtime.commands;

import java.net.MalformedURLException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaModelException;

import fzi.mottem.runtime.Runtime;
import fzi.mottem.runtime.RuntimePlugin;
import fzi.mottem.runtime.util.PTSpecReflector;
import fzi.mottem.runtime.util.PTSpecRunTestsJob;
import fzi.util.eclipse.IntegrationUtils;

public class RunAllCommand extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		try
		{
			runAll(IntegrationUtils.getActiveResource().getProject());
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		
		return null;
	}

	private void runAll(IProject project)
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
		
		PTSpecRunTestsJob runTestsJob = new PTSpecRunTestsJob(runtime, reflector.getTestClasses());
		runTestsJob.setPriority(Job.BUILD);
		runTestsJob.schedule();
	}

}
