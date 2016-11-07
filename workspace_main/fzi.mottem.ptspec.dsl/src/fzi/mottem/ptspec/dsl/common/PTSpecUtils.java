package fzi.mottem.ptspec.dsl.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;

import fzi.mottem.model.baseelements.IDisplayable;
import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.IMessage;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.baseelements.IReferenceableContainer;
import fzi.mottem.model.baseelements.ISignal;
import fzi.mottem.model.baseelements.ISymbolContainer;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.IWrappedReferenceable;
import fzi.mottem.model.baseelements.impl.BaseelementsFactoryImpl;
import fzi.mottem.model.codemodel.CodemodelPackage;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DTVoid;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.model.datastreammodel.DatastreammodelPackage;
import fzi.mottem.model.environmentdatamodel.EnvironmentdatamodelPackage;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelPackage;
import fzi.mottem.model.testrigmodel.Thread;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.compiler.PTSCompilerException;
import fzi.mottem.ptspec.dsl.pTSpec.PTSAnalyzeBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSConcurrentStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDataType;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarationStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarationStatementForLoop;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarator;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSImplementation;
import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSNumberConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageElement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameter;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameterDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunction;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunctionDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageUnit;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariable;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPlotRangeExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPostRealtimeBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRealtimeBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRealtimeStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRunStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRunUntilStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRuntimeInstance;
import fzi.mottem.ptspec.dsl.pTSpec.PTSScopeStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSpecialConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStopStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStringConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSubJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSubSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSUnitDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSValueSymbol;
import fzi.mottem.ptspec.dsl.pTSpec.PTSWaitDeltaStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSWaitTimeStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSWaitUntilStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EINTEGRALDATATYPE;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EOPERATOR;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESPECIALCONSTANT;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;
import fzi.util.StringUtils;
import fzi.util.TupleFirstKey;
import fzi.util.ecore.EcoreUtils;
import fzi.util.xtext.XtextUtils;

public class PTSpecUtils
{

	public final static String UID_PROPERTY_SEPARATOR = "::";
	
	public final static String DATATYPE_PHYSICAL_PREFIX = "physical_";
	public final static String DATATYPE_PHYSICAL_SEPARATOR = "⋅";
	public final static String DATATYPE_PHYSICAL_SUPERMINUS = "⁻";
	public final static char[] DATATYPE_PHYSICAL_SUPERSCR = {'⁻', '⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹'};
	public final static String DATATYPE_ARRAY_POSTFIX = "[]";

	public static String getElementUID(EObject eObject)
	{
		// For elements from base Ecore models (CodeModel, DataStreamModel, ...)
		// a custom XMI ID has been calculated using ModelUtils.generateCustomUUID(eObject)
		// by overwriting *ResourceImpl.attachedHelper(EObject eObject).
		// Xtext models do not provide XMI IDs, thus we have to calculate an ID here.

		String basePath = eObject.eResource().getURI().toPlatformString(true) + "/";
		
		if (eObject instanceof PTSTestVariableDeclaration ||
			eObject instanceof PTSPackageFuncParameterDeclaration)
		{
			PTSTestDeclaration test = EcoreUtils.getContainerInstanceOf(eObject, PTSTestDeclaration.class);
			if (test != null)
				basePath += test.getName() + "_";
			
			PTSPackageDeclaration pkg = EcoreUtils.getContainerInstanceOf(eObject, PTSPackageDeclaration.class);
			if (pkg != null)
				basePath += pkg.getName() + "_";
			
			PTSPackageFunction func = EcoreUtils.getContainerInstanceOf(eObject, PTSPackageFunction.class);
			if (func != null)
				basePath += func.getDeclaration().getName() + "_";
			
			return basePath + ((INamed)eObject).getName();
		}
		else
		{
			return basePath + EcoreUtils.getXmiId(eObject);
		}
	}

	public static String getPropertyUID(EObject ele, PTS_EPROPERTY property)
	{
		return getPropertyUID(getElementUID(ele), property);
	}
	
	public static String getPropertyUID(String elementUID, PTS_EPROPERTY property)
	{
		String propertyStr = null;
	
		switch (property)
		{
			case ADDRESS:
				propertyStr = "A";
				break;
			case INSTRUCTION_POINTER:
				propertyStr = "I";
				break;
			case SAMPLE_RATE:
				propertyStr = "SR";
				break;
			case TRIGGER_ABOVE:
				propertyStr = "TA";
				break;
			case TRIGGER_BELOW:
				propertyStr = "TB";
				break;
			case TRIGGER_FALLING:
				propertyStr = "TF";
				break;
			case TRIGGER_RISING:
				propertyStr = "TR";
				break;
			case COUNT:
				throw new RuntimeException("unexpected creation of UID for LENGTH property");
		}
		
		return elementUID + UID_PROPERTY_SEPARATOR + propertyStr;
	}
	
	public static Collection<IExecutor> getVisibleExecutors(PTSPackageDeclaration pkgDecl)
	{
		Collection<IExecutor> executors = new LinkedList<IExecutor>();
		
		if (pkgDecl == null || pkgDecl.getTarget() == null)
			return executors;
		
		executors.addAll(pkgDecl.getTarget().getList().getActualTargets());
		
		return executors;
	}
	
	public static Collection<IExecutor> getVisibleExecutors(PTSTestDeclaration testDecl)
	{
		LinkedList<IExecutor> list = new LinkedList<IExecutor>();
		
		if (testDecl == null || testDecl.getTarget() == null || testDecl.getTarget().getList() == null)
			return list;
		
		for (IExecutor exec : testDecl.getTarget().getList().getActualTargets())
		{
			if (!exec.eIsProxy())
				list.add(exec);
		}
		
		return list;
	}

	public static Collection<IExecutor> getVisibleExecutors(PTSRealtimeBlock rtBlock)
	{
		PTSTestDeclaration testDecl = EcoreUtils.getContainerInstanceOf(rtBlock, PTSTestDeclaration.class);
		if (testDecl != null)
			return getVisibleExecutors(testDecl);
		
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(rtBlock, PTSPackageDeclaration.class);
		if (pkgDecl != null)
			return getVisibleExecutors(pkgDecl);

		throw new RuntimeException("Detected bad tree hierarchy for PTSTraceBlock");
	}
	
	public static Collection<IExecutor> getVisibleExecutors(PTSImplementation impl)
	{
		PTSScopeStatement scopeStm = EcoreUtils.getContainerInstanceOf(impl, PTSScopeStatement.class);
		if (scopeStm != null)
			return Collections.singletonList(getExplicitExecutor(scopeStm));
		
		PTSTestDeclaration testDecl = EcoreUtils.getContainerInstanceOf(impl, PTSTestDeclaration.class);
		if (testDecl != null)
			return getVisibleExecutors(testDecl);
		
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(impl, PTSPackageDeclaration.class);
		if (pkgDecl != null)
			return getVisibleExecutors(pkgDecl);

		throw new RuntimeException("Detected bad tree hierarchy above PTSDefaultImplementation");
	}
	
	public static IExecutor getExplicitExecutor(PTSTestDeclaration testDecl)
	{
		try
		{
			if (testDecl.getDefaultExecutor() != null)
				return testDecl.getDefaultExecutor().getActualExecutor();
			
			if (testDecl.getTarget().getList().getActualTargets().size() == 1)
				return testDecl.getTarget().getList().getActualTargets().get(0);

			return null;
		}
		catch (NullPointerException ex)
		{
			return null;
		}
	}
	
	public static IExecutor getExplicitExecutor(PTSScopeStatement scopeStm)
	{
		if (scopeStm.getExecutor() != null)
			return scopeStm.getExecutor().getActualExecutor();
		
		throw new RuntimeException("PTSScopeStatement without executor detected");
	}
	
	public static IExecutor getExplicitExecutor(PTSPackageFunction pkgFunc)
	{
		if (pkgFunc.isWithDefault() && pkgFunc.getExecutor() != null)
			return pkgFunc.getExecutor().getActualExecutor();
		else
			return null;
	}
	
	public static IExecutor getAssociatedExecutor(EObject ptsElement)
	{
		if (PTSpecUtils.isTraceAnalyzeContext(ptsElement))
			return null;
		
		if (ptsElement instanceof PTSStopStatement)
		{
			if (((PTSStopStatement) ptsElement).getExecutor() != null)
				return ((PTSStopStatement) ptsElement).getExecutor().getActualExecutor();
		}
		else if (ptsElement instanceof PTSRunStatement)
		{
			if (((PTSRunStatement) ptsElement).getExecutor() != null)
				return ((PTSRunStatement) ptsElement).getExecutor().getActualExecutor();
		}
		else if (ptsElement instanceof PTSRunUntilStatement)
		{
			// nothing to do
		}
		else
		{
			PTSValueSymbol valSym = EcoreUtils.getContainerInstanceOf(ptsElement, PTSValueSymbol.class);
			if (valSym == null)
			{
				System.err.println("Error: getAssociatedExecutor() called for element above PTSValueSymbol in hierarchy");
				throw new RuntimeException("getAssociatedExecutor() called for element above PTSValueSymbol in hierarchy");
			}
			
			if (valSym instanceof PTSSymbolReference)
			{
				if (((PTSSymbolReference) valSym).getBaseSymbol() instanceof IExecutor)
				{
					return (IExecutor)((PTSSymbolReference) valSym).getBaseSymbol();
				}
			}
		}

		PTSScopeStatement scopeStm = EcoreUtils.getContainerInstanceOf(ptsElement, PTSScopeStatement.class);
		if (scopeStm != null)
			return getExplicitExecutor(scopeStm);

		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(ptsElement, PTSPackageFunction.class);
		if (pkgFunc != null)
			return getExplicitExecutor(pkgFunc);

		PTSTestDeclaration testDecl = EcoreUtils.getContainerInstanceOf(ptsElement, PTSTestDeclaration.class);
		if (testDecl != null)
			return getExplicitExecutor(testDecl);

		throw new RuntimeException("Cannot get IExecutor");
	}

	public static List<IInspector> getVisibleInspectors(PTSTestDeclaration testDeclaration)
	{
		LinkedList<IInspector> inspectors = new LinkedList<IInspector>();
		
		for (IExecutor exec : getVisibleExecutors(testDeclaration))
		{
			IInspector inspector = ModelUtils.getInspector(exec);
			
			if (inspector != null)
				inspectors.add(ModelUtils.getInspector(exec));
		}
		
		return inspectors;
	}
	
	public static List<IInspector> getVisibleInspectors(PTSPackageDeclaration pkgDecl)
	{
		LinkedList<IInspector> inspectors = new LinkedList<IInspector>();
		
		for (IExecutor exec : getVisibleExecutors(pkgDecl))
		{
			IInspector inspector = ModelUtils.getInspector(exec);
			
			if (inspector != null)
				inspectors.add(ModelUtils.getInspector(exec));
		}
		
		return inspectors;
	}

	
	public static IInspector getAssociatedInspector(EObject ptsElement)
	{
		IExecutor exec = getAssociatedExecutor(ptsElement);
		return ModelUtils.getInspector(exec);
	}

	public static Collection<PTSUnitDeclaration> getVisibleUnits(PTSTestDeclaration testDecl)
	{
		PTSRoot root = EcoreUtils.getContainerInstanceOf(testDecl, PTSRoot.class);
		
		Collection<PTSUnitDeclaration> unitDecls = new LinkedList<PTSUnitDeclaration>();

		for (IReferenceableContainer ctr : root.getContainerDeclarations())
		{
			if (ctr instanceof PTSPackageDeclaration)
			{
				for (PTSPackageElement pkgEle : ((PTSPackageDeclaration) ctr).getPackageElements())
				{
					if (pkgEle instanceof PTSPackageUnit)
					{
						unitDecls.add(((PTSPackageUnit)pkgEle).getDeclaration());
					}
				}
			}
		}

		return unitDecls;
	}

	
	public static Collection<IDisplayable> getAllDisplayables(TestRigInstance tri)
	{
		LinkedList<IDisplayable> list = new LinkedList<IDisplayable>();
		
		TreeIterator<EObject> allInTestRig = tri.eAllContents();
		while (allInTestRig.hasNext())
		{
			EObject exec = allInTestRig.next();
			if (exec instanceof IExecutor)
			{
				ISymbolContainer container = ((IExecutor) exec).getSymbolContainer();
				
				if (container == null)
					continue;
				
				TreeIterator<EObject> allInSymbolContainer = container.eAllContents();
				while (allInSymbolContainer.hasNext())
				{
					EObject sym = allInSymbolContainer.next();
					if (sym instanceof IDisplayable)
					{
						list.add((IDisplayable)sym);
					}
				}
			}
		}
		
		return list;
	}
	

	public static PTSExpression getFinalInnerExpression(PTSExpression expression)
	{
		if (expression == null)
			return null;
		
		while (expression.getInnerExpression() != null)
			expression = expression.getInnerExpression();
		
		return expression;
	}
	
	private static ITestReferenceable getTestRefrenceableOrConstructWrapper(EObject ele)
	{
		if (ele == null)
			return null;
		
		if (ele instanceof ITestReferenceable)
			return (ITestReferenceable)ele;
		
		IWrappedReferenceable wrapper;
		if (ele instanceof JvmOperation)
		{
			wrapper = BaseelementsFactoryImpl.eINSTANCE.createTRWrapperCallable();
			wrapper.setName(((JvmOperation) ele).getSimpleName());
		}
		else if (ele instanceof JvmField)
		{
			wrapper = BaseelementsFactoryImpl.eINSTANCE.createTRWrapperRW();
			wrapper.setName(((JvmField) ele).getSimpleName());
		}
		else
		{
			throw new RuntimeException("Unexpected EObject instance for construction of an IWrappedReferenceable");
		}
		
		wrapper.setInnerObject(ele);
		return wrapper;
	}
	
	private static ITestReferenceable getElementByFQNIndex(PTSSymbolReference symRef, int n)
	{
		if (n == 0)
		{
			return symRef.getBaseSymbol();
		}
		else
		{
			return getTestRefrenceableOrConstructWrapper(
					symRef.getSubSymbols().get(n-1).getActualSymbol().getTestReferenceable());
		}
	}
	
	public static ITestReferenceable getFinalTestReferenceable(PTSSymbolReference symRef)
	{
		if (symRef.eIsSet(PTSpecPackage.Literals.PTS_SYMBOL_REFERENCE__SUB_SYMBOLS) && symRef.getSubSymbols().size() > 0)
		{
			return getElementByFQNIndex(symRef, symRef.getSubSymbols().size());
		}
		else
		{
			return symRef.getBaseSymbol();
		}
	}
	
	public static JvmIdentifiableElement getFinalJvmIdentifiable(PTSJavaReference jRef)
	{
		if (jRef.eIsSet(PTSpecPackage.Literals.PTS_JAVA_REFERENCE__SUB_JELEMENTS) && jRef.getSubJElements().size() > 0)
			return jRef.getSubJElements().get(jRef.getSubJElements().size() - 1).getActualJElement().getJElement();
		else
			return jRef.getBaseJElement();
	}
	
	public static ITestReferenceable getPredecessingTestReferenceable(PTSSubSymbolReference subSymbol)
	{
		PTSSymbolReference symRef = EcoreUtils.getContainerInstanceOf(subSymbol, PTSSymbolReference.class);

		int ownIdx = symRef.getSubSymbols().indexOf(subSymbol);
		
		if (ownIdx < 0)
		{
			throw new RuntimeException("Could not find provided PTSSubSymbolReference in provided PTSSymbolReference");
		}
		else if (ownIdx == 0)
		{
			return symRef.getBaseSymbol();
		}
		else
		{
			return getTestRefrenceableOrConstructWrapper(
					symRef.getSubSymbols().get(ownIdx - 1).getActualSymbol().getTestReferenceable());
		}
	}

	public static JvmIdentifiableElement getPredecessingJvmIdentifiable(PTSSubJavaReference subJRef)
	{
		PTSJavaReference jRef = EcoreUtils.getContainerInstanceOf(subJRef, PTSJavaReference.class);

		int ownIdx = jRef.getSubJElements().indexOf(subJRef);
		
		if (ownIdx < 0)
		{
			throw new RuntimeException("Could not find provided PTSSubJavaReference in provided PTSJavaReference");
		}
		else if (ownIdx == 0)
		{
			return jRef.getBaseJElement();
		}
		else
		{
			return jRef.getSubJElements().get(ownIdx - 1).getActualJElement().getJElement();
		}
	}
	
	public static Collection<TupleFirstKey<ITestReferenceable, PTSSymbolReference>> getElementsForTracing(PTSRealtimeStatement rtStm)
	{
		HashSet<TupleFirstKey<ITestReferenceable, PTSSymbolReference>> result = new HashSet<TupleFirstKey<ITestReferenceable, PTSSymbolReference>>();
		
		for (PTSPostRealtimeBlock prBlock : rtStm.getPostRealtimeBlocks())
		{
			TreeIterator<EObject> it = prBlock.eAllContents();
			
			while (it.hasNext())
			{
				EObject obj = it.next();
				if (obj instanceof PTSSymbolReference)
				{
					ITestReferenceable tRef = getFinalTestReferenceable((PTSSymbolReference)obj);
					if ((tRef instanceof ITestReadable || tRef instanceof ITestCallable)
						 && !EcoreUtils.hasContainerInstanceOf(tRef, PTSPackageDeclaration.class)
						 && !result.contains(tRef))
					{
						result.add(new TupleFirstKey<ITestReferenceable, PTSSymbolReference>(tRef, (PTSSymbolReference)obj));
					}
				}
			}
		}

		return result;
	}

	public static PTSStatement getLastStatement(PTSPackageFunction pkgFunc)
	{
		if (pkgFunc == null || pkgFunc.getImplementation() == null || pkgFunc.getImplementation().getStatements().size() == 0)
			return null;
		
		return pkgFunc.getImplementation().getStatements().get(pkgFunc.getImplementation().getStatements().size() - 1);
	}


	public static Collection<PTSAnalyzeBlock> getPredecessingAnalzeBlocks(PTSPostRealtimeBlock postRealtimeBlock)
	{
		LinkedList<PTSAnalyzeBlock> anaBlocks = new LinkedList<PTSAnalyzeBlock>();

		PTSRealtimeStatement rtStm = EcoreUtils.getContainerInstanceOf(postRealtimeBlock, PTSRealtimeStatement.class);
		
		int idx = rtStm.getPostRealtimeBlocks().indexOf(postRealtimeBlock);
		while(idx > 0)
		{
			idx--;
			PTSPostRealtimeBlock block = rtStm.getPostRealtimeBlocks().get(idx);
			if (block instanceof PTSAnalyzeBlock)
				anaBlocks.add((PTSAnalyzeBlock) block);
		}
		
		return anaBlocks;
	}
	
	public static PTSDataType getDataType(PTSSymbolReference symRef)
	{
		ITestReferenceable tRef = getFinalTestReferenceable(symRef);
		
		PTSDeclarator declarator = EcoreUtils.getContainerInstanceOf(tRef, PTSDeclarator.class);
		if (declarator != null)
			return declarator.getDataType();
		
		PTSPackageVariable pkgConst = EcoreUtils.getContainerInstanceOf(tRef, PTSPackageVariable.class);
		if (pkgConst != null)
			return pkgConst.getDataType();
		
		PTSPackageFuncParameter param = EcoreUtils.getContainerInstanceOf(tRef, PTSPackageFuncParameter.class);
		if (param != null)
			return param.getDataType();
		
		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(tRef, PTSPackageFunction.class);
		if (pkgFunc != null)
			return pkgFunc.getReturnDataType();
		
		return null;
	}
	

	public static void checkDataTypeCompatibility(PTSExpression expr) throws IncompatibleTypesException
	{
		getDataTypeIdentifier(expr);
	}
	
	public static void checkDataTypeCompatibility(PTSDeclarationStatement declStm) throws IncompatibleTypesException
	{
		if (declStm.isWithAssignment())
		{
			String dt1 = getDataTypeIdentifier(declStm.getDeclarator().getDataType(), false);
			String dt2 = getDataTypeIdentifier(declStm.getAssignment());

			verifyTypesCompatible(dt1, dt2, PTS_EOPERATOR.ASSIGN);
		}
	}

	
	private static Hashtable<String, LinkedList<String>> implictTypeConvertMap;
	static 
	{
		implictTypeConvertMap = new Hashtable<String, LinkedList<String>>();
		implictTypeConvertMap.put(
				PTS_EINTEGRALDATATYPE.DOUBLE.getLiteral(), new LinkedList<String>(Arrays.asList(
				PTS_EINTEGRALDATATYPE.INT8.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT16.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT32.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT64.getLiteral(),
				PTS_EINTEGRALDATATYPE.FLOAT.getLiteral()
				)));
		implictTypeConvertMap.put(
				PTS_EINTEGRALDATATYPE.FLOAT.getLiteral(), new LinkedList<String>(Arrays.asList(
				PTS_EINTEGRALDATATYPE.INT8.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT16.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT32.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT64.getLiteral()
				)));
		implictTypeConvertMap.put(
				PTS_EINTEGRALDATATYPE.INT64.getLiteral(), new LinkedList<String>(Arrays.asList(
				PTS_EINTEGRALDATATYPE.INT8.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT16.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT32.getLiteral()
				)));
		implictTypeConvertMap.put(
				PTS_EINTEGRALDATATYPE.INT32.getLiteral(), new LinkedList<String>(Arrays.asList(
				PTS_EINTEGRALDATATYPE.INT8.getLiteral(),
				PTS_EINTEGRALDATATYPE.INT16.getLiteral()
				)));
		implictTypeConvertMap.put(
				PTS_EINTEGRALDATATYPE.INT16.getLiteral(), new LinkedList<String>(Arrays.asList(
				PTS_EINTEGRALDATATYPE.INT8.getLiteral()
				)));
		implictTypeConvertMap.put(
				PTS_EINTEGRALDATATYPE.STRING.getLiteral(), new LinkedList<String>(Arrays.asList(
				PTS_EINTEGRALDATATYPE.VOID.getLiteral()
				)));
	}
	private static void verifyTypesCompatible(String lhs, String rhs, PTS_EOPERATOR op) throws IncompatibleTypesException
	{
		if (op == PTS_EOPERATOR.INSTANCE_OF)
		{
			if (!lhs.startsWith("java_") || !rhs.startsWith("java_"))
				throw new IncompatibleTypesException("'is' operator requires java types");
		}
		
		if (lhs == null || rhs == null)
		{
			// at least one type is "always compatible" (never checked)
			// TODO: remove this case when PTSpecUtils.getDataTypeIdentifier is fully implemented
			return;
		}
		
		if (rhs.equals(lhs))
		{
			// both types are equal
			return; 
		}
		
		if (lhs.startsWith("java_") && rhs.startsWith("java_"))
		{
			// both are a java type but not equal, currently ignored
			// !TODO implement check for compatibility
			//       (is not possible here, needs JVMIdentifiableElement to check inheritance)
			return;
		}
		
		if (implictTypeConvertMap.containsKey(lhs) && implictTypeConvertMap.get(lhs).contains(rhs))
		{
			// implicit conversion is possible
			return;
		}
		
		
		if (op == PTS_EOPERATOR.ADD && (lhs.equals(PTS_EINTEGRALDATATYPE.STRING.getLiteral()) || lhs.equals(PTS_EINTEGRALDATATYPE.STRING.getLiteral())))
		{
			// concatenation operator is possible
			return;
		}
		
		throw new IncompatibleTypesException("illegal expression " + lhs + " " + op.getLiteral() + " " + rhs);
	}
	
	
	private static String getDataTypeIdentifier(PTSExpression expression) throws IncompatibleTypesException
	{
		if (expression.isInstantiation())
		{
			return getDataTypeIdentifier(expression.getValueSymbol());
		}
		else if (expression.getOperationExpression() == null)
		{
			if (expression.isWithCast())
			{
				return getDataTypeIdentifier(expression.getCastDataType(), false);
			}
			else if (expression.isBracketed())
			{
				return getDataTypeIdentifier(expression.getInnerExpression());
			}
			else
			{
				return getDataTypeIdentifier(expression.getValueSymbol());
			}
		}
		else
		{
			String lhsType;
			if (expression.isBracketed())
			{
				lhsType = getDataTypeIdentifier(expression.getInnerExpression());
			}
			else
			{
				lhsType = getDataTypeIdentifier(expression.getValueSymbol());
			}
			
			String rhsType = getDataTypeIdentifier(expression.getOperationExpression().getRhs());
			
			if (lhsType == null || rhsType == null)
				return null;
			
			return PTSpecUtils.getDataTypeIdentifier(lhsType, rhsType, expression.getOperationExpression().getOp());
		}
	}

	private static String getDataTypeIdentifier(PTSDataType dataType, boolean isArrayAccess)
	{
		StringBuilder sb = new StringBuilder();
		if (dataType.isPhysicalType())
		{
			sb.append(DATATYPE_PHYSICAL_PREFIX);
			PTSUnitDeclaration unitDecl = PTSpecUtils.getBaseUnit(dataType.getUnit());
			if (unitDecl != null)
				sb.append(unitDecl.getName());
		}
		else
		{
			sb.append(dataType.getIntegralType().getLiteral());
		}
		
		if (dataType.isArray() && !isArrayAccess)
		{
			sb.append(DATATYPE_ARRAY_POSTFIX);
		}
		return sb.toString();
	}
	
	private static String getDataTypeIdentifier(PTSValueSymbol valueSymbol)
	{
		if (valueSymbol instanceof PTSSymbolReference)
		{
			return getDataTypeIdentifier((PTSSymbolReference)valueSymbol);
		}
		else if (valueSymbol instanceof PTSNumberConstant)
		{
			PTSNumberConstant c = (PTSNumberConstant) valueSymbol;
			
			if (c.getUnit() != null)
			{
				PTSUnitDeclaration unitDecl = PTSpecUtils.getBaseUnit(c.getUnit());
				return DATATYPE_PHYSICAL_PREFIX + unitDecl.getName();
			}
			else if (c.getValue().contains("."))
			{
				return PTS_EINTEGRALDATATYPE.DOUBLE.getLiteral();
			}
			else
			{
				long value = Long.decode(c.getValue()).longValue();
				
				if (value >= Character.MIN_VALUE && value <= Character.MAX_VALUE)
					return PTS_EINTEGRALDATATYPE.INT8.getLiteral();
				else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE)
					return PTS_EINTEGRALDATATYPE.INT16.getLiteral();
				else if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE)
					return PTS_EINTEGRALDATATYPE.INT32.getLiteral();
				else
					return PTS_EINTEGRALDATATYPE.INT64.getLiteral();
			}
		}
		else if (valueSymbol instanceof PTSStringConstant)
		{
			return PTS_EINTEGRALDATATYPE.STRING.getLiteral();
		}
		else if (valueSymbol instanceof PTSSpecialConstant)
		{
			PTSSpecialConstant c = (PTSSpecialConstant) valueSymbol;
			if (c.getValue() == PTS_ESPECIALCONSTANT.TRUE ||
			    c.getValue() == PTS_ESPECIALCONSTANT.FALSE)
			{
				return PTS_EINTEGRALDATATYPE.BOOL.getLiteral();
			}
			else if (c.getValue() == PTS_ESPECIALCONSTANT.NULL)
			{
				return PTS_EINTEGRALDATATYPE.VOID.getLiteral();
			}
			else
			{
				throw new PTSCompilerException("Unknown PTSSpecialConstant " + c.getValue().getLiteral());
			}
		}
		else if (valueSymbol instanceof PTSRuntimeInstance)
		{
			return null; // TODO: return appropriate type depending on selected property
		}
		else if (valueSymbol instanceof PTSJavaReference)
		{
			return getDataTypeIdentifier(getFinalJvmIdentifiable((PTSJavaReference)valueSymbol));
		}
		else
		{
			return null; // TODO: return appropriate type
		}
	}
	
	private static String getDataTypeIdentifier(PTSSymbolReference symRef)
	{
		PTSDataType dataType = getDataType(symRef);
		boolean isArrayAccess = symRef.isArrayAccess();
		
		if (symRef.isPropertyAccess())
		{
			// !TODO: return appropriate data type
			return null;
		}
		else if (symRef.isEventAccess())
		{
			// !TODO: return appropriate data type
			return null;
		}
		else if (dataType != null)
		{
			return getDataTypeIdentifier(dataType, isArrayAccess);
		}
		else
		{
			ITestReferenceable testRef = getFinalTestReferenceable(symRef);

			if (testRef instanceof Symbol)
			{
				DataType type = ((Symbol) testRef).getDataType();
				
				if (type == null)
				{
					System.err.println("Symbol without datatype: " + testRef.getName());
					throw new PTSCompilerException("Symbol without datatype: " + testRef.getName());
				}
				else if(type instanceof DTInteger)
				{
					if (((DTInteger) type).getBitSize() <= 8)
						return PTS_EINTEGRALDATATYPE.INT8.getLiteral();
					else if (((DTInteger) type).getBitSize() <= 16)
						return PTS_EINTEGRALDATATYPE.INT16.getLiteral();
					else if (((DTInteger) type).getBitSize() <= 32)
						return PTS_EINTEGRALDATATYPE.INT32.getLiteral();
					else if (((DTInteger) type).getBitSize() <= 64)
						return PTS_EINTEGRALDATATYPE.INT64.getLiteral();
					else
					{
						System.err.println("Symbol with integer data type of unsupported bit size");
						throw new PTSCompilerException("Symbol with integer data type of unsupported bit size");
					}
				}
				else if(type instanceof DTFloatingPoint)
				{
					DTFloatingPoint fpType = (DTFloatingPoint) type;
					if (fpType.getExponentBitSize() == 8 &&
						fpType.getSignificandBitSize() == 23)
						return PTS_EINTEGRALDATATYPE.FLOAT.getLiteral();
					else if (fpType.getExponentBitSize() == 11 &&
						fpType.getSignificandBitSize() == 52)
						return PTS_EINTEGRALDATATYPE.DOUBLE.getLiteral();
					else
					{
						System.err.println("Symbol with floating point data type of unsupported format");
						throw new PTSCompilerException("Symbol with floating point data type of unsupported format");
					}
				}
				else if (type instanceof DTVoid)
				{
					return PTS_EINTEGRALDATATYPE.VOID.getLiteral();
				}
				else
				{
					// !TODO: return appropriate data type
					System.err.println("Unknown Symbol data type " + type.getClass().getSimpleName() + " when fetching Java data type");
					throw new PTSCompilerException("Unknown Symbol data type " + type.getClass().getSimpleName() + " when fetching Java data type");
				}
			}
			else if (testRef instanceof ISignal)
			{
				return DATATYPE_PHYSICAL_PREFIX + ((ISignal)testRef).getPhysicalUnit();
			}
			else if (testRef instanceof PTSPackageFuncParameterDeclaration)
			{
				PTSPackageFuncParameter fParam = EcoreUtils.getContainerInstanceOf(testRef, PTSPackageFuncParameter.class);
				return getDataTypeIdentifier(fParam.getDataType(), isArrayAccess);
			}
			else if (testRef instanceof PTSTestVariableDeclaration)
			{
				// Type for PTSTestVariableDeclaration should have been handled at the beginning of this function
				// if we get here DataType of the declaration must be unset, thus JavaType must be set.
				PTSDeclarator declarator = EcoreUtils.getContainerInstanceOf(testRef, PTSDeclarator.class);
				if (declarator.getJavaType() != null)
				{
					return getDataTypeIdentifier(PTSpecUtils.getFinalJvmIdentifiable((PTSJavaReference)declarator.getJavaType()));
				}
				else
				{
					System.err.println("DEBUG: Unexpected PTSTestVariableDeclaration during data type identifier calculation");
					throw new PTSCompilerException("Unexpected PTSTestVariableDeclaration during data type identifier calculation");
				}
			}
			else if (testRef instanceof IWrappedReferenceable)
			{
				EObject innerObject = ((IWrappedReferenceable) testRef).getInnerObject();
				if (innerObject instanceof JvmIdentifiableElement)
				{
					return getDataTypeIdentifier((JvmIdentifiableElement)innerObject);
				}
				else
				{
					System.err.println("DEBUG: Unexpected IWrappedReferenceable during data type identifier calculation");
					throw new PTSCompilerException("Unexpected IWrappedReferenceable during data type identifier calculation");
				}
			}
			else if (testRef instanceof IExecutor)
			{
				return null;
			}
			else
			{
				System.err.println("DEBUG: Unknown TestRef type " + testRef.getClass().getSimpleName() + " when fetching Java data type");
				throw new PTSCompilerException("Unknown TestRef type " + testRef.getClass().getSimpleName() + " when fetching Java data type");
			}
		}
	}
	
	private static String getDataTypeIdentifier(JvmIdentifiableElement ele)
	{
		JvmType jvmType;
		if (ele instanceof JvmOperation)
		{
			jvmType = ((JvmOperation) ele).getReturnType().getType();
		}
		else if (ele instanceof JvmField)
		{
			jvmType = ((JvmField) ele).getType().getType();
		}
		else if (ele instanceof JvmGenericType)
		{
			return "java_" + ele.getIdentifier();
		}
		else
		{
			throw new RuntimeException("error retrieving datatype identifier for java type");
		}

		if (jvmType instanceof JvmGenericType)
			return "java_" + jvmType.getIdentifier();
		
		String result = jvmType.getIdentifier();
		
		if (result.equals("java.lang.String"))
			return PTS_EINTEGRALDATATYPE.STRING.getLiteral();
		else if (result.equals("char"))
			return PTS_EINTEGRALDATATYPE.INT8.getLiteral();
		else if (result.equals("short"))
			return PTS_EINTEGRALDATATYPE.INT16.getLiteral();
		else if (result.equals("int"))
			return PTS_EINTEGRALDATATYPE.INT32.getLiteral();
		else if (result.equals("long"))
			return PTS_EINTEGRALDATATYPE.INT64.getLiteral();
		else if (result.equals("byte"))
			return PTS_EINTEGRALDATATYPE.INT8.getLiteral();
		else
			return result;
	}
	
	private static String getDataTypeIdentifier(Hashtable<String, Integer> result)
	{
		String resultString = DATATYPE_PHYSICAL_PREFIX;
		
		boolean firstSegment = true;
		for (String typeString : result.keySet())
		{
			Integer exponent = result.get(typeString);

			if (exponent.equals(0))
				continue;
			
			if (!firstSegment)
				resultString += DATATYPE_PHYSICAL_SEPARATOR;
			firstSegment = false;
			
			resultString += typeString;
			
			if (exponent.equals(1))
			{
				continue;
			}
			
			String exponentStr = exponent.toString();
			for (int idx=0; idx<exponentStr.length(); idx++)
			{
				char c = exponentStr.charAt(idx);
				if (c == '-')
					resultString += DATATYPE_PHYSICAL_SUPERMINUS;
				else
					resultString += DATATYPE_PHYSICAL_SUPERSCR[Integer.parseInt("" + c) + 1];
			}
		}
		return resultString;
	}
	
	private static String getDataTypeIdentifier(String id1, String id2, PTS_EOPERATOR op) throws IncompatibleTypesException
	{
		Hashtable<String, Integer> phys1 = null;
		Hashtable<String, Integer> phys2 = null;
		
		if (id1.startsWith(DATATYPE_PHYSICAL_PREFIX))
		{
			phys1 = getPhysicalTypeSegments(id1.substring(DATATYPE_PHYSICAL_PREFIX.length()));
		}
		if (id2.startsWith(DATATYPE_PHYSICAL_PREFIX))
		{
			phys2 = getPhysicalTypeSegments(id2.substring(DATATYPE_PHYSICAL_PREFIX.length()));
		}
		
		switch (op)
		{
			case MULTIPLY:
			case DIVIDE:
				String result = getDataTypeIdentifier(phys1, phys2, op);
				if (result == null)
				{
					// !TODO set result to "bigger" type
					result = id1;
				}
				return result;
			case ADD:
			case SUBSTRACT:
				verifyTypesCompatible(id1, id2, op);
				// !TODO return "bigger" type
				return id1;
			case MODULO:
				// !TODO check that integer
				// !TODO return "bigger" unsigned type
				return id1;
			case EQUAL:
			case UN_EQUAL:
			case GREATER:
			case GREATER_EQUAL:
			case LOWER:
			case LOWER_EQUAL:
				verifyTypesCompatible(id1, id2, op);
				// !TODO: check signedness
				return PTS_EINTEGRALDATATYPE.BOOL.getLiteral();
			case BOOL_AND:
			case BOOL_OR:
				if (id1.equals(PTS_EINTEGRALDATATYPE.BOOL.getLiteral()) &&
					id2.equals(PTS_EINTEGRALDATATYPE.BOOL.getLiteral()))
				{
					return PTS_EINTEGRALDATATYPE.BOOL.getLiteral();
				}
				else
				{
					throw new IncompatibleTypesException("bool required");
				}
			case AND:
			case XOR:
			case OR:
				// !TODO check that integer
				// !TODO return "bigger" type
				return id1;
			case SHIFT_LEFT:
			case SHIFT_RIGHT:
			case SHIFT_RIGHT_ZERO:
				// !TODO check that integer
				return id1;
			case ASSIGN:
			case ASSIGN_ADD:
			case ASSIGN_SUBSTRACT:
				verifyTypesCompatible(id1, id2, op);
				return PTS_EINTEGRALDATATYPE.VOID.getLiteral();
			case ASSIGN_DIVIDE:
			case ASSIGN_MULTIPLY:
				if (phys1 != null || phys2 != null)
				{
					throw new IncompatibleTypesException("assign_mul and assign_div not allowed for physical");
				}
				verifyTypesCompatible(id1, id2, op);
				return PTS_EINTEGRALDATATYPE.VOID.getLiteral();
			case INSTANCE_OF:
				verifyTypesCompatible(id1, id2, op);
				return PTS_EINTEGRALDATATYPE.BOOL.getLiteral();
		}
		
		throw new RuntimeException("error in function getResultingType(); should not reach here.");
	}
	
	private static String getDataTypeIdentifier(Hashtable<String, Integer> lhs, Hashtable<String, Integer> rhs, PTS_EOPERATOR op) 
	{
		// caution, this function destroys lhs as it is used for output
		
		if (lhs == null && rhs == null)
			return null;
		
		if (lhs == null) lhs = new Hashtable<String, Integer>();
		if (rhs == null) rhs = new Hashtable<String, Integer>(); 
		
		for (String typeString : rhs.keySet())
		{
			if (lhs.containsKey(typeString))
			{
				Integer currentValue = lhs.get(typeString);
				Integer newValue = Integer.valueOf(0);
				if (op == PTS_EOPERATOR.MULTIPLY)
					newValue = currentValue + rhs.get(typeString);
				else if (op == PTS_EOPERATOR.DIVIDE)
					newValue = currentValue - rhs.get(typeString);
				else
					throw new RuntimeException("error calculating physical type; unexpected operator");
				lhs.put(typeString, newValue);
			}
			else
			{
				if (op == PTS_EOPERATOR.MULTIPLY)
					lhs.put(typeString, rhs.get(typeString));
				else if (op == PTS_EOPERATOR.DIVIDE)
					lhs.put(typeString, -1 * rhs.get(typeString));
				else
					throw new RuntimeException("error calculating physical type; unexpected operator");
			}
		}
	
		return getDataTypeIdentifier(lhs);
	}
	
	
	private static Hashtable<String, Integer> getPhysicalTypeSegments(String typeIdentifier)
	{
		Hashtable<String, Integer> segments = new Hashtable<String, Integer>();
		
		for (String typeSegment : typeIdentifier.split(DATATYPE_PHYSICAL_SEPARATOR))
		{
			String typeString = typeSegment;
			int exponent = 1;
			for (int idx=0; idx<typeSegment.length(); idx++)
			{
				boolean found = false;
				
				for (char c : DATATYPE_PHYSICAL_SUPERSCR)
				{
					if (typeSegment.charAt(idx) == c)
						found = true;
				}
				
				if (found)
				{
					typeString = typeSegment.substring(0, idx);
					String exponentStr = typeSegment.substring(idx);
					String exponentStrParsable = "";
					for (int expIdx=0; expIdx < exponentStr.length(); expIdx++)
					{
						for (int valueIdx=0; valueIdx<DATATYPE_PHYSICAL_SUPERSCR.length; valueIdx++)
						{
							if (exponentStr.charAt(expIdx) == DATATYPE_PHYSICAL_SUPERSCR[valueIdx])
							{
								if (valueIdx==0)
									exponentStrParsable += "-";
								else
									exponentStrParsable += valueIdx-1;
								
								break;
							}
						}
					}

					exponent = Integer.parseInt(exponentStrParsable);
					break;
				}
			}

			
			if (segments.containsKey(typeString))
			{
				Integer current = segments.get(typeString);
				segments.put(typeString, current + exponent);
			}
			else
			{
				segments.put(typeString, exponent);
			}
		}
		
		return segments;
	}

	public static PTSUnitDeclaration getBaseUnit(PTSUnitDeclaration unitDecl)
	{
		if (unitDecl == null)
			return null;
		
		while (true)
		{
			PTSPackageUnit pkgUnit = EcoreUtils.getContainerInstanceOf(unitDecl, PTSPackageUnit.class);

			if (pkgUnit.isDerived())
				unitDecl = pkgUnit.getBaseUnit();
			else
				return unitDecl;
		}
	}

	public static String getJvmIdentifiableName(PTSJavaReference jRef)
	{
		List<String> nameParts = new LinkedList<String>();
		
		nameParts.add(XtextUtils.getTextForFeature(jRef, PTSpecPackage.Literals.PTS_JAVA_REFERENCE__BASE_JELEMENT));
		
		for (PTSSubJavaReference jElement : jRef.getSubJElements())
		{
			nameParts.add(XtextUtils.getTextForFeature(jElement.getActualJElement(), PTSpecPackage.Literals.PTS_ACTUAL_JELEMENT__JELEMENT));
		}

		return StringUtils.serializeCollection(nameParts, ".");
	}
	
	public static String getDisplayName(PTSDataType dataType)
	{
		String arrSize = "";
		if (dataType.isArray() && dataType.eIsSet(PTSpecPackage.Literals.PTS_DATA_TYPE__ARRAY_SIZE_EXPRESSION))
			arrSize = NodeModelUtils.findActualNodeFor(dataType.getArraySizeExpression()).getText().trim();
				
		return dataType.getIntegralType().getLiteral() + (dataType.isArray() ? "["+arrSize+"]" : "");
	}
	
    public static String getDisplayName(EClass clazz)
    {
    	if (PTSpecPackage.Literals.PTS_TARGET_DECLARATION.isSuperTypeOf(clazz))
    		return "Target";
    	if (PTSpecPackage.Literals.PTS_PACKAGE_DECLARATION.isSuperTypeOf(clazz))
    		return "Package";
    	else if (PTSpecPackage.Literals.PTS_TEST_VARIABLE_DECLARATION.isSuperTypeOf(clazz))
    		return "Variable";
    	else if (PTSpecPackage.Literals.PTS_PACKAGE_FUNC_PARAMETER_DECLARATION.isSuperTypeOf(clazz))
    		return "Parameter";
    	else if (PTSpecPackage.Literals.PTS_PACKAGE_FUNCTION_DECLARATION.isSuperTypeOf(clazz))
    		return "Function";
    	else if (PTSpecPackage.Literals.PTS_PACKAGE_VARIABLE_DECLARATION.isSuperTypeOf(clazz))
    		return "Constant";
    	else if (PTSpecPackage.Literals.PTS_TEST_DECLARATION.isSuperTypeOf(clazz))
    		return "Test";
    	else if (PTSpecPackage.Literals.PTS_RUNTIME_INSTANCE.isSuperTypeOf(clazz))
    		return "Runtime Accessor";
		else if (PTSpecPackage.Literals.PTS_JAVA_REFERENCE.isSuperTypeOf(clazz))
			return "Java Accessor";
		else if (PTSpecPackage.Literals.PTS_CONSTANT.isSuperTypeOf(clazz))
			return "Constant";
		else if (PTSpecPackage.Literals.PTS_UNIT_DECLARATION.isSuperTypeOf(clazz))
			return "Physical Unit";
		else if (PTSpecPackage.Literals.PTSE_WRAP_PROPERTY.isSuperTypeOf(clazz))
			return "Property";
		else if (PTSpecPackage.Literals.PTSE_WRAP_EVENT.isSuperTypeOf(clazz))
			return "Event";
		else if (PTSpecPackage.Literals.PTSE_WRAP_PREFIX_OPERATOR.isSuperTypeOf(clazz))
			return "Operator";    	
    	
		else if (CodemodelPackage.Literals.CODE_INSTANCE.isSuperTypeOf(clazz))
			return "Program";
    	else if (CodemodelPackage.Literals.CCLASS.isSuperTypeOf(clazz))
    		return "Program Class";
    	else if (CodemodelPackage.Literals.VARIABLE.isSuperTypeOf(clazz))
    		return "Program Variable";
    	else if (CodemodelPackage.Literals.FUNCTION.isSuperTypeOf(clazz))
    		return "Program Function";
    	
		else if (EnvironmentdatamodelPackage.Literals.ENVIRONMENT_DATA_INSTANCE.isSuperTypeOf(clazz))
			return "Environment Data";
		else if (EnvironmentdatamodelPackage.Literals.ENVIRONMENT_SIGNAL.isSuperTypeOf(clazz))
			return "Signal (Environment)";

		else if (TestrigmodelPackage.Literals.TEST_RIG_INSTANCE.isSuperTypeOf(clazz))
			return "Testrig";
		else if (TestrigmodelPackage.Literals.PROCESSOR_CORE.isSuperTypeOf(clazz))
			return "Processor";
		else if (TestrigmodelPackage.Literals.THREAD.isSuperTypeOf(clazz))
			return "Thread";
		else if (TestrigmodelPackage.Literals.IO_PIN.isSuperTypeOf(clazz))
			return "IO Pin";
		else if (TestrigmodelPackage.Literals.IO_PORT.isSuperTypeOf(clazz))
			return "IO Port";
		else if (TestrigmodelPackage.Literals.UUT.isSuperTypeOf(clazz))
			return "Unit Under Test";
		else if (TestrigmodelPackage.Literals.PIN_SIGNAL.isSuperTypeOf(clazz))
			return "Signal (IO Pin)";

		else if (DatastreammodelPackage.Literals.DATA_STREAM_INSTANCE.isSuperTypeOf(clazz))
			return "Data Stream";
		else if (DatastreammodelPackage.Literals.CAN_MESSAGE.isSuperTypeOf(clazz))
			return "Nachricht";
		else if (DatastreammodelPackage.Literals.MESSAGE_SIGNAL.isSuperTypeOf(clazz))
			return "Signal (Network)";

    	else
    		return clazz.getName();
    	
    	// !TODO: add display names for new elements here
    }
	
	public static String getImageName(EClass clazz)
	{
		if (PTSpecPackage.Literals.PTS_PACKAGE_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test_package.gif";
		else if (PTSpecPackage.Literals.PTS_TEST_VARIABLE_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test_code_var.gif";
		else if (PTSpecPackage.Literals.PTS_PACKAGE_FUNC_PARAMETER_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test_code_var.gif";
		else if (PTSpecPackage.Literals.PTS_PACKAGE_VARIABLE_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test_code_const.gif";
		else if (PTSpecPackage.Literals.PTS_PACKAGE_FUNCTION_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test_code_func.gif";
		else if (PTSpecPackage.Literals.PTS_TARGET_DECLARATION.isSuperTypeOf(clazz))
			return "pts_target.gif";
		else if (PTSpecPackage.Literals.PTS_TEST_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test.gif";
		else if (PTSpecPackage.Literals.PTS_TEST_SUITE_DECLARATION.isSuperTypeOf(clazz))
			return "pts_test_suite.gif";

		else if (CodemodelPackage.Literals.CODE_INSTANCE.isSuperTypeOf(clazz))
			return "CodeInstance.gif";
		else if (CodemodelPackage.Literals.CCLASS.isSuperTypeOf(clazz))
			return "CClass.gif";
		else if (CodemodelPackage.Literals.FUNCTION.isSuperTypeOf(clazz))
			return "Function.gif";
		else if (CodemodelPackage.Literals.VARIABLE.isSuperTypeOf(clazz))
			return "Variable.gif";

		else if (EnvironmentdatamodelPackage.Literals.ENVIRONMENT_DATA_INSTANCE.isSuperTypeOf(clazz))
			return "EnvironmentDataInstance.gif";
		else if (EnvironmentdatamodelPackage.Literals.ENVIRONMENT_SIGNAL.isSuperTypeOf(clazz))
			return "EnvironmentSignal.gif";

		else if (TestrigmodelPackage.Literals.TEST_RIG_INSTANCE.isSuperTypeOf(clazz))
			return "TestRigInstance.gif";
		else if (TestrigmodelPackage.Literals.PROCESSOR_CORE.isSuperTypeOf(clazz))
			return "ProcessorCore.gif";
		else if (TestrigmodelPackage.Literals.THREAD.isSuperTypeOf(clazz))
			return "Thread.gif";
		else if (TestrigmodelPackage.Literals.IO_PIN.isSuperTypeOf(clazz))
			return "IOPin.gif";
		else if (TestrigmodelPackage.Literals.IO_PORT.isSuperTypeOf(clazz))
			return "IOPort.gif";
		else if (TestrigmodelPackage.Literals.UUT.isSuperTypeOf(clazz))
			return "UUT.gif";
		else if (TestrigmodelPackage.Literals.PIN_SIGNAL.isSuperTypeOf(clazz))
			return "PinSignal.gif";

		else if (DatastreammodelPackage.Literals.DATA_STREAM_INSTANCE.isSuperTypeOf(clazz))
			return "DataStreamInstance.gif";
		else if (DatastreammodelPackage.Literals.CAN_MESSAGE.isSuperTypeOf(clazz))
			return "CANMessage.gif";
		else if (DatastreammodelPackage.Literals.MESSAGE_SIGNAL.isSuperTypeOf(clazz))
			return "MessageSignal.gif";

		else
			return null;
		
		// !TODO: add image names for new elements here
	}
    
		
	public static boolean containsExternalReference(PTSPackageFunctionDeclaration pkgFuncDecl, List<PTSPackageFunctionDeclaration> alreadyVisitedFuncs)
	{
		if (alreadyVisitedFuncs == null)
			alreadyVisitedFuncs = new LinkedList<PTSPackageFunctionDeclaration>();
		
		if (alreadyVisitedFuncs.contains(pkgFuncDecl))
			return false;
		else
			alreadyVisitedFuncs.add(pkgFuncDecl);
		
		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(pkgFuncDecl, PTSPackageFunction.class);
		
		TreeIterator<EObject> ti = pkgFunc.getImplementation().eAllContents();
		
		while (ti.hasNext())
		{
			EObject elem = ti.next();
			
			if (elem instanceof PTSSymbolReference)
			{
				ITestReferenceable testRef = getFinalTestReferenceable((PTSSymbolReference)elem);
				
				if (isExternalReference(testRef))
				{
					return true;
				}
				else if (testRef instanceof PTSPackageFunctionDeclaration)
				{
					if (containsExternalReference((PTSPackageFunctionDeclaration)testRef, alreadyVisitedFuncs))
						return true;
				}
			}
		}

		return false;
	}

	@Deprecated
	public static boolean containsWriteToOrCallOfExternalReference(PTSPackageFunctionDeclaration pkgFuncDecl, List<PTSPackageFunctionDeclaration> alreadyVisitedFuncs)
	{
		if (alreadyVisitedFuncs == null)
			alreadyVisitedFuncs = new LinkedList<PTSPackageFunctionDeclaration>();

		if (alreadyVisitedFuncs.contains(pkgFuncDecl))
			return false;
		else
			alreadyVisitedFuncs.add(pkgFuncDecl);

		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(pkgFuncDecl, PTSPackageFunction.class);
		
		TreeIterator<EObject> ti = pkgFunc.getImplementation().eAllContents();
		
		while (ti.hasNext())
		{
			EObject elem = ti.next();
			
			if (elem instanceof PTSSymbolReference)
			{
				ITestReferenceable testRef = getFinalTestReferenceable((PTSSymbolReference)elem);

				if (isExternalReference(testRef))
				{
					if (isAssignment((PTSSymbolReference)elem) || isCall((PTSSymbolReference)elem))
						return true;
				}
				else if (testRef instanceof PTSPackageFunctionDeclaration)
				{
					return containsWriteToOrCallOfExternalReference((PTSPackageFunctionDeclaration)testRef, alreadyVisitedFuncs);
				}
			}
		}

		return false;
	}
	
	@Deprecated
	public static boolean containsWaitStatement(PTSPackageFunctionDeclaration pkgFuncDecl, List<PTSPackageFunctionDeclaration> alreadyVisitedFuncs)
	{
		if (alreadyVisitedFuncs == null)
			alreadyVisitedFuncs = new LinkedList<PTSPackageFunctionDeclaration>();

		if (alreadyVisitedFuncs.contains(pkgFuncDecl))
			return false;
		else
			alreadyVisitedFuncs.add(pkgFuncDecl);

		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(pkgFuncDecl, PTSPackageFunction.class);
		
		TreeIterator<EObject> ti = pkgFunc.getImplementation().eAllContents();
		
		while (ti.hasNext())
		{
			EObject elem = ti.next();
			
			if (elem instanceof PTSWaitDeltaStatement ||
				elem instanceof PTSWaitTimeStatement ||
				elem instanceof PTSWaitUntilStatement)
			{
				return true;
			}
			else if (elem instanceof PTSSymbolReference)
			{
				ITestReferenceable testRef = getFinalTestReferenceable((PTSSymbolReference)elem);

				if (testRef instanceof PTSPackageFunctionDeclaration)
				{
					return containsWaitStatement((PTSPackageFunctionDeclaration)testRef, alreadyVisitedFuncs);
				}
			}
		}

		return false;
	}

	
	public static boolean isExternalReference(ITestReferenceable testRef)
	{
		// !TODO: add other exernal elements here
		if (testRef instanceof IExecutor || 
			testRef instanceof Symbol ||
			testRef instanceof ISignal ||
			testRef instanceof IMessage)
			return true;
		else
			return false;
	}

	public static boolean isReferenceParamter(ITestReferenceable testRef)
	{
		PTSPackageFuncParameter fParam = EcoreUtils.getContainerInstanceOf(testRef, PTSPackageFuncParameter.class);
		return (testRef instanceof PTSPackageFuncParameterDeclaration && fParam != null && fParam.isReferenceAccess());
	}

	public static boolean isWaitTimeDuringTraceSupported(IExecutor executor)
	{
		if (executor instanceof ProcessorCore)
			return false;
		else if (executor instanceof Thread)
			return false;
		else if (executor instanceof IOPin)
			return true;
		else if (executor instanceof IOPort)
			return true;
		else
			throw new RuntimeException("Unknown executor type: " + executor.getClass().getSimpleName());
	}

	public static boolean isAssignmentOperator(PTS_EOPERATOR op)
	{
		switch (op)
		{
			case ASSIGN:
			case ASSIGN_ADD:
			case ASSIGN_SUBSTRACT:
			case ASSIGN_MULTIPLY:
			case ASSIGN_DIVIDE:
				return true;
			case ADD:
			case AND:
			case BOOL_AND:
			case BOOL_OR:
			case DIVIDE:
			case MODULO:
			case EQUAL:
			case GREATER:
			case GREATER_EQUAL:
			case LOWER_EQUAL:
			case LOWER:
			case MULTIPLY:
			case OR:
			case SUBSTRACT:
			case UN_EQUAL:
			case XOR:
			case SHIFT_LEFT:
			case SHIFT_RIGHT:
			case SHIFT_RIGHT_ZERO:
			case INSTANCE_OF:
				return false;
		}

		throw new RuntimeException("Unknown PTS_EOPERATOR: " + op.getName());
}

	public static boolean isAssignment(PTSValueSymbol valueSymbol)
	{
		PTSExpression expr = EcoreUtils.getContainerInstanceOf(valueSymbol, PTSExpression.class);

		if (expr.getOperationExpression() == null)
			return false;
		
		return isAssignmentOperator(expr.getOperationExpression().getOp());
	}
	
	public static boolean isCall(PTSValueSymbol valueSymbol)
	{
		PTSExpression expr = EcoreUtils.getContainerInstanceOf(valueSymbol, PTSExpression.class);
		
		return expr.isCall();
	}
	
	public static boolean isCallable(PTSValueSymbol valueSymbol)
	{
		if (valueSymbol instanceof PTSSymbolReference)
		{
			return PTSpecUtils.getFinalTestReferenceable((PTSSymbolReference)valueSymbol) instanceof ITestCallable;
		}
		else if (valueSymbol instanceof PTSJavaReference)
		{
			JvmIdentifiableElement ele = PTSpecUtils.getFinalJvmIdentifiable((PTSJavaReference)valueSymbol);
			return ele instanceof JvmDeclaredType || ele instanceof JvmOperation;
		}
		else
		{
			return false;
		}
	}

	public static boolean isCapture(PTSDeclarator declarator)
	{
		if (declarator == null)
			return false;
		
		PTSDeclarationStatement stm1 = EcoreUtils.getContainerInstanceOf(declarator, PTSDeclarationStatement.class);
		PTSDeclarationStatementForLoop stm2 = EcoreUtils.getContainerInstanceOf(declarator, PTSDeclarationStatementForLoop.class);
		
		if ((stm1 != null && stm1.isCapture()) ||
			(stm2 != null && stm2.isCapture()))
			return true;
		else
			return false;
	}

	public static boolean isReferringToArray(PTSSymbolReference symRef)
	{
		PTSDataType dataType = getDataType(symRef);
		
		if (dataType != null)
			return dataType.isArray();
		else
			return false;
	}

	public static boolean isConstant(PTSExpression expr)
	{
		if (expr.isCall())
			return false;
		
		if (expr.eIsSet(PTSpecPackage.Literals.PTS_EXPRESSION__VALUE_SYMBOL))
			if (!isConstant(expr.getValueSymbol()))
				return false;
		
		if (expr.eIsSet(PTSpecPackage.Literals.PTS_EXPRESSION__OPERATION_EXPRESSION))
			if (!isConstant(expr.getOperationExpression().getRhs()))
				return false;

		for (EObject e : expr.eClass().eContents())
		{
			if (e instanceof PTSExpression)
				if (!isConstant((PTSExpression)e))
					return false;
		}
		
		return true;
	}
	
	public static boolean isConstant(PTSValueSymbol valSym)
	{
		if (valSym instanceof PTSConstant)
			return true;
		
		if (valSym instanceof PTSSymbolReference)
		{
			ITestReferenceable tref = getFinalTestReferenceable((PTSSymbolReference) valSym);
			if (tref instanceof PTSPackageVariableDeclaration)
			{
				PTSPackageVariable pkgVar = EcoreUtils.getContainerInstanceOf(tref, PTSPackageVariable.class);
				if (pkgVar.isConst())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		if (valSym instanceof PTSJavaReference)
		{
			JvmIdentifiableElement jref = getFinalJvmIdentifiable((PTSJavaReference) valSym);
			if (jref instanceof JvmField)
				return ((JvmField) jref).isConstant();
		}
		
		return false;
	}

	public static boolean mightReferExternals(PTSPackageFunction pkgFunc)
	{
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(pkgFunc, PTSPackageDeclaration.class);
		
		return pkgDecl.getTarget() != null && pkgDecl.getTarget().getList().getActualTargets().size() > 0;
	}
	
	public static boolean isRealtimeContext(EObject ptsElement)
	{
		// in a realtime func
		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(ptsElement, PTSPackageFunction.class);
		if (pkgFunc != null && pkgFunc.isRealtimeFunc())
		{
			return true;
		}

		// within the curly braces of the realtime{} block
		return EcoreUtils.hasContainerInstanceOf(ptsElement, PTSRealtimeBlock.class);
	}
	
	public static boolean isTraceAnalyzeContext(EObject ptsElement)
	{
		// in an analyze func
		PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(ptsElement, PTSPackageFunction.class);
		if (pkgFunc != null && pkgFunc.isAnalyzeFunc())
		{
			return true;
		}
		
		// within the curly braces of the analyze{} block
		return EcoreUtils.hasContainerInstanceOf(ptsElement, PTSPostRealtimeBlock.class);
	}

	
	public static PTSRoot loadFullPTSModel(URI platformResourceURI) throws IOException
	{
		return (PTSRoot)EcoreUtils.loadFullEMFModel(platformResourceURI);
	}

	public static IScope computeSimpleAttributeNameScope(IScope other)
	{
		Collection<EObject> helperList = new LinkedList<EObject>();
		
		for (IEObjectDescription ele : other.getAllElements())
		{
			helperList.add(ele.getEObjectOrProxy());
		}
		
		return Scopes.scopeFor(helperList);
	}

	public static String getFQNForJavaTypeReference(PTSSymbolReference symRef)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<symRef.getSubSymbols().size() + 1; i++)
		{
			ITestReferenceable tRef = getElementByFQNIndex(symRef, i);
			if (i != 0)
				sb.append('.');
			sb.append(tRef.getName());
		}
		return sb.toString();
	}


	public static String getPlotMetaInfo_Axis(PTSPlotRangeExpression pre)
	{
		if (pre.isDecimalLog())
			return "lg";
		else if (pre.isNaturalLog())
			return "ln";
		else
			return "linear";
	}

	public static String getPlotMetaInfo_Line(PTSPlotRangeExpression pre)
	{
		if (pre.isDots())
			return "dots";
		else if (pre.isSampleHold())
			return "hold";
		else
			return "linear";
	}

	public static boolean isConcurrentThreadContext(PTSStatement stm)
	{
		return EcoreUtils.hasContainerInstanceOf(stm, PTSConcurrentStatement.class);
	}

	public static List<IInspector> getInspectorsParticipatingRealtime(PTSRealtimeStatement rtStm)
	{
		LinkedList<IInspector> inspectorsToRegister = new LinkedList<IInspector>();
		addInspectorsParticipatingRealtimeRecursive(rtStm.getRealtimeBlock(), new LinkedList<EObject>(), inspectorsToRegister);
		return inspectorsToRegister;
	}
	
	private static void addInspectorsParticipatingRealtimeRecursive(EObject rtContainer, List<EObject> alreadyVisitedContainers, List<IInspector> inspectorsToRegister)
	{
		if (alreadyVisitedContainers.contains(rtContainer))
			return;
		
		alreadyVisitedContainers.add(rtContainer);
		
		PTSTestDeclaration testDecl = EcoreUtils.getContainerInstanceOf(rtContainer, PTSTestDeclaration.class);
		if (testDecl != null)
			inspectorsToRegister.addAll(PTSpecUtils.getVisibleInspectors(testDecl));
		
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(rtContainer, PTSPackageDeclaration.class);
		if (pkgDecl != null)
			inspectorsToRegister.addAll(PTSpecUtils.getVisibleInspectors(pkgDecl));

		TreeIterator<EObject> it = rtContainer.eAllContents();
		while (it.hasNext())
		{
			EObject ele = it.next();
			
			if (ele instanceof PTSSymbolReference)
			{
				ITestReferenceable tRef = PTSpecUtils.getFinalTestReferenceable((PTSSymbolReference)ele);
				if (tRef instanceof PTSPackageFunctionDeclaration)
				{
					PTSPackageFunction innerFunc = EcoreUtils.getContainerInstanceOf(tRef, PTSPackageFunction.class);
					if (innerFunc.isRealtimeFunc())
					{
						addInspectorsParticipatingRealtimeRecursive(innerFunc, alreadyVisitedContainers, inspectorsToRegister);
					}
				}
			}
		}
	}
}
