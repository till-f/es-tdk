package fzi.mottem.code2model.cdt2ecore;

import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class CDTExtractorJob extends Job
{

	private final IProject _ptsProject;
	private final Collection<IProject> _changedCProjects;
	private final Hashtable<IProject, CDTExtractor> _cdt2EcoreWorkers = new Hashtable<IProject, CDTExtractor>();
	
    /**
     * update CodeModel files corresponding to provided list of C/C++ projects
     * in the provided project (should have PTSpecNature, but this is not ensured).
     */
    public static void startCDT2EcoreJob(IProject ptsProject, Collection<IProject> changedCProjects)
    {
		CDTExtractorJob cdt2EcoreJob = new CDTExtractorJob(ptsProject, changedCProjects);
		cdt2EcoreJob.setPriority(Job.SHORT);
		cdt2EcoreJob.schedule(10);
    }

	public CDTExtractorJob(IProject ptsProject, Collection<IProject> changedCProjects)
	{
		super("Refreshing CodeModel(s) for " + ptsProject.getName());
		
		_ptsProject = ptsProject;
		_changedCProjects = changedCProjects;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
    	IFolder modelFolder;
		try
		{
			modelFolder = _ptsProject.getFolder("model");
			
			if (!modelFolder.exists())
			{
				return Status.CANCEL_STATUS;
			}
		}
		catch (Exception e)
		{
			System.err.println("Cannot refresh CodeModel instances: failure getting active resource: " + e.getMessage());
			return Status.CANCEL_STATUS;
		}

        for (IProject p: _changedCProjects)
        {
            try
            {
            	CDTExtractor cdti;
            	
            	if (_cdt2EcoreWorkers.containsKey(p))
            	{
            		cdti = _cdt2EcoreWorkers.get(p);
            	}
            	else
            	{
                    cdti = new CDTExtractor(p);
                    _cdt2EcoreWorkers.put(p, cdti);
            	}

            	cdti.refresh(modelFolder);
            }
            catch (CoreException e)
            {
                e.printStackTrace();
            }
        }
        
		return Status.OK_STATUS;
	}

}
