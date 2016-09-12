package fzi.mottem.ptspec.dsl.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.util.Arrays;

import com.google.inject.Inject;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunction;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSubSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameter;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunctionDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;
import fzi.mottem.ptspec.dsl.scoping.PTSpecSymbolReferenceScopeProvider;
import fzi.util.StringUtils;
import fzi.util.ecore.EcoreUtils;

public class PTSpecProposalProvider extends AbstractPTSpecProposalProvider
{

	/**
	 * see {@link http://www.eclipse.org/forums/index.php/t/504481/} and
	 * make sure that concepts for scope definition are well understood :-)
	 */
	
	@Inject
	private PTSpecJavaTypeProposalProvider _pTSpecJavaProposalProvider;
	
	@Inject
	private ILabelProvider _labelProvider;
	
	@Inject
	private PTSpecSymbolReferenceScopeProvider _symbolReferenceScopeProvider;

	
	@Override
	public void completePTSSymbolReference_BaseSymbol(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		IScope scope = _symbolReferenceScopeProvider.createBaseScope(model, context.getOffset());
		hlp_completeITestReferenceable(scope, context, acceptor);
	}
	
	@Override
	public void completePTSActualSymbol_TestReferenceable(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		if (model instanceof PTSSubSymbolReference)
		{
			ITestReferenceable testRef = PTSpecUtils.getPredecessingTestReferenceable((PTSSubSymbolReference)model);
			IScope scope = _symbolReferenceScopeProvider.createSubSymbolScope(model, testRef);
			hlp_completeITestReferenceable(scope, context, acceptor);
		}
	}
	
	@Override
	public void completePTSJavaReference_BaseJElement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		IScope scope = getScopeProvider().getScope(model, PTSpecPackage.Literals.PTS_JAVA_REFERENCE__BASE_JELEMENT);
		hlp_completeJavaReference(scope, context, acceptor);
	}
	
	@Override
	public void completePTSActualJElement_JElement(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		IScope scope = getScopeProvider().getScope(model, PTSpecPackage.Literals.PTS_ACTUAL_JELEMENT__JELEMENT);
		hlp_completeJavaReference(scope, context, acceptor);
	}
	
	@Override
	public void completePTSUnitDeclaration_Name(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		final String[] NUMBERS_ONLY = {"¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹", "⁰"};
		final String[] PROPOSALS = {"⋅", "⁻", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹", "⁰"};
		final String[] DISPLAYS  = {"*", "-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

		String prefix = context.getPrefix();
		String[] proposals = PROPOSALS;
		
		if (prefix.isEmpty() || prefix.endsWith("⋅"))
		{
			return;
		}
		else if (prefix.endsWith("⁻") || Arrays.contains(proposals, prefix.charAt(prefix.length()-1)))
		{
			proposals = NUMBERS_ONLY;
    	}
		
		for (String proposalText : proposals)
		{
			int idx = java.util.Arrays.asList(PROPOSALS).indexOf(proposalText);
			
			proposalText = context.getPrefix() + proposalText;
			ICompletionProposal proposal = createCompletionProposal(
					proposalText,
					DISPLAYS[idx],
					null,
					context);
			
			acceptor.accept(proposal);
		}
	}
	
	private void hlp_completeITestReferenceable(IScope scope, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		for (IEObjectDescription eod : scope.getAllElements())
		{
			EObject eobj = eod.getEObjectOrProxy();
			
			final EObject tRef = (EObject) eobj;
			
			Image image;
			String proposalName;
			String fullName = PTSpecUtils.getDisplayName(tRef.eClass());
			
			if (tRef instanceof PTSPackageFunctionDeclaration)
			{
				PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(tRef, PTSPackageFunction.class);
				
				String params = "";
				boolean first = true;
				if (pkgFunc.getParameterList() != null)
				{
					for (PTSPackageFuncParameter param : pkgFunc.getParameterList().getParameters())
					{
						if (!first)
							params += ", ";
						
						params += param.getDataType().getIntegralType().getLiteral();
						if (param.getDataType().isArray())
							params += "[]";
						
						first = false;
					}
				}

				proposalName = eod.getName().toString() + "(" + params + ")";
				image = _labelProvider.getImage(eod.getEObjectOrProxy());
			}
			else
			{
				// !TODO: check me after making container for JvmIdentifiable
				
				proposalName = eod.getName().toString();
				image = _labelProvider.getImage(eod.getEObjectOrProxy());
				if (image == null)
					image = EcoreUtils.getImage(tRef.eClass());
			}
			
			StyledString displayName = new StyledString(proposalName);
			displayName.append(" - " + fullName, StyledString.QUALIFIER_STYLER);

			ICompletionProposal proposal = createCompletionProposal(
					proposalName,
					displayName,
					image,
					context);
			
			acceptor.accept(proposal);
		}
	}

	private void hlp_completeJavaReference(IScope scope, ContentAssistContext context, ICompletionProposalAcceptor acceptor)
	{
		for (IEObjectDescription eod : scope.getAllElements())
		{
			final JvmIdentifiableElement jElement = (JvmIdentifiableElement) eod.getEObjectOrProxy();
			String jIdentifier = jElement.getIdentifier();
			
			Image image;
			String proposalName;
			String fullName;
			
			if (jElement instanceof JvmDeclaredType)
			{
				int flags = 0;
				JvmDeclaredType dt = (JvmDeclaredType)jElement;
				
				if (dt.isAbstract())
					flags |= Flags.AccAbstract;
				if (dt.isFinal())
					flags |= Flags.AccFinal;
				if (dt.isStatic())
					flags |= Flags.AccStatic;
				
				switch (dt.getVisibility())
				{
				case DEFAULT:
					flags |= Flags.AccDefault;
				case PRIVATE:
					flags |= Flags.AccPrivate;
				case PROTECTED:
					flags |= Flags.AccProtected;
				case PUBLIC:
					flags |= Flags.AccPublic;
				}
				
				boolean isInner = jIdentifier.contains("$");

				image = _pTSpecJavaProposalProvider.computeImage(isInner, flags);
				proposalName = eod.getName().toString();
				fullName = jIdentifier;
			}
			else if (jElement instanceof JvmField)
			{
				image = null;
				proposalName = eod.getName().toString();
				fullName = jIdentifier;
			}
			else if(jElement instanceof JvmOperation)
			{
				image = null;
				int paramsStartIdx = jIdentifier.indexOf('(');
				String jIdentifierNoParams = jIdentifier.substring(0, paramsStartIdx);
				proposalName = StringUtils.getTail(jIdentifierNoParams, '.', true) + jIdentifier.substring(paramsStartIdx);
				fullName = StringUtils.getHead(jIdentifierNoParams, '.', true);
			}
			else
			{
				throw new RuntimeException("ContentAssist detected unexpected element in Java scope");
			}

			StyledString displayName = new StyledString(proposalName);
			displayName.append(" - " + fullName, StyledString.QUALIFIER_STYLER);

			ICompletionProposal proposal = createCompletionProposal(
					proposalName,
					displayName,
					image,
					context);
			
			acceptor.accept(proposal);
		}
	}


}
