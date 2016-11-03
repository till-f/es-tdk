package fzi.mottem.runtime.rtgraph;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import fzi.mottem.runtime.rtgraph.commands.RefreshCommand;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup, IResourceChangeListener
{

	// The plug-in ID
	public static final String PLUGIN_ID = "fzi.mottem.runtime.rtgraph"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);

		UIJob job = new UIJob("(Re)-Load Signals") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				RefreshCommand cmd = new RefreshCommand();
				cmd.execute(null);
				return Status.OK_STATUS;
			}
		};
    	job.setPriority(Job.LONG);
    	job.schedule();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;

		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	
	@Override
	public void earlyStartup()
	{
		System.out.println("Successful early startup of RTGraph plugin.");
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		switch (event.getType())
		{
	        case IResourceChangeEvent.PRE_CLOSE:
	           return;
	        case IResourceChangeEvent.PRE_DELETE:
	        	return;
	        case IResourceChangeEvent.POST_CHANGE:
				try
				{
					RTGrahResourceDeltaVisitor changeVisitor = new RTGrahResourceDeltaVisitor();
					event.getDelta().accept(changeVisitor);
				}
				catch (CoreException e1)
				{
					e1.printStackTrace();
				}
				return;
	        case IResourceChangeEvent.PRE_BUILD:
	        	return;
	        case IResourceChangeEvent.POST_BUILD:
	        	return;
	        default:
	        	return;
		}
	}
}
