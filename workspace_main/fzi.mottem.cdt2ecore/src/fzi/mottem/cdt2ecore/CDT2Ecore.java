package fzi.mottem.cdt2ecore;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import fzi.mottem.cdt2ecore.util.PTSpecResourceChangeVisitor;

public class CDT2Ecore implements IResourceChangeListener
{
    
    private CDT2EcorePlugin _activator = null;
    public CDT2EcorePlugin getActivator()
    {
        return _activator;
    }
    
    /**
     * The Constructor
     */
    public CDT2Ecore(CDT2EcorePlugin a)
    {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    /**
     * Kind-of destructor (must be called when not needed anymore)
     */
    public void shutdown()
    {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }
    
    /**
     * this checks for any change in projects with C/C++ nature and re-generates
     * CodeModel(s). Current implementation is very inefficient but OK for the moment.
     */
    @Override
    public void resourceChanged(IResourceChangeEvent event)
    {
		switch (event.getType())
		{
	        case IResourceChangeEvent.PRE_CLOSE:
	           return;
	        case IResourceChangeEvent.PRE_DELETE:
	        	return;
	        case IResourceChangeEvent.POST_CHANGE:
				try
				{
					PTSpecResourceChangeVisitor changeVisitor = new PTSpecResourceChangeVisitor();
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
