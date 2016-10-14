package fzi.mottem.code2model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import com.bicirikdwarf.dwarf.Dwarf32Context;
import com.bicirikdwarf.elf.Elf32Context;
import com.bicirikdwarf.emf.DwarfModelFactory;
import com.bicirikdwarf.emf.dwarf.DwarfModel;

import fzi.mottem.code2model.cdt2ecore.CDTExtractorJob;
import fzi.mottem.ptspec.dsl.ui.nature.PTSpecNature;
import fzi.util.eclipse.IntegrationUtils;

/**
 * This IResourceDeltaVisitor reacts on changes in C/C++ files or binary files
 * (ELF files) and updates EMF models accordingly.
 * NOTE: Current implementation is poor and needs improvement.
 */
public class Code2ModelResourceDeltaVisitor implements IResourceDeltaVisitor
{
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
		//return option1(delta);
		return option2(delta);
	}
	
	/**
	 * extract symbols from C/C++ code using CDT. Simply triggers CDTExtractor for every
	 * PTSpec project.
	 */
	@SuppressWarnings("unused")
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
		String elfPathStr = IntegrationUtils.getStringSystemPathForWorkspaceRelativePath(delta.getResource().getFullPath());
		File elfFile = new File(elfPathStr);

		RandomAccessFile raFile = null;
		DwarfModel model = null;
		try
		{
			raFile = new RandomAccessFile(elfFile, "r");
			FileChannel elfFileChannel = raFile.getChannel();
			MappedByteBuffer buffer = elfFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, elfFileChannel.size());
	    	Elf32Context elfContext = new Elf32Context(buffer);
	    	Dwarf32Context dwarfContext = new Dwarf32Context(elfContext);
	    	model = DwarfModelFactory.createModel(dwarfContext);
		}
		catch (IOException e)
		{
			return true;
		} 
		finally
		{
			try {
				raFile.close();
			} catch (IOException e1) {
				return true;
			}
		}

    	TreeIterator<EObject> iterator = model.eAllContents();
		while (iterator.hasNext())
		{
			EObject obj = iterator.next();
			//System.out.println(obj.getClass().getSimpleName());
			// !TODO: create and fill code model file
		}
		
		return false;
	}
}
