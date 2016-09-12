package fzi.mottem.cdt2ecore;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;

import fzi.mottem.cdt2ecore.util.CDT2EcoreJob;
import fzi.mottem.cdt2ecore.util.PTSpecResourceChangeVisitor;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;
import fzi.util.eclipse.IntegrationUtils;

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
     * update all CodeModel files in the project that is currently in focus if
     * it has PTSpecNature.
     */
    public void updateCurrentPTSProject()
    {
    	try
    	{
			IProject ptsProject = IntegrationUtils.getActiveResource().getProject();

    		if (ptsProject == null || !ptsProject.isOpen())
    		{
    			System.err.println("Cannot update CodeModels: no viable project in scope");
    			return;
    		}

    		if (ptsProject.hasNature(PTSpecNature.NATURE_ID))
			{
				IWorkspace ws = ResourcesPlugin.getWorkspace();
				Collection<IProject> cProjects = new LinkedList<IProject>();
				for (IProject wsProject : ws.getRoot().getProjects())
				{
					if (CoreModel.hasCNature(wsProject) || CoreModel.hasCCNature(wsProject))
					{
						cProjects.add(wsProject);
					}
				}
				updatePTSProject(ptsProject, cProjects);
			}
		}
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    }

    /**
     * update CodeModel files corresponding to provided list of C/C++ projects
     * in all projects having PTSpec Nature.
     */
    public void updateAllPTSProjects(Collection<IProject> changedCProjects)
    {
    	for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
    	{
    		if (!project.isOpen())
    			continue;
    		
    		try
    		{
				if (project.hasNature(PTSpecNature.NATURE_ID))
				{
					updatePTSProject(project, changedCProjects);
				}
			}
    		catch (CoreException e)
    		{
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * update CodeModel files corresponding to provided list of C/C++ projects
     * in the provided project (should have PTSpecNature, but this is not ensured).
     */
    public void updatePTSProject(IProject ptsProject, Collection<IProject> changedCProjects)
    {
		CDT2EcoreJob cdt2EcoreJob = new CDT2EcoreJob(ptsProject, changedCProjects);
		cdt2EcoreJob.setPriority(Job.SHORT);
		cdt2EcoreJob.schedule(10);
    }

    /**
     * checks delta of ResourceChangeEvent; if anything has changed in a C/C++ project,
     * trigger reload of corresponding code model(s)
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
					Collection<IProject> changedCProjects = changeVisitor.getChangedCProjects();
					updateAllPTSProjects(changedCProjects);
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
