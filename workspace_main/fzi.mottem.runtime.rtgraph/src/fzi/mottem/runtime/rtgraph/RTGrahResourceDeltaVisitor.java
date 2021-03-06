package fzi.mottem.runtime.rtgraph;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;

import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;
import fzi.mottem.runtime.rtgraph.commands.RefreshCommand;

public class RTGrahResourceDeltaVisitor implements IResourceDeltaVisitor
{
	private static UIJob REFRESH_SIGNALS_JOB = new UIJob("(Re)-Load Signals") {
		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			RefreshCommand cmd = new RefreshCommand();
			cmd.execute(null);
			return Status.OK_STATUS;
		}
	};
	
	public final static String[] MODEL_FILE_EXTENSIONS = {
			ModelUtils.FILE_EXTENSION_TESTRIG_MODEL, 
			ModelUtils.FILE_EXTENSION_CODE_MODEL,
			ModelUtils.FILE_EXTENSION_ND_MODEL,
			ModelUtils.FILE_EXTENSION_ED_MODEL
		};

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException
	{
    	IProject project = delta.getResource().getProject();

    	if (project == null)
			return true;
		
		if (!project.isOpen())
			return false;
		
		if (!project.hasNature(PTSpecNature.NATURE_ID))
			return false;
		
    	switch (delta.getKind())
		{
			case IResourceDelta.CHANGED:
				// only react on changes that actually cause different file content
				if ((delta.getFlags() & IResourceDelta.CONTENT) == IResourceDelta.CONTENT ||
					(delta.getFlags() & IResourceDelta.REPLACED) == IResourceDelta.REPLACED)
				{
					handleChange(delta);
				}
				return true;

			case IResourceDelta.ADDED:
			case IResourceDelta.REMOVED:
				handleChange(delta);
				
			default:
				return true;
		}
	}
	
	
	private void handleChange(IResourceDelta delta)
	{
		String fileExtension = delta.getResource().getFileExtension();
		if (fileExtension == null)
		{
			return;
		}
		else if (ArrayUtils.contains(MODEL_FILE_EXTENSIONS, fileExtension.toLowerCase()))
		{
			REFRESH_SIGNALS_JOB.setPriority(Job.LONG);
			REFRESH_SIGNALS_JOB.schedule(1000);
		}
	}
}
