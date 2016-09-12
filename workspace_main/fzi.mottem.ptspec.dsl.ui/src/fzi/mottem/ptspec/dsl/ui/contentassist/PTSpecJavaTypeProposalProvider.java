package fzi.mottem.ptspec.dsl.ui.contentassist;

import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.common.types.xtext.ui.JdtTypesProposalProvider;

public class PTSpecJavaTypeProposalProvider extends JdtTypesProposalProvider
{
	public Image computeImage(boolean isInnerType, int modifiers)
	{
		return super.computeImage(null, isInnerType, modifiers);
	}
}
