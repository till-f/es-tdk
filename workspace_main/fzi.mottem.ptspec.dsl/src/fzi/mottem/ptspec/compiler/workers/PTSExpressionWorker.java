package fzi.mottem.ptspec.compiler.workers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;

import fzi.mottem.jjet.interfaces.IJJETContext;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.IWrappedReferenceable;
import fzi.mottem.ptspec.compiler.PTSCompilerException;
import fzi.mottem.ptspec.compiler.util.PTS2JavaUtil;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSCallParameterList;
import fzi.mottem.ptspec.dsl.pTSpec.PTSConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDataType;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarator;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSOperationExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameterDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunction;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunctionDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPlotBlock;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRuntimeInstance;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSValueSymbol;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EOPERATOR;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ERUNTIMEPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;
import fzi.util.ecore.EcoreUtils;

public class PTSExpressionWorker
{

	IJJETContext _context;
	
	public PTSExpressionWorker(IJJETContext context)
	{
		_context = context;
	}

	public String getCodeFor(PTSExpression expr)
	{
		if (expr == null)
			return "";
		
		String prefix = "";
		if (expr.isInstantiation())
		{
			String callCode = "";
			if (expr.getCallParameterList() != null)
				callCode = getCodeFor(expr.getCallParameterList());
			return ("new " +  PTSpecUtils.getFinalJvmIdentifiable((PTSJavaReference)expr.getValueSymbol()).getQualifiedName() + "(" + callCode + ")");
		}
		else
		{
			if (expr.eIsSet(PTSpecPackage.Literals.PTS_EXPRESSION__PREFIX_OPERATOR))
			{
				prefix = PTS2JavaUtil.getJavaOperator(expr.getPrefixOperator().getEvalue());
			}
			else if (expr.isWithCast())
			{
				prefix = "(" + PTS2JavaUtil.getJavaDataType(expr.getCastDataType(), false, false) + ")";
			}
			
			String operationRhs = "";
			if (expr.eIsSet(PTSpecPackage.Literals.PTS_EXPRESSION__OPERATION_EXPRESSION))
			{
				operationRhs = PTS2JavaUtil.getJavaOperator(expr.getOperationExpression().getOp()) + getCodeFor(expr.getOperationExpression().getRhs());
			}
			
			if (expr.isCall() && !expr.isBracketed())
			{
				return prefix + getCodeToCall(expr.getValueSymbol(), expr.getCallParameterList()) + operationRhs;
			}
			else if (expr.isBracketed() && !expr.isCall())
			{
				return prefix + "(" + getCodeFor(expr.getInnerExpression()) + ")" + operationRhs;
			}
			else if (!expr.isBracketed() && !expr.isCall())
			{
				if (expr.eIsSet(PTSpecPackage.Literals.PTS_EXPRESSION__OPERATION_EXPRESSION))
				{
					return prefix + getCodeFor(expr.getValueSymbol(), expr.getOperationExpression());
				}
				else
				{
					return prefix + getCodeToRead(expr.getValueSymbol()) + operationRhs;
				}
			}
			else
			{
				throw new PTSCompilerException("Syntax error in expression");
			}
		}
	}

	private String getCodeFor(PTSValueSymbol valueSymbol, PTSOperationExpression opExpr)
	{
		PTS_EOPERATOR op = opExpr.getOp();
		
		if (PTSpecUtils.isAssignmentOperator(op))
		{
			return getCodeToWrite(valueSymbol, op, getCodeFor(opExpr.getRhs()));
		}
		else
		{
			return getCodeToRead(valueSymbol) + PTS2JavaUtil.getJavaOperator(op) + getCodeFor(opExpr.getRhs());
		}
	}
		
	private String getCodeFor(PTSCallParameterList callParamList)
	{
		if (callParamList == null)
			return "";
		
		StringBuilder argumentsCodeBuilder = new StringBuilder();
		
		boolean first = true;
		for (PTSExpression expr : callParamList.getExpressions())
		{
			if (!first)
				argumentsCodeBuilder.append(",");
			
			argumentsCodeBuilder.append(getCodeFor(expr));
			
			first = false;
		}
		
		return argumentsCodeBuilder.toString();
	}
	
	
	// Reading
	// ------------------------------------------------------
	
	private String getCodeToRead(PTSValueSymbol valueSymbol)
	{
		if (valueSymbol instanceof PTSConstant)
		{
			return PTS2JavaUtil.getJavaConstantValue((PTSConstant)valueSymbol);
		}
		else if (valueSymbol instanceof PTSSymbolReference)
		{
			return getCodeToRead((PTSSymbolReference)valueSymbol);
		}
		else if (valueSymbol instanceof PTSRuntimeInstance)
		{
			return getCodeToRead((PTSRuntimeInstance)valueSymbol);
		}
		else if (valueSymbol instanceof PTSJavaReference)
		{
			return getCodeToRead((PTSJavaReference)valueSymbol);
		}
		else
		{
			throw new PTSCompilerException("Invalid read from PTSValueSymbol type " + valueSymbol.getClass().getSimpleName() + " detected");
		}
	}

	private String getCodeToRead(PTSSymbolReference symRef)
	{
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (symRef.isReferenceAccess())
		{
			if (symRef.isEventAccess() || symRef.isArrayAccess() || (symRef.isPropertyAccess() && symRef.getProperty().getEvalue() == PTS_EPROPERTY.COUNT))
			{
				throw new PTSCompilerException("Invalid 'ref' for " + finalTestRef.getClass().getSimpleName() + " '" + finalTestRef.getName() + "' detected");
			}
			else
			{
				if (PTSpecUtils.isReferenceParamter(finalTestRef))
				{
					return finalTestRef.getName();
				}
				else
				{
					if (symRef.isPropertyAccess())
						return PTS2JavaUtil.getJavaPropertyUID(finalTestRef, symRef.getProperty().getEvalue());
					else
						return PTS2JavaUtil.getJavaElementUID(finalTestRef);
				}
			}
		}
		else if (symRef.isPropertyAccess() && symRef.getProperty().getEvalue() == PTS_EPROPERTY.COUNT)
		{
			PTSDataType dataType = PTSpecUtils.getDataType(symRef);
			if (dataType == null || !dataType.isArray())
				throw new PTSCompilerException("Invalid property 'Length' on " + finalTestRef.getClass().getSimpleName() + " '" + finalTestRef.getName() + "' detected");
			
			return finalTestRef.getName() + ".length";
		}
		else if (symRef.isPropertyAccess())
		{
			if (finalTestRef instanceof PTSTestVariableDeclaration ||
				finalTestRef instanceof PTSPackageFuncParameterDeclaration ||
				finalTestRef instanceof PTSPackageVariableDeclaration)
			{
				throw new PTSCompilerException("Invalid property on "+ finalTestRef.getClass().getSimpleName() + " '" + finalTestRef.getName() + "' detected");
			}
			else
			{
				switch(symRef.getProperty().getEvalue())
				{
					case INSTRUCTION_POINTER:
					case ADDRESS:
						String getItemPropertyValueArgs;
						if (PTSpecUtils.isTraceAnalyzeContext(symRef))
						{
							getItemPropertyValueArgs = PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," + PTS2JavaUtil.getJavaProperty(symRef.getProperty().getEvalue());
						}
						else if (PTSpecUtils.isRealtimeContext(symRef))
						{
							break;
						}
						else
						{
							getItemPropertyValueArgs = PTS2JavaUtil.getJavaInspectorUID(symRef) + "," + PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," + PTS2JavaUtil.getJavaProperty(symRef.getProperty().getEvalue());
						}
						return "((" + PTS2JavaUtil.getJavaPropertyDataType(symRef.getProperty().getEvalue()) + ")" + PTS2JavaUtil.getJavaEnvironmentProviderName(symRef) + ".getItemPropertyValue(" + getItemPropertyValueArgs + "))";
	
					case SAMPLE_RATE:
					case TRIGGER_ABOVE:
					case TRIGGER_BELOW:
					case TRIGGER_FALLING:
					case TRIGGER_RISING:
					case COUNT:
						break; // throws below
				}
				throw new PTSCompilerException("Invalid read from property '" + symRef.getProperty().getEvalue().getLiteral() + "' on " + finalTestRef.getClass().getSimpleName() + " '" + finalTestRef.getName() + "' detected");
			}
		}
		else if (symRef.isEventAccess())
		{
			String elementUID;
			if (PTSpecUtils.isReferenceParamter(finalTestRef))
			{
				elementUID = finalTestRef.getName();
			}
			else if (finalTestRef instanceof PTSTestVariableDeclaration ||
				finalTestRef instanceof PTSPackageVariableDeclaration ||
				finalTestRef instanceof PTSPackageFuncParameterDeclaration)
			{
				throw new PTSCompilerException("Event on " + finalTestRef.getClass().getSimpleName() + " '" + finalTestRef.getName() + "' detected");
			}
			else
			{
				elementUID = PTS2JavaUtil.getJavaElementUID(finalTestRef);
			}

			if (PTSpecUtils.isTraceAnalyzeContext(symRef))
			{
				return PTS2JavaUtil.getJavaTraceProviderName(symRef) + ".checkEvent(" + PTS2JavaUtil.getJavaEvent(symRef.getEvent().getEvalue()) +  "," + elementUID + ")";
			}
			else
			{
				return PTS2JavaUtil.getJavaEvent(symRef.getEvent().getEvalue()) +  "," + elementUID;
			}
		}
		else
		{
			if (finalTestRef instanceof IWrappedReferenceable)
			{
				EObject innerObject = ((IWrappedReferenceable) finalTestRef).getInnerObject();
				if (!(innerObject instanceof JvmIdentifiableElement))
					throw new PTSCompilerException("Unexpected type in IWrappedReferenceable: " + innerObject.getClass().getSimpleName());

				return PTSpecUtils.getFQNForJavaTypeReference(symRef);
			}
			else if (!PTSpecUtils.isReferenceParamter(finalTestRef) &&
				!EcoreUtils.hasContainerInstanceOf(symRef, PTSPlotBlock.class) &&
				(finalTestRef instanceof PTSTestVariableDeclaration ||
				 finalTestRef instanceof PTSPackageFuncParameterDeclaration))
			{
				if (symRef.isArrayAccess())
					return getArrayReadCode(finalTestRef.getName(), symRef);
				else
					return finalTestRef.getName();
			}
			else if (finalTestRef instanceof PTSPackageVariableDeclaration)
			{
				if (symRef.isArrayAccess())
					return getArrayReadCode(PTS2JavaUtil.getJavaAccessProviderName((PTSPackageVariableDeclaration)finalTestRef, symRef), symRef);
				else
					return PTS2JavaUtil.getJavaAccessProviderName((PTSPackageVariableDeclaration)finalTestRef, symRef);
			}
			else
			{
				String testRefType = PTS2JavaUtil.getJavaDataType(finalTestRef);
				
				String getValueArgs;
				if (PTSpecUtils.isTraceAnalyzeContext(symRef))
				{
					if (PTSpecUtils.isReferenceParamter(finalTestRef))
					{
						getValueArgs = finalTestRef.getName();
					}
					else
					{
						getValueArgs = PTS2JavaUtil.getJavaElementUID(finalTestRef);
					}
				}
				else
				{
					getValueArgs = PTS2JavaUtil.getJavaInspectorUID(symRef) + "," + PTS2JavaUtil.getJavaElementUID(finalTestRef);
				}

				return "((" + testRefType + ")" + PTS2JavaUtil.getJavaEnvironmentProviderName(symRef) + ".getValue(" + getValueArgs + "))";
			}
		}
	}

	private String getCodeToRead(PTSRuntimeInstance runtimeInstance)
	{
		String propertyFuncName = PTS2JavaUtil.getJavaRuntimePropertyFuncName(runtimeInstance.getProperty());
		
		if (runtimeInstance.getProperty() == PTS_ERUNTIMEPROPERTY.GLOBAL_TIME)
		{
			return PTS2JavaUtil.getJavaEnvironmentProviderName(runtimeInstance) + "." + propertyFuncName + "()";
		}
		else
		{
			return PTS2JavaUtil.getJavaTraceProviderName(runtimeInstance) + "." + propertyFuncName + "()";
		}
	}
	
	private String getCodeToRead(PTSJavaReference jRef)
	{
		JvmIdentifiableElement finalJElement = PTSpecUtils.getFinalJvmIdentifiable(jRef);
		
		if (finalJElement instanceof JvmField)
			return finalJElement.getQualifiedName();
		else if (finalJElement instanceof JvmGenericType)
			return finalJElement.getQualifiedName();
		else
			throw new PTSCompilerException("Reading from non-field Java element '" + finalJElement.getIdentifier() + "' detected");
	}
	
	
	// Writing
	// ------------------------------------------------------

	private String getCodeToWrite(PTSValueSymbol valueSymbol, PTS_EOPERATOR op, String assignmentCode)
	{
		if (valueSymbol instanceof PTSSymbolReference)
		{
			return getCodeToWrite((PTSSymbolReference)valueSymbol, op, assignmentCode);
		}
		else if (valueSymbol instanceof PTSJavaReference)
		{
			return getCodeToWrite((PTSJavaReference)valueSymbol, op, assignmentCode);
		}
		else
		{
			throw new PTSCompilerException("Invalid write to PTSValueSymbol type " + valueSymbol.getClass().getSimpleName() + " detected");
		}
	}

	private String getCodeToWrite(PTSSymbolReference symRef, PTS_EOPERATOR op, String assignmentCode)
	{
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);

		if (symRef.isPropertyAccess())
		{
			// write access to symRef's property
			
			if (finalTestRef instanceof PTSTestVariableDeclaration ||
				finalTestRef instanceof PTSPackageFuncParameterDeclaration ||
				finalTestRef instanceof PTSPackageVariableDeclaration)
			{
				throw new PTSCompilerException("Invalid write to property of test-internal elment '" + finalTestRef.getName() + "'");
			}
			else
			{
				if (op.getValue() != PTS_EOPERATOR.ASSIGN_VALUE)
					throw new PTSCompilerException("Invalid read-and-assign operation on property detected");

				switch(symRef.getProperty().getEvalue())
				{
					case INSTRUCTION_POINTER:
					case ADDRESS:
					case COUNT:
						break;
	
					case SAMPLE_RATE:
					case TRIGGER_ABOVE:
					case TRIGGER_BELOW:
					case TRIGGER_FALLING:
					case TRIGGER_RISING:
						if (PTSpecUtils.isTraceAnalyzeContext(symRef))
						{
							break;
						}
						else if (PTSpecUtils.isRealtimeContext(symRef))
						{
							// Write is embedded in a trace block; special API method must be called
							return PTS2JavaUtil.TRACE_INTANCE_NAME + ".addStimulus(" + 
								PTS2JavaUtil.getJavaInspectorUID(symRef) + "," +
								PTS2JavaUtil.getJavaExecutorUID(symRef) + 
								",ITrace.SetupEvent.WRITE," + 
								PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," +
								PTS2JavaUtil.getJavaProperty(symRef.getProperty().getEvalue()) + "," +
								assignmentCode +
								")";
						}
						else
						{
							return PTS2JavaUtil.RUNTIME_INSTANCE_NAME + ".setItemPropertyValue(" +
								PTS2JavaUtil.getJavaInspectorUID(symRef) + "," + 
								PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," + 
								PTS2JavaUtil.getJavaProperty(symRef.getProperty().getEvalue()) + "," + 
								assignmentCode + 
								")";
						}
				}

				throw new PTSCompilerException("Invalid write to read-only property detected");
			}
		}
		else if (symRef.isEventAccess())
		{
			// write access to symRef's event

			throw new PTSCompilerException("Invalid write to an event at element '" + finalTestRef.getName() + "'");
		}
		else
		{
			// write access to symRef itself
			if (finalTestRef instanceof IWrappedReferenceable)
			{
				EObject innerObject = ((IWrappedReferenceable) finalTestRef).getInnerObject();
				if (!(innerObject instanceof JvmIdentifiableElement))
					throw new PTSCompilerException("Unexpected type in IWrappedReferenceable: " + innerObject.getClass().getSimpleName());

				// assumes that base element is a local variable (which is always true for wrapped referenceables)
				// FIXME: this does not support recursive access of members of the referenced java object though...
				return PTSpecUtils.getFQNForJavaTypeReference(symRef) + PTS2JavaUtil.getJavaOperator(op) + assignmentCode;
			}
			else if (PTSpecUtils.isTraceAnalyzeContext(symRef) && !PTSpecUtils.isReferringToArray(symRef))
			{
				if (!(finalTestRef instanceof PTSTestVariableDeclaration ||
				      finalTestRef instanceof PTSPackageFuncParameterDeclaration))
					throw new PTSCompilerException("Invalid write to non-internal variable " + finalTestRef.getName() + " within analyze block");
				
				String instr1;
				String instr2;
				if (PTSpecUtils.isReferenceParamter(finalTestRef) && op != PTS_EOPERATOR.ASSIGN)
				{
					throw new PTSCompilerException("Combined read-and-assign operators are not supported for external element '" + finalTestRef.getName() + "'");
				}
				else if(PTSpecUtils.isReferenceParamter(finalTestRef))
				{
					instr1 = "";
					instr2 = PTS2JavaUtil.getJavaTraceProviderName(symRef) + ".injectValue(" + finalTestRef.getName() + "," + assignmentCode + ")";
				}
				else
				{
				    instr1 = finalTestRef.getName() + PTS2JavaUtil.getJavaOperator(op) + assignmentCode;
				    PTSDeclarator declarator = EcoreUtils.getContainerInstanceOf(finalTestRef, PTSDeclarator.class);
				    if (PTSpecUtils.isCapture(declarator))
				    	instr2 = ";" + PTS2JavaUtil.getJavaTraceProviderName(symRef) + ".injectValue(" + PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," + finalTestRef.getName() + ")";
				    else
				    	instr2 = "";
				}
				
				return instr1 + instr2;
			}
			else if (finalTestRef instanceof PTSTestVariableDeclaration ||
				     finalTestRef instanceof PTSPackageFuncParameterDeclaration)
			{
				// write access to local PTS internal element
				if (symRef.isArrayAccess())
					return getArrayWriteCode(finalTestRef.getName(), symRef, op, assignmentCode);
				else
					return finalTestRef.getName() + PTS2JavaUtil.getJavaOperator(op) + assignmentCode;
			}
			else if (finalTestRef instanceof PTSPackageVariableDeclaration)
			{
				// write access to global PTS internal element
				if (symRef.isArrayAccess())
					return getArrayWriteCode(PTS2JavaUtil.getJavaAccessProviderName((PTSPackageVariableDeclaration)finalTestRef, symRef), symRef, op, assignmentCode);
				else
					return PTS2JavaUtil.getJavaAccessProviderName((PTSPackageVariableDeclaration)finalTestRef, symRef) + PTS2JavaUtil.getJavaOperator(op) + assignmentCode;
			}
			else
			{
				// write access to external element
				
				if (op.getValue() != PTS_EOPERATOR.ASSIGN_VALUE)
					throw new PTSCompilerException("Combined read-and-assign operators are not supported for external element '" + finalTestRef.getName() + "'");
				
				if (PTSpecUtils.isRealtimeContext(symRef))
				{
					// Write is embedded in a trace block; special API method must be called
					return PTS2JavaUtil.TRACE_INTANCE_NAME + ".addStimulus(" + 
							PTS2JavaUtil.getJavaInspectorUID(symRef) + "," +
							PTS2JavaUtil.getJavaExecutorUID(symRef) + 
							",ITrace.SetupEvent.WRITE," + 
							PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," +
							PTS2JavaUtil.EPROPERTY_NONE + "," +
							assignmentCode +
							")";
				}
				else
				{
					return PTS2JavaUtil.RUNTIME_INSTANCE_NAME + ".setValue(" + PTS2JavaUtil.getJavaInspectorUID(symRef) + "," + PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," + assignmentCode + ")";
				}
			}
		}
	}
	
	private String getCodeToWrite(PTSJavaReference jRef, PTS_EOPERATOR op, String assignmentCode)
	{
		JvmIdentifiableElement finalJElement = PTSpecUtils.getFinalJvmIdentifiable(jRef);
		
		if (finalJElement instanceof JvmField)
			return finalJElement.getQualifiedName() + PTS2JavaUtil.getJavaOperator(op) + assignmentCode;
		else
			throw new PTSCompilerException("Writing to non-field Java element '" + finalJElement.getIdentifier() + "' detected");
	}


	// Calling
	// ------------------------------------------------------
	
	private String getCodeToCall(PTSValueSymbol valueSymbol, PTSCallParameterList callParamList)
	{
		if (valueSymbol instanceof PTSSymbolReference)
		{
			return getCodeToCall((PTSSymbolReference)valueSymbol, callParamList);
		}
		else if (valueSymbol instanceof PTSJavaReference)
		{
			return getCodeToCall((PTSJavaReference)valueSymbol, callParamList);
		}
		else
		{
			throw new PTSCompilerException("Invalid call to PTSValueSymbol type " + valueSymbol.getClass().getSimpleName() + " detected");
		}
	}
	
	private String getCodeToCall(PTSSymbolReference symRef, PTSCallParameterList callParamList)
	{
		if (symRef.isPropertyAccess() ||
			symRef.isEventAccess())
		{
			throw new PTSCompilerException("Invalid call to Property/Event detected");
		}
		
		ITestReferenceable finalTestRef = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (!(finalTestRef instanceof ITestCallable))
		{
			throw new PTSCompilerException("Invalid call to ITestReferenceable type " + finalTestRef.getClass().getSimpleName() + " detected");
		}
		
		String argumentsCode = getCodeFor(callParamList);

		String callCode;
		if (finalTestRef instanceof PTSPackageFunctionDeclaration)
		{
			// PTSpec internal call
			String accessorName = PTS2JavaUtil.getJavaAccessProviderName((PTSPackageFunctionDeclaration)finalTestRef, symRef);
			PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(finalTestRef, PTSPackageFunction.class);
			if (pkgFunc.isAnalyzeFunc() || pkgFunc.isRealtimeFunc())
			{
				String traceName = PTS2JavaUtil.getJavaTraceProviderName(symRef);
				callCode = accessorName + "(" +
						traceName + (argumentsCode.length() > 0 ? "," + argumentsCode : "") +
						")";
			}
			else
			{
				callCode = accessorName + "(" + argumentsCode + ")";
			}
		}
		else if (finalTestRef instanceof IWrappedReferenceable)
		{
			EObject innerObject = ((IWrappedReferenceable) finalTestRef).getInnerObject();
			if (!(innerObject instanceof JvmIdentifiableElement))
				throw new PTSCompilerException("Unexpected type in IWrappedReferenceable: " + innerObject.getClass().getSimpleName());

			callCode = PTSpecUtils.getFQNForJavaTypeReference(symRef) + "(" + getCodeFor(callParamList) + ")";
		}
		else
		{
			// Call to external function
			
			if (PTSpecUtils.isRealtimeContext(symRef))
			{
				// Call is embedded in the implementation of a trace block; special API method must be called
				callCode = PTS2JavaUtil.TRACE_INTANCE_NAME + ".addStimulus(" + 
						PTS2JavaUtil.getJavaInspectorUID(symRef) + "," + 
						PTS2JavaUtil.getJavaExecutorUID(symRef) + "," + 
						"ITrace.SetupEvent.CALL," +
						PTS2JavaUtil.getJavaElementUID(finalTestRef) + "," +
						PTS2JavaUtil.EPROPERTY_NONE + "," +
						"null" +
						(argumentsCode.length() > 0 ? "," + argumentsCode : "") +
						")";
			}
			else
			{
				callCode = PTS2JavaUtil.RUNTIME_INSTANCE_NAME + ".execute(" +
						PTS2JavaUtil.getJavaInspectorUID(symRef) + "," + 
						PTS2JavaUtil.getJavaExecutorUID(symRef) + "," + 
						PTS2JavaUtil.getJavaElementUID(finalTestRef) + 
						(argumentsCode.length() > 0 ? "," + argumentsCode : "") +
						")"; 
				
				// if this call is embedded in an operation or another call or part of an operation:
				// add typecast expression
				PTSExpression expr = EcoreUtils.getContainerInstanceOf(symRef, PTSExpression.class);
				
				if (EcoreUtils.hasContainerInstanceOf(symRef, PTSOperationExpression.class) ||
					EcoreUtils.hasContainerInstanceOf(symRef, PTSCallParameterList.class) ||
					expr.getOperationExpression() != null)
				{
					callCode = "((" + PTS2JavaUtil.getJavaDataType(finalTestRef) + ")" + callCode + ")";
				}
			}
		}
		
		return callCode;
	}

	private String getCodeToCall(PTSJavaReference jRef, PTSCallParameterList callParamList)
	{
		JvmIdentifiableElement finalJElement = PTSpecUtils.getFinalJvmIdentifiable(jRef);

		if (finalJElement instanceof JvmOperation)
			return finalJElement.getQualifiedName() + "(" + getCodeFor(callParamList) + ")";
		else
			throw new PTSCompilerException("Call to non-operational Java element '" + finalJElement.getIdentifier() + "' detected");
	}

	
	// Array Access
	// ------------------------------------------------------
	
	private String getArrayReadCode(String arrayName, PTSSymbolReference symRef)
	{
		if (symRef.isArrayAccess())
		{
			return arrayName + "[" + getCodeFor(symRef.getArrayAccessExpression()) + "]";
		}
		else
		{
			throw new PTSCompilerException("Request for read-array-code on scalar detected");
		}
	}
	
	private String getArrayWriteCode(String arrayName, PTSSymbolReference symRef, PTS_EOPERATOR op, String assignmentCode)
	{
		if (symRef.isArrayAccess())
		{
			return arrayName + "[" + getCodeFor(symRef.getArrayAccessExpression()) + "]" + PTS2JavaUtil.getJavaOperator(op) + assignmentCode;
		}
		else
		{
			throw new PTSCompilerException("Request for read-array-code on scalar detected");
		}
	}
}
