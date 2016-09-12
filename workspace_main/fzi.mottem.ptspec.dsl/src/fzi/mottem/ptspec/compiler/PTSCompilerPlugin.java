package fzi.mottem.ptspec.compiler;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class PTSCompilerPlugin extends AbstractUIPlugin 
{
	
	public static final String PLUGIN_ID = "fzi.mottem.ptspec.compiler"; //$NON-NLS-1$

	public static PTSCompilerPlugin Instance = null;
	
	private PTSCompilerSettings _settings;
	public PTSCompilerSettings getSettings()
	{
		return _settings;
	}
	
	public PTSCompilerPlugin() 
	{
	}

	public void start(BundleContext context) throws Exception 
	{
		_settings = new PTSCompilerSettings();
        
		Instance = this;
		
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception 
	{
		super.stop(context);

		Instance = null;
		
        _settings = null;
	}

//	@Override
//	public void resourceChanged(IResourceChangeEvent event)
//	{
//		List<IResource> resourcesToCompile;
//		switch (event.getType())
//		{
//	        case IResourceChangeEvent.PRE_CLOSE:
//	           return;
//	        case IResourceChangeEvent.PRE_DELETE:
//	        	return;
//	        case IResourceChangeEvent.POST_CHANGE:
//				try
//				{
//					resourcesToCompile = new LinkedList<IResource>();
//					event.getDelta().accept(new PTSResourceChangeVisitor(resourcesToCompile));
//					PTSCompileJob compileJob = new PTSCompileJob(resourcesToCompile, true);
//					compileJob.setPriority(Job.SHORT);
//					compileJob.schedule(10);
//				}
//				catch (CoreException e1)
//				{
//					e1.printStackTrace();
//				}
//				return;
//	        case IResourceChangeEvent.PRE_BUILD:
//	        	return;
//	        case IResourceChangeEvent.POST_BUILD:
//	        	return;
//	        default:
//	        	return;
//		}
//	}
	
}