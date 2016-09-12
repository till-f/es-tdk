package fzi.mottem.ptspec.dsl.scoping;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IGlobalScopeProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IMessage;
import fzi.mottem.model.baseelements.IReferenceableContainer;
import fzi.mottem.model.baseelements.ISignal;
import fzi.mottem.model.baseelements.ISymbolContainer;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.IWrappedReferenceable;
import fzi.mottem.model.codemodel.CClass;
import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.model.datastreammodel.DataStreamInstance;
import fzi.mottem.model.environmentdatamodel.EnvironmentDataInstance;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSAnalyzeBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarationStatementForLoop;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarator;
import fzi.mottem.ptspec.dsl.pTSpec.PTSForLoopStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSImplementation;
import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageElement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameter;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunction;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariable;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPostRealtimeBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.mottem.ptspec.dsl.pTSpec.PTSScopeStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTargetDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;
import fzi.util.ecore.EcoreUtils;

public class PTSpecSymbolReferenceScopeProvider
{
	
	@Inject
	private IGlobalScopeProvider _globalScopeProvider;
	
	@Inject
	private PTSpecJavaScopeProvider _pTSpecGlobalJavaScopeProvider;
	
	private Predicate<IEObjectDescription> _filter_TypePTSPackage = new Predicate<IEObjectDescription>()
	{
		@Override
		public boolean apply(IEObjectDescription input)
		{
			if (input.getEObjectOrProxy() instanceof PTSPackageDeclaration)
				return true;
			else
				return false;
		}
	};
	
	
	/*
	 * Factory method for base symbol scope (based on statement; for scoping)
	 */
	public IScope createBaseScope(EObject ptsElement)
	{
		int offset = NodeModelUtils.getNode(ptsElement).getOffset();

		return createBaseScope(ptsElement, offset);
	}
	
	/*
	 * Factory method for base symbol scope (based on object and offset; for content assist)
	 */
	public IScope createBaseScope(EObject ptsElement, int offset)
	{
		LinkedList<ITestReferenceable> scopeElements = new LinkedList<ITestReferenceable>();

		// construct the global scope (this adds all PTSPackageDeclarations from imported URIs into scope)
		IScope globalScope = _globalScopeProvider.getScope(ptsElement.eResource(), PTSpecPackage.Literals.PTS_SYMBOL_REFERENCE__BASE_SYMBOL, _filter_TypePTSPackage);
		
		// also add package declarations within own resource
		PTSRoot root = EcoreUtils.getContainerInstanceOf(ptsElement, PTSRoot.class);
		if (root != null)
		{
			for (IReferenceableContainer container : root.getContainerDeclarations())
			{
				if (container instanceof PTSPackageDeclaration)
					scopeElements.add(container);
			}
		}
		
		
		// From here elements depending on the context are added
		// ----------------------------------------------------------------------------
		
		PTSScopeStatement scopeStm = EcoreUtils.getContainerInstanceOf(ptsElement, PTSScopeStatement.class);
		PTSTestDeclaration testDecl = EcoreUtils.getContainerInstanceOf(ptsElement, PTSTestDeclaration.class);
		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(ptsElement, PTSPackageFunction.class);
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(ptsElement, PTSPackageDeclaration.class);

		// Executors in Target are added
		if (testDecl != null)
		{
			addSpecials_executorsInTarget(scopeElements, testDecl.getTarget());
		}
		else if (pkgDecl != null)
		{
			addSpecials_executorsInTarget(scopeElements, pkgDecl.getTarget());
		}
		
		
		// Externals of current executor in scope are added.
		// If there is no scope statement, the default executor of Test or PackageFunction is added (if any).
		IExecutor exec = null;
		if (scopeStm != null && scopeStm.getExecutor() != null)
		{
			exec = scopeStm.getExecutor().getActualExecutor();
		}
		else if(testDecl != null && testDecl.getDefaultExecutor() != null)
		{
			exec = testDecl.getDefaultExecutor().getActualExecutor();
		}
		else if(pkgFunc != null && pkgFunc.getExecutor() != null)
		{
			exec = pkgFunc.getExecutor().getActualExecutor();
		}
		addSpecials_baseExternalsForExecutor(scopeElements, exec);

		// If we are in an for loop, add respective declaration arguments
		PTSForLoopStatement flStatement = EcoreUtils.getContainerInstanceOf(ptsElement, PTSForLoopStatement.class);
		if (flStatement != null)
		{
			addSpecials_ForLoopStatement(flStatement, offset, scopeElements);
		}

		// If we are in an AnalyzeBlock or PlotBlock, add local symbols of previous block of this kind
		PTSPostRealtimeBlock prBlock = EcoreUtils.getContainerInstanceOf(ptsElement, PTSPostRealtimeBlock.class);
		if (prBlock != null)
		{
			addSpecials_PostRealtimeBlock(prBlock, offset, scopeElements);
		}

		// If we are in a package function add specific elements from package
		if (pkgFunc != null)
		{
			addSpecials_PackageFunction(pkgFunc, offset, scopeElements);
		}

		// Finally add all declarations of top-level implementation that are always visible just depending on the offset
		if (testDecl != null)
		{
			subSym_addAllLocalDeclarations(scopeElements, testDecl.getImplementation(), ptsElement, false, offset);
			return Scopes.scopeFor(scopeElements, globalScope);
		}
		else if (pkgFunc != null)
		{
			subSym_addAllLocalDeclarations(scopeElements, pkgFunc.getImplementation(), ptsElement, false, offset);
			return Scopes.scopeFor(scopeElements, globalScope);
		}
		else
		{
			throw new RuntimeException("Unknown top level construct for PTSStatement");
		}
	}


	/*
	 * Factory method for sub symbol scope (right of a ".").
	 * ptsElement specifies the grammer object the scope request originates from.
	 * baseItem specifies the source container for sub symbols (left of the ".").
	 */
	public IScope createSubSymbolScope(EObject ptsElement, ITestReferenceable baseItem)
	{
		LinkedList<ITestReferenceable> scopeElements = new LinkedList<ITestReferenceable>();
		LinkedList<JvmIdentifiableElement> javaScopeElements = new LinkedList<JvmIdentifiableElement>();
		
		if (baseItem instanceof IExecutor)
		{
			for (EObject item : ((IExecutor) baseItem).getSymbolContainer().eContents())
			{
				if (item instanceof ITestReferenceable)
				{
					scopeElements.add((ITestReferenceable)item);
				}
			}
		}
		else if (baseItem instanceof PTSPackageDeclaration)
		{
			subSym_addPackgeContent(scopeElements, (PTSPackageDeclaration) baseItem, PTSpecUtils.isTraceAnalyzeContext(ptsElement), PTSpecUtils.isRealtimeContext(ptsElement));
		}
		else if (baseItem instanceof PTSTestVariableDeclaration)
		{
			PTSDeclarator declarator = EcoreUtils.getContainerInstanceOf(baseItem, PTSDeclarator.class);
			
			PTSJavaReference jRef = (PTSJavaReference)declarator.getJavaType();
			
			if (jRef != null)
			{
				JvmIdentifiableElement javaClass = jRef.getBaseJElement();
				_pTSpecGlobalJavaScopeProvider.addNonStaticMembers(javaClass, javaScopeElements);
			}
		}
		else if (baseItem instanceof IWrappedReferenceable)
		{
			EObject innerObject = ((IWrappedReferenceable) baseItem).getInnerObject();
			
			JvmType type = null;
			if (innerObject instanceof JvmField)
			{
				type = ((JvmField) innerObject).getType().getType();
			}
			else if (innerObject instanceof JvmOperation)
			{
				type = ((JvmOperation) innerObject).getReturnType().getType();
			}
			
			_pTSpecGlobalJavaScopeProvider.addNonStaticMembers(type, javaScopeElements);
		}
		else
		{
			System.out.println("DEBUG: unexpected base item during scope calculation: " + baseItem.getClass().getSimpleName());

//			for (EObject item : baseItem.eContents())
//			{
//				if (item instanceof ITestReferenceable)
//				{
//					scopeElements.add((ITestReferenceable)item);
//				}
//			}
		}
		
		IScope javaScope = _pTSpecGlobalJavaScopeProvider.scopeFor(javaScopeElements);
		return Scopes.scopeFor(scopeElements, javaScope);
	}
	

	private void addSpecials_executorsInTarget(LinkedList<ITestReferenceable> scopeElements, PTSTargetDeclaration targetDecl)
	{
		if (targetDecl == null)
			return;
		
		for (IExecutor executor : targetDecl.getList().getActualTargets())
		{
			scopeElements.add(executor);
		}
	}
	
	private void addSpecials_baseExternalsForExecutor(Collection<ITestReferenceable> scopeElements, IExecutor executor)
	{
		if (executor == null || executor.getName() == null)
			return;
		
		ISymbolContainer symbolContainer = executor.getSymbolContainer();
		
		if (symbolContainer == null || symbolContainer.getName() == null)
			return;

		if(symbolContainer instanceof CodeInstance)
		{
			subSym_addCodeBaseExternals(scopeElements, (CodeInstance)symbolContainer);
		}
		else if (symbolContainer instanceof DataStreamInstance)
		{
			subSym_addDataStreamBaseExeternals(scopeElements, (DataStreamInstance)symbolContainer);
		}
		else if (symbolContainer instanceof EnvironmentDataInstance)
		{
			subSym_addEnvironmentDataBaseExternals(scopeElements, (EnvironmentDataInstance)symbolContainer);
		}
		else if (symbolContainer instanceof IOPin)
		{
			subSym_addIOPinBaseExternals(scopeElements, (IOPin)symbolContainer);
		}
		else
		{
			throw new RuntimeException("Unkown SymbolContainer type: " + symbolContainer.getClass().getSimpleName());
		}
	}

	private void addSpecials_ForLoopStatement(PTSForLoopStatement forLoopStm, int offset, LinkedList<ITestReferenceable> scopeElements)
	{
		for (PTSDeclarationStatementForLoop decl : forLoopStm.getForDecls())
		{
			scopeElements.add(decl.getDeclarator().getDeclaration());
		}

		// local declarations
		PTSForLoopStatement containerForLoopStm = EcoreUtils.getContainerInstanceOf(forLoopStm.eContainer(), PTSForLoopStatement.class);
		if (containerForLoopStm != null)
		{
			addSpecials_ForLoopStatement(containerForLoopStm, offset, scopeElements);
		}
	}

	private void addSpecials_PostRealtimeBlock(PTSPostRealtimeBlock prBlock, int offset, LinkedList<ITestReferenceable> scopeElements)
	{
		// local declarations of preceeding analze blocks
		for(PTSAnalyzeBlock otherAnaBlock : PTSpecUtils.getPredecessingAnalzeBlocks(prBlock))
		{
			subSym_addAllLocalDeclarations(scopeElements, otherAnaBlock.getImplementation(), null, true, offset);
		}
	}

	private void addSpecials_PackageFunction(PTSPackageFunction pkgFunc, int offset, LinkedList<ITestReferenceable> scopeElements)
	{
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(pkgFunc, PTSPackageDeclaration.class);

		// constants and functions declared in package
		subSym_addPackgeContent(scopeElements, pkgDecl, pkgFunc.isAnalyzeFunc(), pkgFunc.isRealtimeFunc());

		// function parameters
		subSym_addPackageFuncParameters(scopeElements, pkgFunc);
	}
	
	
	
	/*
	 * add content of speficic package
	 * public because also used from PTSSubSymbolReferenceScope
	 */
	private void subSym_addPackgeContent(Collection<ITestReferenceable> scopeElements, PTSPackageDeclaration pkgDecl, boolean isAnalyzeContext, boolean isRealtimeContext)
	{
		if (pkgDecl == null)
			return;
		
		for (PTSPackageElement elem : pkgDecl.getPackageElements())
		{
			if (elem instanceof PTSPackageFunction)
			{
				if (isAnalyzeContext && ((PTSPackageFunction) elem).isAnalyzeFunc() ||
					isRealtimeContext && ((PTSPackageFunction) elem).isRealtimeFunc())
				{
					// ok, needed to checked catched first (see below)
				}
				else if (
						((isAnalyzeContext || isRealtimeContext) && PTSpecUtils.mightReferExternals((PTSPackageFunction)elem)) ||
						(!isAnalyzeContext && ((PTSPackageFunction)elem).isAnalyzeFunc()) ||
						(!isRealtimeContext && ((PTSPackageFunction)elem).isRealtimeFunc())
						)
				{
					// no functions with externals in analyze and realtime blocks (except they are analyze or realtime functions, see above)
					// no analyze functions outside analyze blocks
					// no realtime functions outsite realtime blocks
					continue;
				}
				
				ITestReferenceable tr = ((PTSPackageFunction) elem).getDeclaration();
				
				if (tr.getName() != null)
					scopeElements.add(tr);
			}
			else if (elem instanceof PTSPackageVariable)
			{
				ITestReferenceable tr = ((PTSPackageVariable) elem).getDeclaration();
				
				if (tr.getName() != null)
					scopeElements.add(tr);
			}
			else
			{
				throw new RuntimeException("Unknown PTSPackageElement type in PTSPackageDeclaration.getPackageElements: " + elem.getClass().getSimpleName());
			}
		}
	}
	
	/*
	 * add package function parameters
	 */
	private void subSym_addPackageFuncParameters(Collection<ITestReferenceable> scopeElements, PTSPackageFunction pkgFunc)
	{
		if (pkgFunc == null || pkgFunc.getParameterList() == null)
			return;
		
		for (PTSPackageFuncParameter param : pkgFunc.getParameterList().getParameters())
		{
			ITestReferenceable tr = param.getDeclaration();
			
			if (tr.getName() != null)
				scopeElements.add(tr);
		}
	}

	/*
	 * add external elements of code
	 */
	private void subSym_addCodeBaseExternals(Collection<ITestReferenceable> scopeElements, CodeInstance code)
	{
		for (Symbol symbol: code.getSymbols())
		{
			scopeElements.add(symbol);
		}

		for (CClass cClass: code.getCClasses())
		{
			scopeElements.add(cClass);
		}
	}

	/*
	 * add external elements of data stream
	 */
	private void subSym_addDataStreamBaseExeternals(Collection<ITestReferenceable> scopeElements, DataStreamInstance dstream)
	{
		for (ISignal signal: dstream.getSignals())
		{
			scopeElements.add(signal);
		}
		for (IMessage message: dstream.getCanMessages())
		{
			scopeElements.add(message);
		}
	}
	
	/*
	 * add external elements of inspector attributes
	 */
	private void subSym_addEnvironmentDataBaseExternals(Collection<ITestReferenceable> scopeElements, EnvironmentDataInstance edi)
	{
		for (ISignal s: edi.getSignals())
		{
			scopeElements.add(s);
		}
	}

	/*
	 * add external elements of inspector attributes
	 */
	private void subSym_addIOPinBaseExternals(Collection<ITestReferenceable> scopeElements, IOPin pin)
	{
		for (ISignal s: pin.getPinSignals())
		{
			scopeElements.add(s);
		}
	}

	/*
	 * add ALL local declarations.
	 * If "current" is null, no exception is made
	 * If "current" is not null, only those are added that are declared in an implementation block that is a parent of "current"
	 */
	private void subSym_addAllLocalDeclarations(Collection<ITestReferenceable> scopeElements, PTSImplementation baseImplementation, EObject current, boolean capturedOnly, int stopOffset)
	{
		if (baseImplementation == null)
			return;
		
		LinkedList<PTSImplementation> parentImplsOfCurrent = new LinkedList<PTSImplementation>();
		if (current != null)
		{
			while(true)
			{
				PTSImplementation impl = EcoreUtils.getContainerInstanceOf(current, PTSImplementation.class);
				if (impl == null) break;
				parentImplsOfCurrent.add(impl);
				current = impl.eContainer();
				if (current == null) break;
			}
		}
 
		TreeIterator<EObject> allChilds = baseImplementation.eAllContents();
		while (allChilds.hasNext())
		{
			EObject child = allChilds.next();
			
			if (child instanceof PTSDeclarator)
			{
				if (current != null)
				{
					// if "current" is not null, only elements declared in same hierarchy as current are added
					PTSImplementation parentImplOfNew;
					if (child.eContainer() instanceof PTSDeclarationStatementForLoop)
					{
						PTSForLoopStatement fls = EcoreUtils.getContainerInstanceOf(child, PTSForLoopStatement.class);
						parentImplOfNew = fls.getImplementation();
					}
					else
					{
						parentImplOfNew = EcoreUtils.getContainerInstanceOf(child, PTSImplementation.class);
					}
					
					if (!parentImplsOfCurrent.contains(parentImplOfNew))
					{
						continue;
					}
				}
				
				if (capturedOnly && !PTSpecUtils.isCapture((PTSDeclarator) child))
					continue;
				
				boolean reachedStopOffset = subSym_doAddDeclaration(scopeElements, ((PTSDeclarator)child), stopOffset);
				if (reachedStopOffset)
					return;
			}
		}

		return;
	}
	
	/*
	 * actually adds to the scope
	 */
	private boolean subSym_doAddDeclaration(Collection<ITestReferenceable> scopeElements, PTSDeclarator declarator, int stopOffset)
	{
		int offset = NodeModelUtils.getNode(declarator).getOffset();
		
		if (offset >= stopOffset)
			return true;

		ITestReferenceable tRef = declarator.getDeclaration();
		
		if (tRef.getName() != null)
			scopeElements.add(tRef);
		
		return false;
	}
	
}
