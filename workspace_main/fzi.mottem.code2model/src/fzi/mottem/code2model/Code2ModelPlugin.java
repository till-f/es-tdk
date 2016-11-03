package fzi.mottem.code2model;

import java.util.Hashtable;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.bicirikdwarf.emf.dwarf.DwarfModel;

public class Code2ModelPlugin extends AbstractUIPlugin implements IStartup, IResourceChangeListener
{
	public static final String PLUGIN_ID = "fzi.mottem.cdt2ecore"; //$NON-NLS-1$
	
	// this lock is used to ensure that only one cdt2ecore or elf2ecore job is run at once (each of them modifies the CodeModel files)
	// it is ok to use one lock for all model files because at most a few exist in the workspace (normally exactly one). 
	public static final Object GLOBAL_CODE_MODEL_LOCK = new Object();

	public static Code2ModelPlugin Instance = null;
	
	private final Hashtable<String, DwarfModel> _elfFileToDwarfMap = new Hashtable<String, DwarfModel>();
	
	public Hashtable<String, DwarfModel> getElfFileToDwarfMap()
	{
		return _elfFileToDwarfMap;
	}

	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		
		Instance = this;
	}

	public void stop(BundleContext context) throws Exception 
	{
		Instance = null;
		
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		
		super.stop(context);
	}
	
    @Override
	public void earlyStartup()
	{
		// Nothing to do. This plugin does not show a different behavior
		// when it is started using "early startup", thus there is no need to
		// distinguish between normal and early startup.
    	
    	// Nevertheless deriving from IStartup is needed, so that this plugin
    	// can register a handler for workspace changes (triggering re-load of code models)
		
		System.out.println("Successful early startup of Code2Model plugin.");
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
					Code2ModelResourceDeltaVisitor changeVisitor = new Code2ModelResourceDeltaVisitor();
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
