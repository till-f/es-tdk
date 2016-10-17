package fzi.mottem.code2model;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;

import fzi.mottem.code2model.cdt2ecore.CDTExtractorJob;
import fzi.mottem.code2model.elf2ecore.ELFExtractorJob;

/**
 * This IResourceDeltaVisitor reacts on changes in C/C++ files or binary files
 * (ELF files) and updates EMF models accordingly.
 * 
 * The CDTExtractor should extract all information (source code location, data type etc.)
 * and ELFExtractor should only extend this information by the memory address of the symbol.
 * 
 * The current implementation of the CDTExtractor is imcomplete, so that additional symbols
 * are found by the ELFExtractor. If adding these symbols is easy, this is done. However, in
 * this case no source code location is available.  
 */
public class Code2ModelResourceDeltaVisitor implements IResourceDeltaVisitor
{
	private final static String[] C_FILE_EXTENSIONS = {"c", "cpp", "hpp", "c++", "h++"};   // use lower case only
	
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
		
    	switch (delta.getKind())
		{
			case IResourceDelta.CHANGED:
			case IResourceDelta.ADDED:
				String fileExtension = delta.getResource().getFileExtension();
				if (fileExtension == null)
				{
					return true;
				}
				else if (ArrayUtils.contains(C_FILE_EXTENSIONS, fileExtension.toLowerCase()))
				{
					return parseCFileAndAddToModel(delta);
				}
				else if (fileExtension.equals("elf"))
				{
					return parseElfAndAddToModel(delta);
				}
				return true;
				
			default:
				return true;
		}

	}
	
	/**
	 * Extracts symbols from C/C++ code using CDT. Uses CDTExtractor for every
	 * PTSpec project.
	 */
	private boolean parseCFileAndAddToModel(IResourceDelta delta)
    {
    	CDTExtractorJob job = new CDTExtractorJob(delta.getResource().getProject());
    	job.setPriority(Job.LONG);
    	job.schedule();

		return false;
	}

    /**
     * Extracts symbols from ELF file. Adds these symbols to CodeModel(s) which have their
     * binaryFile attribute set to their ELF file. 
     */
	private boolean parseElfAndAddToModel(IResourceDelta delta)
	{
    	ELFExtractorJob job = new ELFExtractorJob(delta.getResource());
    	job.setPriority(Job.BUILD);
    	job.schedule();

		return false;
	}
}
