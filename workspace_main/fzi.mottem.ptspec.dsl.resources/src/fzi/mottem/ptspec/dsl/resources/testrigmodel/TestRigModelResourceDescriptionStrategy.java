package fzi.mottem.ptspec.dsl.resources.testrigmodel;

import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;

public class TestRigModelResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy
{

	/*
	 *  This adds the root element of referenced CodeModel or DataStreamModel
	 *  to the global scope.
	 *  This would cause that the CodeModel / DataStreamModel can be referenced
	 *  directly (without "full qualified name"). This is not desired anymore
     *  and was commented out.
	 */

//	@Override
//	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor)
//	{
//		boolean result = super.createEObjectDescriptions(eObject, acceptor);
//		
//		if (!result)
//			return false;
//		
//		if (eObject instanceof SoftwareExecutor)
//		{
//			CodeInstance ci = ((SoftwareExecutor) eObject).getCodeInstance();
//			QualifiedName ciQualifiedName = new CodeModelQualifiedNameProvider().getFullyQualifiedName(ci);
//			acceptor.accept(EObjectDescription.create(ciQualifiedName, ci));
//			
//		}
//		else if(eObject instanceof IOPort)
//		{
//			DataStreamInstance dsi = ((IOPort) eObject).getDataStreamInstance();
//			QualifiedName dsiQualifiedName = new DataStreamModelQualifiedNameProvider().getFullyQualifiedName(dsi);
//			acceptor.accept(EObjectDescription.create(dsiQualifiedName, dsi));
//		}
//		
//		return true;
//	}

}
