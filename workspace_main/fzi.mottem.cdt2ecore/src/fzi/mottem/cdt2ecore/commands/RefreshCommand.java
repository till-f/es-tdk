package fzi.mottem.cdt2ecore.commands;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import fzi.mottem.cdt2ecore.util.CDT2EcoreJob;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;
import fzi.util.eclipse.IntegrationUtils;

public class RefreshCommand extends AbstractHandler 
{

	@Override
	public Object execute(ExecutionEvent event) 
	{
    	try
    	{
			IProject ptsProject = IntegrationUtils.getActiveResource().getProject();

    		if (ptsProject == null || !ptsProject.isOpen())
    		{
    			System.err.println("Cannot update CodeModels: no viable project in scope");
    		}
    		else if (ptsProject.hasNature(PTSpecNature.NATURE_ID))
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
				
				// option 1
				CDT2EcoreJob.startCDT2EcoreJob(ptsProject, cProjects);
			}
		}
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    	
    	return null;
	}

}
