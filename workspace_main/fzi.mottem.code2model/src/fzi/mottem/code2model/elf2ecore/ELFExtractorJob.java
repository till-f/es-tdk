package fzi.mottem.code2model.elf2ecore;

import java.util.LinkedList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fzi.mottem.code2model.Code2ModelPlugin;
import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.util.ModelUtils;

public class ELFExtractorJob extends Job
{
	private final IResource _elfFile;

	public ELFExtractorJob(IResource elfFile)
	{
		super("Extracting info from ELF file");
		
		_elfFile = elfFile;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		synchronized (Code2ModelPlugin.GLOBAL_CODE_MODEL_LOCK)
		{
			String fullELFPathStr = _elfFile.getFullPath().toPortableString();
			
			LinkedList<CodeInstance> relevantCodeInstances = new LinkedList<CodeInstance>();
			for (CodeInstance ci : ModelUtils.getAllCodeModelsInWorkspace())
			{
				if (fullELFPathStr.endsWith(ci.getBinaryFile()))
				{
					relevantCodeInstances.add(ci);
				}
			}
			
			if (relevantCodeInstances.isEmpty())
				return Status.OK_STATUS;
			
			ELFExtractor extractor = new ELFExtractor(_elfFile);
			for (CodeInstance ci : relevantCodeInstances)
			{
				extractor.extractInto(ci);
			}

			return Status.OK_STATUS;
		}
	}

}