package fzi.mottem.ptspec.dsl.ui.highlighting;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.LeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ISymbolContainer;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.codemodel.CClass;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSActualJElement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSActualSymbol;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDataType;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExecutor;
import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSNumberConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameterDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunctionDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageUnit;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSUnitExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;

public class PTSpecHighlightingCalculator implements ISemanticHighlightingCalculator 
{
	
	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor)
	{
		if (resource == null || resource.getParseResult() == null)
		   return;
     
		INode rootNode = resource.getParseResult().getRootNode();
		
		for (INode node : rootNode.getAsTreeIterable())
		{
			if (!(node instanceof LeafNode))
				continue;
			
			if (node.getSemanticElement() instanceof PTSSymbolReference)
			{
				ITestReferenceable testRef = ((PTSSymbolReference)node.getSemanticElement()).getBaseSymbol();
				String hlConfigID = getHighlightingConfiguration(testRef);
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_SYMBOL_REFERENCE__BASE_SYMBOL, hlConfigID, acceptor);
			}
				
			else if (node.getSemanticElement() instanceof PTSActualSymbol)
			{
				EObject testRef = ((PTSActualSymbol)node.getSemanticElement()).getTestReferenceable();
				
				String hlConfigID;
				if (testRef instanceof ITestReferenceable)
					hlConfigID = getHighlightingConfiguration((ITestReferenceable)testRef);
				else if (testRef instanceof JvmIdentifiableElement)
					hlConfigID = getHighlightingConfiguration((JvmIdentifiableElement)testRef);
				else
					continue;
				
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_ACTUAL_SYMBOL__TEST_REFERENCEABLE, hlConfigID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSJavaReference)
			{
				JvmIdentifiableElement jRef = ((PTSJavaReference)node.getSemanticElement()).getBaseJElement();
				String hlConfigID = getHighlightingConfiguration(jRef);
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_JAVA_REFERENCE__BASE_JELEMENT, hlConfigID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSActualJElement)
			{
				JvmIdentifiableElement jRef = ((PTSActualJElement)node.getSemanticElement()).getJElement();
				String hlConfigID = getHighlightingConfiguration(jRef);
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_ACTUAL_JELEMENT__JELEMENT, hlConfigID, acceptor);
			}
				
			else if (node.getSemanticElement() instanceof PTSTestVariableDeclaration)
			{
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_TEST_VARIABLE_DECLARATION.getEStructuralFeature("name"), PTSpecHighlightingConfiguration.TESTSYMBOL_HL_ID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSNumberConstant)
			{
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_NUMBER_CONSTANT__UNIT, PTSpecHighlightingConfiguration.PHYSICALUNIT_HL_ID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSPackageUnit)
			{
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_PACKAGE_UNIT__BASE_UNIT, PTSpecHighlightingConfiguration.PHYSICALUNIT_HL_ID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSUnitExpression)
			{
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_UNIT_EXPRESSION__UNIT, PTSpecHighlightingConfiguration.PHYSICALUNIT_HL_ID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSDataType)
			{
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_DATA_TYPE__UNIT, PTSpecHighlightingConfiguration.PHYSICALUNIT_HL_ID, acceptor);
			}
			
			else if (node.getSemanticElement() instanceof PTSExecutor)
			{
				highlightFirstNodeForFeature(node.getSemanticElement(), PTSpecPackage.Literals.PTS_EXECUTOR__ACTUAL_EXECUTOR, PTSpecHighlightingConfiguration.EXECUTOR_HL_ID, acceptor);
			}

		}
	}

	private String getHighlightingConfiguration(JvmIdentifiableElement jElement)
	{
		if (jElement instanceof JvmDeclaredType)
		{
			return PTSpecHighlightingConfiguration.JAVADECLTYPE_HL_ID;
		}
		else
		{
			return PTSpecHighlightingConfiguration.JAVAOTHERELEMENT_HL_ID;
		}
	}

	private String getHighlightingConfiguration(ITestReferenceable testRef)
	{
		if (testRef instanceof PTSTestVariableDeclaration ||
			testRef instanceof PTSPackageFuncParameterDeclaration)
		{
			return PTSpecHighlightingConfiguration.TESTSYMBOL_HL_ID;
		}
		else if (testRef instanceof PTSPackageDeclaration)
		{
			return PTSpecHighlightingConfiguration.PACKAGE_HL_ID;
		}
		else if (testRef instanceof PTSPackageFunctionDeclaration)
		{
			return PTSpecHighlightingConfiguration.PACKAGEFUNCTION_HL_ID;
		}
		else if (testRef instanceof PTSPackageVariableDeclaration)
		{
			return PTSpecHighlightingConfiguration.PACKAGEVARIABLE_HL_ID;
		}
		else if (testRef instanceof IExecutor)
		{
			return PTSpecHighlightingConfiguration.EXECUTOR_HL_ID;
		}
		else if (PTSpecUtils.isExternalReference(testRef))
		{
			return PTSpecHighlightingConfiguration.EXTERNALSYMBOL_HL_ID;
		}
		else if (testRef instanceof ISymbolContainer)
		{
			return PTSpecHighlightingConfiguration.EXTERNALPACKAGE_HL_ID;
		}
		else if (testRef instanceof CClass)
		{
			return PTSpecHighlightingConfiguration.EXTERNALPACKAGE_HL_ID;
		}
		else
		{
			return PTSpecHighlightingConfiguration.DEFAULT_ID;
		}
	}

	private void highlightFirstNodeForFeature(EObject eObj, EStructuralFeature eStructuralFeature, String highlightID, IHighlightedPositionAcceptor acceptor)
	{
		List<INode> nodes = NodeModelUtils.findNodesForFeature(eObj, eStructuralFeature);
		
		if (nodes.size() <= 0)
			return;
		
		INode node = nodes.get(0);
		
		acceptor.addPosition(node.getOffset(), node.getLength(), highlightID);
	}
	
}
