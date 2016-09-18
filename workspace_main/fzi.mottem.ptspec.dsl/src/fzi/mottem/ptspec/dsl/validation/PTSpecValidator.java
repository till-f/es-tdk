package fzi.mottem.ptspec.dsl.validation;

import java.util.HashSet;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.validation.Check;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.IReferenceableContainer;
import fzi.mottem.model.baseelements.ISignal;
import fzi.mottem.model.baseelements.ISymbolContainer;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.common.IncompatibleTypesException;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSBreakAtStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSCallParameterList;
import fzi.mottem.ptspec.dsl.pTSpec.PTSContainerDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDataType;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarationStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarator;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSOperationExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageElement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunction;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariable;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRealtimeStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRunStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRunUntilStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRuntimeInstance;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStopStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSwitchCaseBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTargetDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTargetDefinitionList;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSValueSymbol;
import fzi.mottem.ptspec.dsl.pTSpec.PTSWaitDeltaStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;
import fzi.util.ecore.EcoreUtils;

/**
* Custom validation rules. 
*
* see http://www.eclipse.org/Xtext/documentation.html#validation
*/
public class PTSpecValidator extends AbstractPTSpecValidator
{
	
	public enum Severity { Info, Warning, Error, Fatal }


	// Checking for any duplicate names
	// ----------------------------------------------------------------------------

	@Check
	public void checkNoDuplicateTargetInDefinitionList(PTSTargetDeclaration targetDeclaration)
	{
		HashSet<IExecutor> alreadyVisited = new HashSet<IExecutor>();
		
		if (targetDeclaration.getList() == null)
			return;

		for (IExecutor executor : targetDeclaration.getList().getActualTargets())
		{
			if (alreadyVisited.contains(executor))
			{
				warning("Duplicates in target '" + targetDeclaration.getName() + "'", targetDeclaration, PTSpecPackage.Literals.PTS_TARGET_DECLARATION__LIST);
				break;
			}

			alreadyVisited.add(executor);
		}
	}
	
	@Check
	public void checkNoDuplicateContainerDeclaration(PTSRoot root)
	{
		HashSet<String> alreadyDefinedNames = new HashSet<String>();
		
		for (IReferenceableContainer decl : root.getContainerDeclarations())
		{
			if (alreadyDefinedNames.contains(decl.getName()))
			{
				error("Container identifier '" + decl.getName() + "' already used", decl, decl.eClass().getEStructuralFeature("name"));
			}
			else
			{
				alreadyDefinedNames.add(decl.getName());
			}
		}
	}
	
	@Check
	public void checkNoDuplicatePackageElement(PTSPackageDeclaration pkgDecl)
	{
		HashSet<String> alreadyDefinedNames = new HashSet<String>();
		
		for (PTSPackageElement pkgElem : pkgDecl.getPackageElements())
		{
			if (pkgElem instanceof PTSPackageFunction)
			{
				String name = ((PTSPackageFunction) pkgElem).getDeclaration().getName();
				hlp_checkNotContained(alreadyDefinedNames, name, true, pkgElem, pkgElem.eClass().getEStructuralFeature("declaration"), "Package element with name '" + name + "' already declared", Severity.Error);
			}

			if (pkgElem instanceof PTSPackageVariable)
			{
				String name = ((PTSPackageVariable) pkgElem).getDeclaration().getName();
				hlp_checkNotContained(alreadyDefinedNames, name, true, pkgElem, pkgElem.eClass().getEStructuralFeature("declaration"), "Package element with name '" + name + "' already declared", Severity.Error);
			}
		}
	}
	
	@Check
	public void checkNoDuplicateDeclarationInPackageFunction(PTSPackageFunction pkgFunc)
	{
		HashSet<String> alreadyDefinedNames = new HashSet<String>();
		
		hlp_checkNoDuplicateDeclaration(alreadyDefinedNames, pkgFunc, true);
	}
	

	@Check
	public void checkNoDuplicateDeclarationTest(PTSTestDeclaration testDecl)
	{
		HashSet<String> alreadyDefinedNames = new HashSet<String>();

		hlp_checkNoDuplicateDeclaration(alreadyDefinedNames, testDecl, true);
	}
	
	@Check
	public void checkTargetDefinitionListValid(PTSTargetDefinitionList list)
	{
		for (IExecutor target : list.getActualTargets())
		{
			ISymbolContainer symbolDB = target.getSymbolContainer();
			if (symbolDB == null)
			{
				hlp_reportMessage(
					"Target does not specify a symbol database", 
					list, 
					PTSpecPackage.Literals.PTS_TARGET_DEFINITION_LIST__ACTUAL_TARGETS, 
					list.getActualTargets().indexOf(target), 
					Severity.Warning);
			}

			IInspector inspector = null;
			
			try
			{
				inspector = ModelUtils.getInspector(target);
			}
			catch (RuntimeException ex)
			{
				ex.printStackTrace();
			}
			
			if (inspector == null)
			{
				hlp_reportMessage(
					"Target is not connected to an inspector", 
					list, 
					PTSpecPackage.Literals.PTS_TARGET_DEFINITION_LIST__ACTUAL_TARGETS, 
					list.getActualTargets().indexOf(target), 
					Severity.Warning);
			}
		}
	}
	

	// Syntax & context checking
	// ----------------------------------------------------------------------------

	@Check
	public void checkRunUntilStatementHasValidExpression(PTSRunUntilStatement runUntilStatement)
	{
		PTSSymbolReference symRef = (PTSSymbolReference) runUntilStatement.getSymbolReference();
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (!(finalTestRef instanceof ITestCallable) &&
			!(finalTestRef instanceof PTSContainerDeclaration))
		{
			hlp_reportMessage("Illegal run until statement: reference is not callable", runUntilStatement, Severity.Error);
		}
	}
	
	@Check
	public void checkBreakAtStatementHasValidExpression(PTSBreakAtStatement breakAtStatement)
	{
		PTSSymbolReference symRef = (PTSSymbolReference) breakAtStatement.getSymbolReference();
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (!(finalTestRef instanceof ITestCallable) &&
			!(finalTestRef instanceof PTSContainerDeclaration))
		{
			hlp_reportMessage("Illegal break at statement: reference is not callable", breakAtStatement, Severity.Error);
		}
	}
	
	@Check
	public void checkRunStatementInExecutorContext(PTSRunStatement runStatement)
	{
		if (PTSpecUtils.getAssociatedExecutor(runStatement) == null)
		{
			hlp_reportMessage("Illegal run statement: no executor in context", runStatement, Severity.Error);
		}
	}

	@Check
	public void checkStopStatementInExecutorContext(PTSStopStatement stopStatement)
	{
		if (PTSpecUtils.getAssociatedExecutor(stopStatement) == null)
		{
			hlp_reportMessage("Illegal stop statement: no executor in context", stopStatement, Severity.Error);
		}
	}

	@Check
	public void checkWaitDeltaStatementInTraceAnalyzeContextOnly(PTSWaitDeltaStatement waitDeltaStatement)
	{
		if (!PTSpecUtils.isTraceAnalyzeContext(waitDeltaStatement))
			hlp_reportMessage("Illegal wait delta statement; no trace available in context", waitDeltaStatement, Severity.Error);
	}
	
	@Check
	public void checkSymbolReferenceHasValidSymbolAsLastElementOnly(PTSSymbolReference symRef)
	{
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (finalTestRef == null)
			return;
		
		if (finalTestRef instanceof IReferenceableContainer && finalTestRef.eContainer() != null)
			hlp_reportMessage("Containers cannot be referenced in general code; their members can be accessed using '.'", symRef, Severity.Error);
		
		if (finalTestRef instanceof IExecutor && !symRef.isPropertyAccess())
			hlp_reportMessage("Executors cannot be referenced in general code; their properties can be accessed using ':'", symRef, Severity.Error);
	}

	@Check
	public void checkSymbolReferenceIsValid(PTSSymbolReference symRef)
	{
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (PTSpecUtils.isRealtimeContext(symRef))
		{
			if (finalTestRef instanceof Function)
			{
				if (EcoreUtils.hasContainerInstanceOf(symRef, PTSOperationExpression.class) ||
					EcoreUtils.hasContainerInstanceOf(symRef, PTSCallParameterList.class))
					hlp_reportMessage("Call to program function '" + finalTestRef.getName() + "' cannot be part of an operation in trace blocks", symRef, Severity.Error);
			}
		}
		else if (PTSpecUtils.isTraceAnalyzeContext(symRef))
		{
			if (PTSpecUtils.isExternalReference(finalTestRef))
			{
				if (PTSpecUtils.isAssignment(symRef))
				{
					hlp_reportMessage("External element '" + finalTestRef.getName() + "' cannot be target of an assignment in trace context", symRef, Severity.Error);
				}
				
				if (PTSpecUtils.isCall(symRef))
				{
					hlp_reportMessage("External element '" + finalTestRef.getName() + "' cannot be called from trace context", symRef, Severity.Error);
				}
			}
		}
		else
		{
			// Checks specific for non Trace Analyze context
			PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(finalTestRef, PTSPackageFunction.class);
			
			if (pkgFunc == null)
				return;

			if (pkgFunc.isAnalyzeFunc())
			{
				hlp_reportMessage("Use of analyze function outside analyze context not allowed", symRef, Severity.Error);
			}
			
			if (pkgFunc.isRealtimeFunc())
			{
				hlp_reportMessage("Use of realtime function outside realtime context not allowed", symRef, Severity.Error);
			}
		}
	}
	
	@Check
	public void checkCallableHasCallParameterList(PTSValueSymbol valueSymbol)
	{
		if (PTSpecUtils.isCallable(valueSymbol))
		{
			PTSExpression expr = EcoreUtils.getContainerInstanceOf(valueSymbol, PTSExpression.class);
			
			// !TODO: clearify how expr can be NULL here... (yes, it actually occurs sometimes)
			if (expr == null)
				return;
			
			if (valueSymbol instanceof PTSSymbolReference)
			{
				PTSSymbolReference symRef = (PTSSymbolReference) valueSymbol;
				if (symRef.isPropertyAccess() ||
					symRef.isEventAccess())
					return;
			}
			else if (valueSymbol instanceof PTSJavaReference)
			{
				JvmIdentifiableElement ele = PTSpecUtils.getFinalJvmIdentifiable((PTSJavaReference) valueSymbol);
				if (ele instanceof JvmGenericType)
					return;
			}
			
			if (!expr.isCall())
			{
				hlp_reportMessage("Missing call paramters for callable element", valueSymbol, Severity.Error);
			}
		}
	}
	
	@Check
	public void checkCallParameterListOnCallableOnly(PTSCallParameterList cpList)
	{
		PTSExpression expr = EcoreUtils.getContainerInstanceOf(cpList, PTSExpression.class);
		PTSValueSymbol valSym = expr.getValueSymbol();
		
		if (!PTSpecUtils.isCallable(valSym))
		{
			hlp_reportMessage("Invalid call to non-callable element", valSym.eContainer(), valSym.eContainingFeature(), Severity.Error);
		}
	}
	
	@Check
	public void checkArrayAccessOnValidElementsOnly(PTSSymbolReference symRef)
	{
		if (symRef.isArrayAccess() &&
			!PTSpecUtils.isReferringToArray(symRef))
		{
			hlp_reportMessage("Invalid array operator on scalar element detected", symRef, PTSpecPackage.Literals.PTS_SYMBOL_REFERENCE__ARRAY_ACCESS, Severity.Error);
		}
	}


	// Semantic checking
	// ----------------------------------------------------------------------------

	@Check
	public void checkDataTypeCompatibility(PTSExpression expr)
	{
		// type checking disabled because check of types in expressions has some flaws...
//		try
//		{
//			PTSpecUtils.checkDataTypeCompatibility(expr);
//		}
//		catch (IncompatibleTypesException e)
//		{
//			hlp_reportMessage("Type error: " + e.getReason(), expr, Severity.Error);
//		}
	}
	
	@Check
	public void checkDataTypeCompatibility(PTSDeclarationStatement declStm)
	{
		try 
		{
			PTSpecUtils.checkDataTypeCompatibility(declStm);
		} 
		catch (IncompatibleTypesException e) 
		{
			hlp_reportMessage("Type error: " + e.getReason(), declStm, Severity.Error);
		}
	}
	
	@Check
	public void checkCaseExpressionIsConstant(PTSSwitchCaseBlock caseBlock)
	{
		for (PTSExpression expr : caseBlock.getCaseExprs())
		{
			if (!PTSpecUtils.isConstant(expr))
			{
				hlp_reportMessage("Case expression must be a constant", expr, Severity.Error);
			}
		}
	}

	@Check
	public void checkRealtimeStatementOnlyInRegularContext(PTSRealtimeStatement stm)
	{
		if (PTSpecUtils.isRealtimeContext(stm) || PTSpecUtils.isTraceAnalyzeContext(stm))
		{
			hlp_reportMessage("Nested realtime statements and within 'realtime' or 'analyze' functions not allowed", stm, Severity.Error);
		}
	}

	@Check
	public void checkArrayDeclarationHasSize(PTSDataType dataType)
	{
		if (dataType.isArray() && dataType.getArraySizeExpression() == null)
		{
			hlp_reportMessage("Size of array is mandatory", dataType, Severity.Error);
		}
	}
	
	@Check
	public void checkRuntimePropertiesValid(PTSRuntimeInstance rtInstance)
	{
		switch(rtInstance.getProperty())
		{
			case GLOBAL_TIME:
				break;
			case END_OF_TRACE:
			case TIMESTAMP:
				if (!PTSpecUtils.isRealtimeContext(rtInstance) && !PTSpecUtils.isTraceAnalyzeContext(rtInstance))
					hlp_reportMessage("Properties 'EOT' and 'Timestamp' are not valid in this context", rtInstance, Severity.Error);
				break;
		}
	}
	
	@Check
	public void checkPropertiesValid(PTSSymbolReference symRef)
	{
		if (!symRef.isPropertyAccess())
			return;
		
		boolean ok = true;
		switch(symRef.getProperty().getEvalue())
		{
			case COUNT:
				// valid on Arrays
				ok = PTSpecUtils.isReferringToArray(symRef);
				break;
			case INSTRUCTION_POINTER:
				// valid on ProcessorCore
				ITestReferenceable finalTestRef1 = PTSpecUtils.getFinalTestReferenceable(symRef);
				ok = (finalTestRef1 == null) || (finalTestRef1 instanceof ProcessorCore);
				break;
			case ADDRESS:
				// valid on ProcessorCore
				ITestReferenceable finalTestRef2 = PTSpecUtils.getFinalTestReferenceable(symRef);
				ok = (finalTestRef2 == null) || (finalTestRef2 instanceof Symbol);
				break;
			case SAMPLE_RATE:
			case TRIGGER_ABOVE:
			case TRIGGER_BELOW:
			case TRIGGER_FALLING:
			case TRIGGER_RISING:
				// valid on IOPin
				ITestReferenceable finalTestRef3 = PTSpecUtils.getFinalTestReferenceable(symRef);
				ok = (finalTestRef3 == null) || (finalTestRef3 instanceof ISignal);
				break;
		}
		
		if (!ok)
			hlp_reportMessage("Invalid property '" + symRef.getProperty().getEvalue().getLiteral() + "'", symRef, Severity.Error);
	}

	
	
	// Some helper functions:
	// ----------------------------------------------------------------------------
	
	private void hlp_checkNoDuplicateDeclaration(HashSet<String> alreadyDefinedNames, EObject root, boolean doAdd)
	{
		TreeIterator<EObject> allChilds = root.eAllContents();
		while (allChilds.hasNext())
		{
			EObject child = allChilds.next();
			
			if (child instanceof PTSDeclarator)
			{
				String name = ((PTSDeclarator) child).getDeclaration().getName();
				hlp_checkNotContained(alreadyDefinedNames, name, doAdd, child, child.eClass().getEStructuralFeature("name"), "Local element '" + name + "' already declared", Severity.Error);
			}
		}
	}
	
	private <T> void hlp_checkNotContained(HashSet<T> itemList, T item, boolean doAdd, EObject source, EStructuralFeature feature, String message, Severity severity)
	{
		hlp_checkNotContained(itemList, item, doAdd, source, feature, -1, message, severity);
	}
	
	private <T> void hlp_checkNotContained(HashSet<T> itemList, T item, boolean doAdd, EObject source, EStructuralFeature feature, int idx, String message, Severity severity)
	{
		if (itemList.contains(item))
		{
			hlp_reportMessage(message, source, feature, idx, severity);
		}
		else
		{
			if (doAdd)
				itemList.add(item);
		}
	}
	
	private void hlp_reportMessage(String message, EObject source, Severity severity)
	{
		EStructuralFeature containingFeature = source.eContainingFeature();
		
		if (containingFeature.getUpperBound() == EStructuralFeature.UNBOUNDED_MULTIPLICITY)
		{
			// use EMF to get list of elements in which source is contained in and retrieve index
			@SuppressWarnings("unchecked")
			EObjectContainmentEList<EObject> elements = (EObjectContainmentEList<EObject>) source.eContainer().eGet(containingFeature);
			
			int idx = elements.indexOf(source);
			hlp_reportMessage(message, source.eContainer(), source.eContainingFeature(), idx, severity);
		}
		else
		{
			hlp_reportMessage(message, source.eContainer(), source.eContainingFeature(), -1, severity);
		}
	}

	private void hlp_reportMessage(String message, EObject source, EStructuralFeature feature, Severity severity)
	{
		hlp_reportMessage(message, source, feature, -1, severity);
	}

	private void hlp_reportMessage(String message, EObject source, EStructuralFeature feature, int idx, Severity severity)
	{
		switch (severity)
		{
			case Info:
				info(message, source, feature, idx);
				break;
			case Warning:
				warning(message, source, feature, idx);
				break;
			case Error:
				error(message, source, feature, idx);
				break;
			case Fatal:
				throw new RuntimeException(message);
			default:
				error(message, source, feature, idx);
				break;
		}
	}
	
}
