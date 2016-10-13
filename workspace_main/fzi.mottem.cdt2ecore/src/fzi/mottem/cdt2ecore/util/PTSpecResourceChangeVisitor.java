package fzi.mottem.cdt2ecore.util;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;

/**
 * This ChangeVisitor reacts on changes in C/C++ files or ELF files and
 * updates EMF models containing symbol information.
 * NOTE: Current implementation is very inefficient.
 */
public class PTSpecResourceChangeVisitor implements IResourceDeltaVisitor
{

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException
	{
		IProject project = delta.getResource().getProject();
		
		if (project == null)
			return true;
		
		if (!project.isOpen())
			return false;
		
		if (CoreModel.hasCNature(project) || CoreModel.hasCCNature(project))
		{
			switch (delta.getKind())
			{
				case IResourceDelta.CHANGED:
				case IResourceDelta.ADDED:
					// TODO: Combine options 1+2 and improve efficiency of option 1 (only process changed resoures)

					// option 1: extract symbols from C/C++ code using CDT.
					LinkedList<IProject> changedCProjects = new LinkedList<IProject>();
					changedCProjects.add(project);
					startCDT2EcoreJobForAllPTSProjects(changedCProjects);

					// option 2: extract symbols from ELF file with addresses.
					return false;
				default:
					return false;
			}
		}

		return false;
	}
	
    /**
     * update CodeModel files corresponding to provided list of C/C++ projects
     * in all projects having PTSpec Nature.
     */
    private void startCDT2EcoreJobForAllPTSProjects(Collection<IProject> changedCProjects)
    {
    	for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
    	{
    		if (!project.isOpen())
    			continue;
    		
    		try
    		{
				if (project.hasNature(PTSpecNature.NATURE_ID))
				{
					CDT2EcoreJob.startCDT2EcoreJob(project, changedCProjects);
				}
			}
    		catch (CoreException e)
    		{
				e.printStackTrace();
			}
    	}
    }
}
