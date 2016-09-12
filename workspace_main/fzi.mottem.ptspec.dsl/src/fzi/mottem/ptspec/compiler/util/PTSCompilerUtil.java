package fzi.mottem.ptspec.compiler.util;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.ptspec.compiler.PTSCompilerPlugin;
import fzi.mottem.ptspec.compiler.PTSCompilerSettings;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSNumberConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestSuiteDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSUnitDeclaration;
import fzi.util.ecore.EcoreUtils;

public class PTSCompilerUtil
{
	
	public static IFolder getBaseOutputFolder(IProject project)
	{
		PTSCompilerSettings settings = PTSCompilerPlugin.Instance.getSettings();
		
		IFolder outputFolder = project.getFolder(settings.getOutputFolder());
		
		return outputFolder;
	}
	
	public static String getStringCompatibleOriginalSyntax(PTSExpression expression)
	{
		ICompositeNode node = NodeModelUtils.getNode(expression);
		String ptsCode = node.getText().trim();
		ptsCode.replace('\"', '\'');
		return ptsCode;
	}
	
	public static Collection<PTSPackageDeclaration> getAllReferencedPackages(EObject ptsSymbolContainer)
	{
		HashSet<PTSPackageDeclaration> pkgDecls = new HashSet<PTSPackageDeclaration>();
		
		TreeIterator<EObject> it = ptsSymbolContainer.eAllContents();
		while (it.hasNext())
		{
			EObject obj = it.next();
			
			if (obj instanceof PTSSymbolReference)
			{
				ITestReferenceable testRef = PTSpecUtils.getFinalTestReferenceable((PTSSymbolReference) obj);
				PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(testRef, PTSPackageDeclaration.class);
				if (pkgDecl != null && !pkgDecls.contains(pkgDecl))
				{
					pkgDecls.add(pkgDecl);
				}
			}
			else if (obj instanceof PTSNumberConstant)
			{
				PTSUnitDeclaration unitDecl = ((PTSNumberConstant) obj).getUnit();
				if (unitDecl != null)
				{
					PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(unitDecl, PTSPackageDeclaration.class);
					pkgDecls.add(pkgDecl);
				}
			}
		}

		return pkgDecls;
	}

	public static Collection<PTSTestDeclaration> getAllReferencedTests(PTSTestSuiteDeclaration testSuiteDecl)
	{
		return testSuiteDecl.getList().getTests();
	}
}
