package fzi.mottem.ptspec.dsl.generator;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

import fzi.mottem.ptspec.compiler.util.PTSCompileJob;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;

/**
 * Generates code from your model files on save.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#TutorialCodeGeneration
 */
public class PTSpecGenerator implements IGenerator
{

	@Override
	public void doGenerate(Resource resource, IFileSystemAccess fsa)
	{
		if (resource.getErrors().size() > 0)
			System.err.println("Automatic compilation of " + resource.getURI().toString() + " skipped du to errors");
		
		EList<EObject> fileContents = resource.getContents();
		
		if (fileContents.size() > 0)
		{
			PTSCompileJob compileJob = new PTSCompileJob((PTSRoot)fileContents.get(0));
			compileJob.setPriority(Job.SHORT);
			compileJob.schedule();
		}
	}

}
