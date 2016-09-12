package fzi.mottem.ptspec.dsl.ui.hyperlinking;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Region;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;

import fzi.mottem.model.codemodel.CClass;
import fzi.mottem.model.codemodel.SourceCodeLocation;
import fzi.mottem.model.codemodel.Symbol;
import fzi.util.ecore.EcoreUtils;

public class PTSpecCodeModelHyperlinkFactory
{
	
	public boolean canHandle(EObject target)
	{
		final SourceCodeLocation sLoc = getSourceCodeLocation(target);
		
		if (sLoc == null)
			return false;
		
		final IFile targetFile = getFile(sLoc);
		
		return targetFile.exists();
	}
	
	public void acceptHyperlink(Region sourceRegion, EObject target, IHyperlinkAcceptor acceptor)
	{
		acceptor.accept(createHyperlink(sourceRegion, target, acceptor));
	}
	
	private PTSpecCodeModelHyperlink createHyperlink(Region sourceRegion, EObject target, IHyperlinkAcceptor acceptor)
	{
		final SourceCodeLocation sLoc = getSourceCodeLocation(target);
		final IFile targetFile = getFile(sLoc);
		final Region targetRegion = new Region(sLoc.getOffset(), sLoc.getLength());
		
		return new PTSpecCodeModelHyperlink(targetFile, targetRegion, sourceRegion);
	}
	
	private SourceCodeLocation getSourceCodeLocation(EObject target)
	{
		if (target instanceof Symbol)
		{
			return ((Symbol)target).getDeclarationLocation();
		}
		else if (target instanceof CClass)
		{
			return ((CClass)target).getDeclarationLocation();
		}
		
		return null;
	}
	
	private IFile getFile(SourceCodeLocation sLoc)
	{
		final URI uri = URI.createPlatformResourceURI(sLoc.getSourceFile().getFilePath(), true);
		return EcoreUtils.getFileForEMFURI(uri);
	}

}
