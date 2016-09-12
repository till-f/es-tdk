package fzi.mottem.ptspec.runtestlistener;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class EvaluateContributionsHandler
{
	private static final String RUNTESTLISTENER_ID = "fzi.mottem.ptspec.dsl.ui.runtestlistener";
	
	private IRunTestListener _runTestListener = null;
	
	public IRunTestListener getRunTestListener()
	{
		return _runTestListener;
	}
		  
    public void evaluate()
    {
    	IExtensionRegistry registry = Platform.getExtensionRegistry();
    	
		IConfigurationElement[] config = registry.getConfigurationElementsFor(RUNTESTLISTENER_ID);
	    try
	    {
			for (IConfigurationElement e : config)
			{
				final Object o = e.createExecutableExtension("class");
				
				if (o instanceof IRunTestListener)
				{
					executeExtension((IRunTestListener) o);
				}
			}
	    }
	    catch (CoreException ex)
	    {
	    	System.out.println(ex.getMessage());
	    }
    }

	private void executeExtension(IRunTestListener testListener)
	{
		if (_runTestListener != null)
			System.err.println("Run Test Listener already registered; there may be only one.");
			
		_runTestListener = testListener;
	}
  
}
