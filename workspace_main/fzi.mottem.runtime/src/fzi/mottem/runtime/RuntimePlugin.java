package fzi.mottem.runtime;

import java.util.Hashtable;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class RuntimePlugin extends AbstractUIPlugin
{
	public static final String PLUGIN_ID = "fzi.mottem.runtime"; //$NON-NLS-1$

	public static RuntimePlugin Instance = null;
	
	private final Hashtable<IProject, Runtime> _runtimeMap = new Hashtable<IProject, Runtime>();
	public Runtime getRuntime(IProject project)
	{
		if (_runtimeMap.containsKey(project))
		{
			return _runtimeMap.get(project);
		}
		else
		{
			Runtime r = new Runtime(project);
			_runtimeMap.put(project, r);
			return r;
		}
	}

	public RuntimePlugin()
	{
	}
	
	@Override
	public void start(BundleContext context) throws Exception
	{
		Instance = this;

		super.start(context);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);

		Instance = null;
	}
}
