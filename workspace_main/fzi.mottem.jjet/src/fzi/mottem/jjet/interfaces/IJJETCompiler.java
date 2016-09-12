package fzi.mottem.jjet.interfaces;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;

public interface IJJETCompiler
{
	public void compile(IJJETTemplate template, Object argument, IJJETContext existingContext);
	
	public void resourceGenerated(IResource resource);
	
	public void addCompileListener(ICompileListener listener);
	
	public void removeCompileListener(ICompileListener listener);

	public IFolder getBaseFolder();
}
