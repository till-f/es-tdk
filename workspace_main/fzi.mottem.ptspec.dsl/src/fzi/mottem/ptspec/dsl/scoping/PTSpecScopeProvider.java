package fzi.mottem.ptspec.dsl.scoping;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IGlobalScopeProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSBreakAtStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRunUntilStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSubJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSubSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSUnitDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;

public class PTSpecScopeProvider extends AbstractDeclarativeScopeProvider 
{
	/**
	 * For most scopes in PTSpec it is not enough to simply rely on Xtext scope calculation.
	 * The global scope contains too many elements, since most symbols are "out of scope" in 
	 * typical contexts (executors etc. narrow the scope, only "included" files may be used).
	 * One approach would be to always filter the global scope, but for better "namespace"
	 * experience (editor is aware of indiviual parts of "."-separated name) it is more promising
	 * to pick only the "top" items. This can be calculated more efficiently not using filters
	 * but setting up the element-list manually.
	 *
	 * All elements that can be referred to in the language, derive from "ITestReferenceable",
	 * thus the scope is made up of a list of ITestReferenceables.
	 * 
	 * Referencing Java elements utilizes features that come with Xtext. This only works with
	 * the "new" mechanism for imports (importedNamespace instead of importURI) and requieres
	 * the types.TypesGeneratorFragment. This will force the global scope provider to be of the
	 * type TypesAwareDefaultGlobalScopeProvider. This again is not compatibel with importURI.
	 * For this reason a custom IGlobalScopeProvider (PTSpecGlobalScopeProvider) is used. It
	 * derives from ImportUriGlobalScopeProvider (which results in regular importURI support)
	 * and "importedNamespace" is handled manually. 
	 */

	@Inject
	private PTSpecSymbolReferenceScopeProvider _symbolReferenceScopeProvider;
	
	@Inject
	private PTSpecJavaScopeProvider _pTSpecGlobalJavaScopeProvider;
	
	@Inject
	private IGlobalScopeProvider _globalScopeProvider;
	
	private Predicate<IEObjectDescription> _filter_TypePTSUnitDeclaration = new Predicate<IEObjectDescription>()
	{
		@Override
		public boolean apply(IEObjectDescription input)
		{
			if (input.getEObjectOrProxy() instanceof PTSUnitDeclaration)
				return true;
			else
				return false;
		}
	};
	
	// Java Import
	public IScope scope_PTSJavaImport_importedType(PTSRoot root, EReference ref)
	{
		return _pTSpecGlobalJavaScopeProvider.getScope(root.eResource(), ref);
	}

	// Executors
	public IScope scope_PTSExecutor_actualExecutor(PTSTestDeclaration testDecl, EReference ref)
	{
		Collection<IExecutor> executors = PTSpecUtils.getVisibleExecutors(testDecl);

		return Scopes.scopeFor(executors);
	}
	
	public IScope scope_PTSExecutor_actualExecutor(PTSPackageDeclaration pkgDecl, EReference ref)
	{
		Collection<IExecutor> executors = PTSpecUtils.getVisibleExecutors(pkgDecl);

		return Scopes.scopeFor(executors);
	}
	
	// Unit
	public IScope scope_PTSNumberConstant_unit(PTSRoot root, EReference ref)
	{
		IScope globalScope = _globalScopeProvider.getScope(root.eResource(), PTSpecPackage.Literals.PTS_NUMBER_CONSTANT__UNIT, _filter_TypePTSUnitDeclaration);
		
		return PTSpecUtils.computeSimpleAttributeNameScope(globalScope);
	}
	
	public IScope scope_PTSDataType_unit(PTSRoot root, EReference ref)
	{
		IScope globalScope = _globalScopeProvider.getScope(root.eResource(), PTSpecPackage.Literals.PTS_DATA_TYPE__UNIT, _filter_TypePTSUnitDeclaration);
		
		return PTSpecUtils.computeSimpleAttributeNameScope(globalScope);
	}
	
	// Symbol Reference
	public IScope scope_PTSSymbolReference_baseSymbol(PTSRoot root, EReference ref)
	{
		System.err.println("DEBUG: PTSSymbolReference_baseSymbol scope request for PTSRoot detected");
		return IScope.NULLSCOPE;
	}

	public IScope scope_PTSSymbolReference_baseSymbol(PTSStatement stm, EReference ref)
	{
		// must only be called for run-until and stop-at (others will use version for expression below)
		if (stm instanceof PTSRunUntilStatement || stm instanceof PTSBreakAtStatement)
		{
			return _symbolReferenceScopeProvider.createBaseScope(stm);
		}

		System.err.println("DEBUG: PTSSymbolReference_baseSymbol scope request for unexpected PTSStatement");
		return IScope.NULLSCOPE;
	}
	
	public IScope scope_PTSSymbolReference_baseSymbol(PTSExpression expr, EReference ref)
	{
		return _symbolReferenceScopeProvider.createBaseScope(expr);
	}
	
	public IScope scope_PTSActualSymbol_testReferenceable(PTSSubSymbolReference subSymbol, EReference ref)
	{
		ITestReferenceable predecessor = PTSpecUtils.getPredecessingTestReferenceable(subSymbol);
		return _symbolReferenceScopeProvider.createSubSymbolScope(subSymbol, predecessor);
	}

	// Java Reference
	public IScope scope_PTSJavaReference_baseJElement(PTSRoot root, EReference ref)
	{
		return _pTSpecGlobalJavaScopeProvider.getPTSpecJavaScope(root);
	}
	
	public IScope scope_PTSActualJElement_jElement(PTSSubJavaReference subJRef, EReference ref)
	{
		JvmIdentifiableElement jElement = PTSpecUtils.getPredecessingJvmIdentifiable(subJRef);
		return _pTSpecGlobalJavaScopeProvider.calculateScopeForStaticMembers(jElement);
	}

}
