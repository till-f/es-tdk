package fzi.mottem.jjet;

import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import fzi.mottem.jjet.interfaces.IJJETCompiler;
import fzi.mottem.jjet.interfaces.IJJETContext;
import fzi.mottem.jjet.interfaces.IJJETTemplate;

/*
 * This provides custom context information (can be seen as replacement of JET2Context).
 * Unfortunately JET2Context is final (why?!!!), thus we cannot extend that class.
 */
public class JJETContext implements IJJETContext
{
	private final Stack<Integer> _indentionStack = new Stack<Integer>();
	private final IJJETTemplate _template;
	private final IJJETCompiler _compiler;
	private final Object _argument;
	
	private StringBuffer _buffer = null;
	private IFile _outFile = null;
	
	@Override
	public IJJETTemplate getTemplate()
	{
		return _template;
	}

	@Override
	public IJJETCompiler getCompiler()
	{
		return _compiler;
	}
	
	@Override
	public Object getArgument()
	{
		return _argument;
	}

	@Override
	public StringBuffer getBuffer()
	{
		return _buffer;
	}

	@Override
	public void setBuffer(StringBuffer buffer)
	{
		if (_buffer != null)
			throw new RuntimeException("JJETContext.setBuffer() must not be called more than once");
		
		_buffer = buffer;
	}
	
	@Override
	public IFile getOutputFile()
	{
		return _outFile;
	}

	@Override
	public void setOutputFile(IPath path, String name)
	{
		if (_outFile != null)
			throw new RuntimeException("JJETContext.setOutputFile() must not be called more than once");
		
		if (_compiler.getBaseFolder() == null)
			throw new RuntimeException("JJETContext.setOutputFile() requires baseFilder in Compiler");
		
		IFolder folder = _compiler.getBaseFolder();

		try
		{
			if (!folder.exists())
				folder.create(true, true, null);
	
			for (String subFolder : path.segments())
			{
				folder = folder.getFolder(subFolder);
				
				if (!folder.exists())
				{
					try
					{
						folder.create(false, true, null);
					}
					catch(CoreException e)
					{
						// silently ignore if folder cannot be created
					}
				}

				getCompiler().resourceGenerated(folder);
			}
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		
		_outFile = folder.getFile(name);
	}

	@Override
	public void setOutputFile(IFile file)
	{
		if (_outFile != null)
			throw new RuntimeException("JJETContext.setOutputFile() must not be called more than once");
		_outFile = file;
	}

	public JJETContext(IJJETTemplate template, IJJETCompiler compiler, Object argument)
	{
		_template = template;
		_compiler = compiler;
		_argument = argument;
		_indentionStack.push(0);
	}

	@Override
	public void pushIndent(int nTabs)
	{
		_indentionStack.push(_indentionStack.peek() + nTabs);
	}

	@Override
	public void popIndent()
	{
		if (_indentionStack.size() > 1)
			_indentionStack.pop();
	}

	@Override
	public int getCurrentIndent()
	{
		return _indentionStack.peek();
	}
}
