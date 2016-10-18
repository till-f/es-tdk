package fzi.mottem.ptspec.dsl.ui;

import org.osgi.framework.BundleContext;

import fzi.mottem.ptspec.dsl.ui.internal.PTSpecActivator;
import fzi.mottem.ptspec.runtestlistener.EvaluateContributionsHandler;
import fzi.mottem.ptspec.runtestlistener.IRunTestListener;

public class PTSpecActivatorCustom extends PTSpecActivator
{

	private final EvaluateContributionsHandler _contributionsHandler = new EvaluateContributionsHandler();

	public static PTSpecActivatorCustom getInstanceCustom()
	{
		return (PTSpecActivatorCustom)PTSpecActivator.getInstance();
	}
	
	/*
	 * Convenience function for EvaluateContributionsHandler.getRunTestListener()
	 */
	public IRunTestListener getRunTestListener()
	{
		return _contributionsHandler.getRunTestListener();
	}
	
	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		
		System.out.println("Successful early startup of PTSpec UI plugin.");
		
		_contributionsHandler.evaluate();
	}
	
}
