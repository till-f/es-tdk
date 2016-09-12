package fzi.mottem.ptspec.dsl.ui.hyperlinking;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Region;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.xtext.ui.editor.hyperlinking.AbstractHyperlink;

public class PTSpecCodeModelHyperlink extends AbstractHyperlink
{
	
	private final IFile _targetFile;
	private final Region _targetRegion;
	
	public PTSpecCodeModelHyperlink(IFile targetFile, Region targetRegion, Region sourceRegion)
	{
		_targetFile = targetFile;
		_targetRegion = targetRegion;
		
		setHyperlinkRegion(sourceRegion);
	}

	@Override
	public void open()
	{
		try
		{
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			ITextEditor editor = (ITextEditor) IDE.openEditor(page, _targetFile);
			editor.selectAndReveal(_targetRegion.getOffset(), _targetRegion.getLength());
		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
	}

}
