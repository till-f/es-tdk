package fzi.mottem.jjet.interfaces;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public interface IJJETContext
{
	public IJJETCompiler getCompiler();
	
	public Object getArgument();

	public IJJETTemplate getTemplate();

	public StringBuffer getBuffer();

	public void setBuffer(StringBuffer buffer);
	
	public IFile getOutputFile();

	public void setOutputFile(IFile file);

	public void setOutputFile(IPath path, String name);

	public void pushIndent(int nTabs);

	public void popIndent();

	public int getCurrentIndent();
}
