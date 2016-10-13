package fzi.mottem.code2model;

import java.util.LinkedList;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import fzi.mottem.code2model.cdt2ecore.CDTExtractorJob;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;

/**
 * This IResourceDeltaVisitor reacts on changes in C/C++ files or binary files
 * (ELF files) and updates EMF models accordingly.
 * NOTE: Current implementation is poor and needs improvement.
 */
public class Code2ModelResourceDeltaVisitor implements IResourceDeltaVisitor
{
	private final static int OPTION = 1;

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException
	{
    	IProject project = delta.getResource().getProject();

    	if (project == null)
			return true;
		
		if (!project.isOpen())
			return false;
		
		if (!CoreModel.hasCNature(project) && !CoreModel.hasCCNature(project))
			return false;

		// TODO: Combine options 1+2 and improve efficiency of option 1
		//       (only process changed resoures). This requires architecture
		//       change (interface to emf is currently simply writing model files
		//       into file system - combination requires proper update of models)
		if (OPTION == 1)
			return option1(delta);
		else
			return option2(delta);
	}
	
	/**
	 * extract symbols from C/C++ code using CDT. Simply triggers CDTExtractor for every
	 * PTSpec project.
	 */
    private boolean option1(IResourceDelta delta)
    {
    	IProject project = delta.getResource().getProject();

    	switch (delta.getKind())
		{
			case IResourceDelta.CHANGED:
			case IResourceDelta.ADDED:
				LinkedList<IProject> changedCProjects = new LinkedList<IProject>();
				changedCProjects.add(project);
		    	for (IProject ptsProject : ResourcesPlugin.getWorkspace().getRoot().getProjects())
		    	{
		    		if (!ptsProject.isOpen())
		    			continue;
		    		
		    		try
		    		{
						if (ptsProject.hasNature(PTSpecNature.NATURE_ID))
						{
							CDTExtractorJob.startCDT2EcoreJob(ptsProject, changedCProjects);
						}
					}
		    		catch (CoreException e)
		    		{
						e.printStackTrace();
					}
		    	}
				return false;
			default:
				return false;
		}
	}

    /**
     * extract symbols from ELF file with addresses.
     */
	private boolean option2(IResourceDelta delta)
	{
		if (delta.getResource() == null ||
			delta.getResource().getFileExtension() == null ||
			!delta.getResource().getFileExtension().equals("elf"))
		{
			return true;
		}

		// found an updated ELF file
    	System.err.println("Option 2 currently not supported (ELF file " + delta.getResource().getFullPath().toPortableString() + " updated)");
		
		return false;
	}
}
