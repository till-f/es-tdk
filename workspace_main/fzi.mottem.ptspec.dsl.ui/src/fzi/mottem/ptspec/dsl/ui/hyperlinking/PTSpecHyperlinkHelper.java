package fzi.mottem.ptspec.dsl.ui.hyperlinking;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Region;
import org.eclipse.xtext.common.types.xtext.ui.TypeAwareHyperlinkHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;

import com.google.inject.Inject;

public class PTSpecHyperlinkHelper extends TypeAwareHyperlinkHelper
{

	@Inject
	private PTSpecCodeModelHyperlinkFactory _ptsCodeModelHyperlinkFactory;
	
	@Override
	public void createHyperlinksTo(XtextResource from, Region region, EObject to, IHyperlinkAcceptor acceptor)
	{
		if (_ptsCodeModelHyperlinkFactory.canHandle(to))
			_ptsCodeModelHyperlinkFactory.acceptHyperlink(region, to, acceptor);
		else 
			super.createHyperlinksTo(from, region, to, acceptor);
	}

}
