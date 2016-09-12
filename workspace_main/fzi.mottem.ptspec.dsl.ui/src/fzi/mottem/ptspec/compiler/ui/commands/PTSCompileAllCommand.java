package fzi.mottem.ptspec.compiler.ui.commands;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import com.google.inject.Inject;

import fzi.mottem.ptspec.compiler.util.PTSCompileJob;
import fzi.mottem.ptspec.compiler.util.PTSCompilerUtil;
import fzi.mottem.ptspec.dsl.common.PTSpecConstants;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.util.eclipse.IntegrationUtils;

public class PTSCompileAllCommand extends AbstractHandler implements IJobChangeListener
{
    @Inject
    private IResourceSetProvider _resourceSetProvider;
    
    private IProject _project = null;
    
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		try
		{
			_project = IntegrationUtils.getActiveResource().getProject();
			LinkedList<PTSRoot> modelsToCompile = new LinkedList<PTSRoot>();

			for(IResource resource : IntegrationUtils.getResourcesOfProject(_project, PTSpecConstants.FILE_EXTENSION))
			{
				URI uri = URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
	            ResourceSet rs = _resourceSetProvider.get(_project);
	            Resource r = rs.getResource(uri, true);
	            
	            if (r.getErrors().size() > 0)
	            	continue;
	            
	            PTSRoot root = (PTSRoot)r.getContents().get(0);
	            
	            if (root == null)
	            	continue;
	            
	            modelsToCompile.add(root);
	        }

			PTSCompileJob compileJob = new PTSCompileJob(modelsToCompile);
			compileJob.setPriority(Job.BUILD);
			compileJob.addJobChangeListener(this);
			compileJob.schedule();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

		return null;
	}

	@Override
	public void aboutToRun(IJobChangeEvent event)
	{
	}

	@Override
	public void awake(IJobChangeEvent event)
	{
	}

	@Override
	public void done(IJobChangeEvent event)
	{
		List<IResource> generatedResources = ((PTSCompileJob)event.getJob()).getGeneratedResources();
		
		if (generatedResources.size() > 0)
			deleteUnwrittenResources(generatedResources, PTSCompilerUtil.getBaseOutputFolder(_project));
	}

	@Override
	public void running(IJobChangeEvent event)
	{
	}

	@Override
	public void scheduled(IJobChangeEvent event)
	{
	}

	@Override
	public void sleeping(IJobChangeEvent event)
	{
	}

	private void deleteUnwrittenResources(List<IResource> generatedResources, IFolder folder)
	{
		try
		{
			for (IResource resource : folder.members())
			{
				if (!generatedResources.contains(resource))
				{
					resource.delete(true, null);
				}
				else
				{
					if (resource instanceof IFolder)
						deleteUnwrittenResources(generatedResources, (IFolder)resource);
				}
			}
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}
	
}
