package fzi.mottem.jjet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import fzi.mottem.jjet.interfaces.ICompileListener;
import fzi.mottem.jjet.interfaces.IJJETCompiler;
import fzi.mottem.jjet.interfaces.IJJETContext;
import fzi.mottem.jjet.interfaces.IJJETTemplate;
import fzi.util.StringUtils;

public class JJETCompiler implements IJJETCompiler
{
    private final String NL = System.getProperties().getProperty("line.separator");
	private final Set<ICompileListener> _compileListeners = new LinkedHashSet<ICompileListener>();
	private final IFolder _baseFolder;
	
	@Override
	public IFolder getBaseFolder()
	{
		return _baseFolder;
	}

	public JJETCompiler(IFolder baseFolder)
	{
		_baseFolder = baseFolder;
	}
	
	@Override
	public void compile(IJJETTemplate template, Object argument, IJJETContext existingContext)
	{
		if (existingContext != null)
		{
			// generated text is a fragment triggered by another template. Is not generated into separate file but into buffer of parent
			if (existingContext.getBuffer() == null)
				throw new RuntimeException("No buffer in existing context. Ensure that buffer is registered to the context at the beginning of every template.");
			
			// generate fragment
			JJETContext context = new JJETContext(template, this, argument);
			String fragment = template.generate(context);
			
			// indent as desired and add fragment to existing buffer
			String fragment_trimmed = fragment.trim();
			String fragment_indent = StringUtils.indentTabs(fragment_trimmed, existingContext.getCurrentIndent());
			existingContext.getBuffer().append(NL);
			existingContext.getBuffer().append(fragment_indent);
		}
		else
		{
			// text is generated into new file
			JJETContext context = new JJETContext(template, this, argument);
			String text = template.generate(context);
			flushToFile(text, context);
		}
	}
	
	@Override
	public void resourceGenerated(IResource resource)
	{
		for (ICompileListener listener : _compileListeners)
		{
			listener.resourceGenerated(resource);
		}
	}
	
	@Override
	public void addCompileListener(ICompileListener listener)
	{
		_compileListeners.add(listener);
	}

	@Override
	public void removeCompileListener(ICompileListener listener)
	{
		_compileListeners.remove(listener);
	}
	
	private void flushToFile(String text, JJETContext context)
	{
		IFile outFile = context.getOutputFile();

		if (outFile == null)
			return;
		
		try 
		{
			InputStream inStream = new ByteArrayInputStream(text.getBytes());

			if (outFile.exists())
				outFile.setContents(inStream, true, false, null);
			else
				outFile.create(inStream, IResource.NONE, null);
			
			context.getCompiler().resourceGenerated(outFile);
		} 
		catch (CoreException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
}
