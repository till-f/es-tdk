package fzi.mottem.ptspec.compiler.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;

import fzi.mottem.jjet.JJETCompiler;
import fzi.mottem.jjet.interfaces.ICompileListener;
import fzi.mottem.ptspec.compiler.precompiled.JET_Main;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.util.ecore.EcoreUtils;

public class PTSCompileJob extends Job  implements ICompileListener
{

	private final List<PTSRoot> _modelsToCompile;
	
	/*
	 * A list of generated files. If any compiler instance generates a file it will be added
	 * to this list. It is used when the method compileAll() is called, wich will afterwords
	 * delete all files in the base output folder that have not been added to the list.
	 * Because multiple compiler might be running, this list is wrapped by a synchronized
	 * List in order to guarantee thread safety. 
	 */
	private final List<IResource> _generatedResources = Collections.synchronizedList(new LinkedList<IResource>());
	public List<IResource> getGeneratedResources()
	{
		return _generatedResources;
	}

	public PTSCompileJob(PTSRoot modelToCompile)
	{
		super("Compile PTSpec resource");

		_modelsToCompile = new LinkedList<PTSRoot>();
		_modelsToCompile.add(modelToCompile);
	}

	public PTSCompileJob(LinkedList<PTSRoot> modelsToCompile)
	{
		super("Compile PTSpec resource(s)");

		_modelsToCompile = modelsToCompile;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		monitor.beginTask("Compiling", _modelsToCompile.size());

		int workDone = 0;

		for (PTSRoot modelToCompile : _modelsToCompile)
		{
			try
			{
				URI resourceUri = modelToCompile.eResource().getURI();
				IFile file = EcoreUtils.getFileForEMFURI2(resourceUri);

				monitor.subTask(file.getProjectRelativePath().toString());
				
				JJETCompiler compiler = new JJETCompiler(PTSCompilerUtil.getBaseOutputFolder(file.getProject()));
				compiler.addCompileListener(this);
				
				compiler.compile(new JET_Main(), modelToCompile, null);
			}
			finally
			{
				workDone++;
				monitor.worked(workDone);
			}
		}
		
		return Status.OK_STATUS;
	}
	
	@Override
	public void resourceGenerated(IResource resource)
	{
		_generatedResources.add(resource);
	}
}
