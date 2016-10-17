package fzi.mottem.code2model.cdt2ecore;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fzi.mottem.code2model.Code2ModelPlugin;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;

public class CDTExtractorJob extends Job
{
	private final IProject _cdtProject;

	/**
	 * TODO: This job is inefficient, because the complete model is generated every time.
	 * The job should only update the data associated with the appropriate file.
	 */
	public CDTExtractorJob(IProject cdtProject)
	{
		super("Extracting info from soruce code");
		
		_cdtProject = cdtProject;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		synchronized (Code2ModelPlugin.GLOBAL_CODE_MODEL_LOCK)
		{
	    	for (IProject ptsProject : ResourcesPlugin.getWorkspace().getRoot().getProjects())
	    	{
	    		if (!ptsProject.isOpen())
	    			continue;
	    		
	    		try
	    		{
					if (ptsProject.hasNature(PTSpecNature.NATURE_ID))
					{
						CDTExtractor extractor = new CDTExtractor(_cdtProject);
						IFolder modelFolder = ptsProject.getFolder(ModelUtils.PTS_MODEL_FILES_ROOT);
						extractor.extractInto(modelFolder);
					}
				}
	    		catch (CoreException e)
	    		{
					e.printStackTrace();
				}
	    	}
	
			return Status.OK_STATUS;
		}
	}

}
