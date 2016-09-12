package fzi.mottem.ptspec.compiler.util;

import org.eclipse.emf.ecore.EObject;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.baseelements.ISignal;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Symbol;
import fzi.mottem.ptspec.compiler.PTSCompilerException;
import fzi.mottem.ptspec.compiler.PTSCompilerPlugin;
import fzi.mottem.ptspec.compiler.PTSCompilerSettings;
import fzi.mottem.ptspec.compiler.workers.PTSExpressionWorker;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDataType;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarationStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarationStatementForLoop;
import fzi.mottem.ptspec.dsl.pTSpec.PTSDeclarator;
import fzi.mottem.ptspec.dsl.pTSpec.PTSJavaReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSNumberConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameter;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFuncParameterDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunction;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunctionDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageUnit;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariable;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSpecialConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStringConstant;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestSuiteDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestVariableDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSUnitDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSUnitExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EEVENT;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EINTEGRALDATATYPE;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ELOOPCTRLSTATEMENT;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EOPERATOR;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EPREFIXOPERATOR;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ERUNTIMEPROPERTY;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESPECIALCONSTANT;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_EUNITOPERATOR;
import fzi.mottem.ptspec.dsl.pTSpec.PTSpecPackage;
import fzi.util.ecore.EcoreUtils;

public class PTS2JavaUtil
{

	public static final String RUNTIME_INSTANCE_NAME = "_pts_runtime";
	public static final String TRACE_INTANCE_NAME = "_pts_trace";
	public static final String UIDRESOLVER_NAME = "_pts_uid_resolver";
	public static final String TEST_INSTANCE_NAME = "_pts_test";
	
	public static final String EPROPERTY_NONE = "EItemProperty.NONE";
	
	public static final String PACKAGE_UNIT_CONVERT_FUNC = "convertToBaseUnit";

	private static final String PACKAGE_NAME_PREFIX = "_pkg_";
	

	/*
	 * Resolves the name by which the environment can be accessed, which is
	 * dependend on the context of provided PTSpec element.
	 */
	public static String getJavaEnvironmentProviderName(EObject ptsElement)
	{
		if (PTSpecUtils.isTraceAnalyzeContext(ptsElement))
		{
			return getJavaTraceProviderName(ptsElement);
		}
		
		return RUNTIME_INSTANCE_NAME;
	}

	public static String getJavaTestProviderName(EObject ptsElement)
	{
		if (EcoreUtils.hasContainerInstanceOf(ptsElement, PTSTestDeclaration.class))
			return "this";
		else
			return TEST_INSTANCE_NAME;
	}
	
	/*
	 * Resolves the name by which the trace can be accessed, which is
	 * dependend on the context of provided PTSpec element.
	 */
	public static String getJavaTraceProviderName(EObject ptsElement)
	{
		if (PTSpecUtils.isTraceAnalyzeContext(ptsElement) || PTSpecUtils.isRealtimeContext(ptsElement))
		{
			return TRACE_INTANCE_NAME;
		}
		else
		{
			// return name for NULL if trace is not available in the provided context
			return "null";
		}
	}

	public static String getJavaAccessProviderName(PTSPackageDeclaration pkgDecl, PTSPackageDeclaration pkgContext)
	{
		if (pkgDecl == pkgContext)
			return "this";
		else
			return PACKAGE_NAME_PREFIX + pkgDecl.getName();
	}

	public static String getJavaAccessProviderName(PTSPackageFunctionDeclaration pkgFunc, PTSSymbolReference context)
	{
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(pkgFunc, PTSPackageDeclaration.class);
		PTSPackageDeclaration pkgContext = EcoreUtils.getContainerInstanceOf(context, PTSPackageDeclaration.class);
		return getJavaAccessProviderName(pkgDecl, pkgContext) + "." + pkgFunc.getName();
	}

	public static String getJavaAccessProviderName(PTSPackageVariableDeclaration pkgVar, PTSSymbolReference context)
	{
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(pkgVar, PTSPackageDeclaration.class);
		PTSPackageDeclaration pkgContext = EcoreUtils.getContainerInstanceOf(context, PTSPackageDeclaration.class);
		return getJavaAccessProviderName(pkgDecl, pkgContext) + "." + pkgVar.getName();
	}
	
	public static String getJavaAccessProviderName(PTSUnitDeclaration pkgUnit, PTSNumberConstant context)
	{
		PTSPackageDeclaration pkgDecl = EcoreUtils.getContainerInstanceOf(pkgUnit, PTSPackageDeclaration.class);
		PTSPackageDeclaration pkgContext = EcoreUtils.getContainerInstanceOf(context, PTSPackageDeclaration.class);
		return getJavaAccessProviderName(pkgDecl, pkgContext) + "." + PACKAGE_UNIT_CONVERT_FUNC;
	}

	public static String getJavaExecutorUID(EObject ptsElement)
	{
		IExecutor exec;
		
		if (ptsElement instanceof IExecutor)
			exec = (IExecutor) ptsElement;
		else
			exec = PTSpecUtils.getAssociatedExecutor(ptsElement);
		
		if(exec != null)
			return getJavaElementUID(exec);
		else
			return "null";
	}
	
	public static String getJavaInspectorUID(EObject ptsElement)
	{
		IInspector inspector;
		
		if (ptsElement instanceof IInspector)
			inspector = (IInspector) ptsElement;
		else
			inspector = PTSpecUtils.getAssociatedInspector(ptsElement);
		
		// if null is returned, the context of eObj does not provide an Inspector.
		// this may be within a package, where the id is provided by a variable,
		// otherwise the executor UID is not available => return NULL.
		
		if(inspector != null)
			return getJavaElementUID(inspector);
		else
			return "null";
	}
	
	public static String getJavaElementUID(INamed named)
	{
		return " /* " + named.getName() + " */ \"" + PTSpecUtils.getElementUID(named) + "\"";
	}

	public static String getJavaPropertyUID(INamed named, PTS_EPROPERTY property)
	{
		return " /* " + named.getName() + ":" + property.getLiteral() + " */ \"" + PTSpecUtils.getPropertyUID(named, property) + "\"";
	}
	
	public static String getJavaClassName(PTSPackageDeclaration pkgDecl)
	{
		return pkgDecl.getName() + "Package";
	}
	
	public static String getJavaClassName(PTSTestDeclaration testDecl)
	{
		return testDecl.getName() + "Test";
	}
	
	public static String getJavaClassName(PTSTestSuiteDeclaration testDecl)
	{
		return testDecl.getName() + "Suite";
	}
	
	public static String getJavaFullQualifiedClassName(PTSTestDeclaration testDecl)
	{
		return getJavaTestPackageName() + "." + getJavaClassName(testDecl);
	}
	
	public static String getJavaFullQualifiedClassName(PTSTestSuiteDeclaration testSuiteDecl)
	{
		return getJavaTestPackageName() + "." + getJavaClassName(testSuiteDecl);
	}
	
	public static String getJavaTestPackageName()
	{
		PTSCompilerSettings settings = PTSCompilerPlugin.Instance.getSettings();
		return settings.getOutputPackage() + "." + settings.getTestSubPackage();
	}

	public static String getJavaPtsPackagePackageName()
	{
		PTSCompilerSettings settings = PTSCompilerPlugin.Instance.getSettings();
		return settings.getOutputPackage() + "." + settings.getPackagesSubPackage();
	}

	public static String getJavaDataType(ITestReferenceable testRef)
	{
		if (testRef instanceof PTSTestVariableDeclaration)
		{
			PTSDeclarator declarator = EcoreUtils.getContainerInstanceOf(testRef, PTSDeclarator.class);
			PTSDataType type = declarator.getDataType();
			return getJavaDataType(type, false, false);
		}
		else if (testRef instanceof PTSPackageFunctionDeclaration)
		{
			PTSPackageFunction pkgFunc = EcoreUtils.getContainerInstanceOf(testRef, PTSPackageFunction.class);
			PTSDataType type = pkgFunc.getReturnDataType();
			return getJavaDataType(type, false, false);
		}
		else if (testRef instanceof PTSPackageVariableDeclaration) 
		{
			PTSPackageVariable pkgVar = EcoreUtils.getContainerInstanceOf(testRef, PTSPackageVariable.class);
			PTSDataType type = pkgVar.getDataType();
			return getJavaDataType(type, false, false);
		}
		else if (testRef instanceof Symbol)
		{
			DataType type = ((Symbol) testRef).getDataType();
			
			if (type == null)
			{
				throw new PTSCompilerException("Symbol without datatype: " + testRef.getName());
			}
			else if(type instanceof DTInteger)
			{
				if (((DTInteger) type).getBitSize() <= 8)
					return "byte";
				else if (((DTInteger) type).getBitSize() <= 16)
					return "short";
				else if (((DTInteger) type).getBitSize() <= 32)
					return "int";
				else if (((DTInteger) type).getBitSize() <= 64)
					return "long";
				else
					throw new PTSCompilerException("Symbol with integer data type of unsupported bit size");
			}
			else if(type instanceof DTFloatingPoint)
			{
				DTFloatingPoint fpType = (DTFloatingPoint) type;
				if (fpType.getExponentBitSize() == 8 &&
					fpType.getSignificandBitSize() == 23)
					return "float";
				else if (fpType.getExponentBitSize() == 11 &&
					fpType.getSignificandBitSize() == 52)
					return "double";
				else
					throw new PTSCompilerException("Symbol with floating point data type of unsupported format");
			}
			else
			{
				// !TODO: return appropriate data type
				throw new PTSCompilerException("Unknown Symbol data type " + type.getClass().getSimpleName() + " when fetching Java data type");
			}
		}
		else if (testRef instanceof ISignal)
		{
			// !TODO: return appropriate data type
			return "double";
		}
		else if (testRef instanceof PTSPackageFuncParameterDeclaration)
		{
			PTSPackageFuncParameter fParam = EcoreUtils.getContainerInstanceOf(testRef, PTSPackageFuncParameter.class);
			return getJavaDataType(fParam.getDataType(), false, false);
		}
		else
		{
			// !TODO (possible improvement): find out why this throws sometimes for "PTSContainerDeclarationImpl"... 
			throw new PTSCompilerException("Unknown TestRef type " + testRef.getClass().getSimpleName() + " when fetching Java data type");
		}
	}
	
	public static String getJavaDataType(PTSDataType dataType, boolean isReferenceAccess, boolean suppressArray)
	{
		if (isReferenceAccess)
		{
			return "String";
		}
		else if (suppressArray)
		{
			if (dataType.isPhysicalType())
				return "double";
			else
				return getIntegralJavaDataType(dataType.getIntegralType());
		}
		else if (dataType.isArray())
		{
			if (dataType.isPhysicalType())
				return "double[]";
			else
				return getIntegralJavaDataType(dataType.getIntegralType()) + "[]";
		}
		else
		{
			if (dataType.isPhysicalType())
				return "double";
			else
				return getIntegralJavaDataType(dataType.getIntegralType());
		}
	}
	
	private static String getIntegralJavaDataType(PTS_EINTEGRALDATATYPE ptsType)
	{
		switch (ptsType)
		{
			case VOID:
				return "void";
			case BOOL:
				return "boolean";
			case INT8:
				return "byte";
			case INT16:
				return "short";
			case INT32:
				return "int";
			case INT64:
				return "long";
			case FLOAT:
				return "float";
			case DOUBLE:
				return "double";
			case STRING:
				return "String";
			case EVENT:
				return "String";
		}

		throw new PTSCompilerException("Unknown data type: " + ptsType.getName());
	}
	
	public static String getJavaLoopControlKeyword(PTS_ELOOPCTRLSTATEMENT ptsLoopControlStatement)
	{
		switch (ptsLoopControlStatement)
		{
		case BREAK:
			return "break";
		case CONTINUE:
			return "continue";
		}
		
		throw new PTSCompilerException("Unknown loop control statement: " + ptsLoopControlStatement.getName());
	}

	public static String getJavaSpecialConstant(PTS_ESPECIALCONSTANT ptsSpecialConstant)
	{
		switch (ptsSpecialConstant)
		{
			case FALSE:
				return "false";
			case NULL:
				return "null";
			case TRUE:
				return "true";
		}
		
		throw new PTSCompilerException("Unknown special constant: " + ptsSpecialConstant.getName());
	}

	public static String getJavaOperator(PTS_EOPERATOR op)
	{
		switch (op)
		{
			case ADD:
				return "+";
			case AND:
				return "&";
			case ASSIGN:
				return "=";
			case ASSIGN_ADD:
				return "+=";
			case ASSIGN_DIVIDE:
				return "/=";
			case ASSIGN_MULTIPLY:
				return "*=";
			case ASSIGN_SUBSTRACT:
				return "-=";
			case BOOL_AND:
				return "&&";
			case BOOL_OR:
				return "||";
			case DIVIDE:
				return "/";
			case MODULO:
				return "%";
			case EQUAL:
				return "==";
			case GREATER:
				return ">";
			case GREATER_EQUAL:
				return ">=";
			case LOWER_EQUAL:
				return "<=";
			case LOWER:
				return "<";
			case MULTIPLY:
				return "*";
			case OR:
				return "|";
			case SUBSTRACT:
				return "-";
			case UN_EQUAL:
				return "!=";
			case XOR:
				return "^";
			case INSTANCE_OF:
				return " instanceof ";
			case SHIFT_LEFT:
				return "<<";
			case SHIFT_RIGHT:
				return ">>";
			case SHIFT_RIGHT_ZERO:
				return ">>>";
		}
		
		throw new PTSCompilerException("Unknown PTS_EOPERATOR: " + op.getName());
	}

	public static String getJavaUnitOperator(PTS_EUNITOPERATOR op)
	{
		switch (op)
		{
			case ADD:
				return "+";
			case DIVIDE:
				return "/";
			case MULTIPLY:
				return "*";
			case SUBSTRACT:
				return "-";
		}
		
		throw new PTSCompilerException("Unknown PTS_EUNITOPERATOR: " + op.getName());
	}
	
	public static String getJavaOperator(PTS_EPREFIXOPERATOR op)
	{
		switch (op)
		{
			case INVERT:
				return "!";
		}
		
		throw new PTSCompilerException("Unknown prefix operator: " + op.getName());
	}

	public static String getJavaEvent(PTS_EEVENT event)
	{
		switch(event)
		{
			case READ:
				return "EEvent.Read";
			case WRITTEN:
				return "EEvent.Written";
			case CALLED:
				return "EEvent.Called";
			case RETURNED:
				return "EEvent.Returned";
			case RECEIVED:
				return "EEvent.Received";
			case SENT:
				return "EEvent.Sent";
		}

		throw new PTSCompilerException("Unknown PTS_EEVENT: " + event.getName());
	}

	public static String getJavaProperty(PTS_EPROPERTY property)
	{
		switch (property)
		{
			case INSTRUCTION_POINTER:
				return "EItemProperty.InstructionPointer";
			case ADDRESS:
				return "EItemProperty.Address";
			case SAMPLE_RATE:
				return "EItemProperty.SampleRate";
			case TRIGGER_ABOVE:
				return "EItemProperty.TriggerAbove";
			case TRIGGER_BELOW:
				return "EItemProperty.TriggerBelow";
			case TRIGGER_FALLING:
				return "EItemProperty.TriggerFalling";
			case TRIGGER_RISING:
				return "EItemProperty.TriggerRising";
			case COUNT:
				throw new PTSCompilerException("Invalid request for java property name of " + property.getClass().getSimpleName() + " " + property.getLiteral());
		}

		throw new PTSCompilerException("Unknown property: " + property);
	}

	public static String getJavaPropertyDataType(PTS_EPROPERTY property)
	{
		switch (property)
		{
			case INSTRUCTION_POINTER:
				return "long";
			case ADDRESS:
				return "long";
			case SAMPLE_RATE:
				return "double";
			case TRIGGER_ABOVE:
				return "double";
			case TRIGGER_BELOW:
				return "double";
			case TRIGGER_FALLING:
				return "boolean";
			case TRIGGER_RISING:
				return "boolean";
			case COUNT:
				throw new PTSCompilerException("Invalid request for java property datatype of " + property.getClass().getSimpleName() + " " + property.getLiteral());
		}

		throw new PTSCompilerException("Unknown property: " + property);
	}

	public static String getJavaRuntimePropertyFuncName(PTS_ERUNTIMEPROPERTY property)
	{
		switch (property)
		{
			case GLOBAL_TIME:
				return "getGlobalTime";
			case TIMESTAMP:
				return "getTimestamp";
			case END_OF_TRACE:
				return "isEndOfTrace";
		}

		throw new PTSCompilerException("Unknown property: " + property);
	}

	public static String getJavaConstantValue(PTSConstant constant)
	{
		if (constant instanceof PTSStringConstant)
		{
			return "\"" + ((PTSStringConstant)constant).getValue() + "\"";
		}
		else if (constant instanceof PTSNumberConstant)
		{
			PTSNumberConstant numConst = (PTSNumberConstant)constant;
			if (numConst.eIsSet(PTSpecPackage.Literals.PTS_NUMBER_CONSTANT__UNIT))
				return getJavaAccessProviderName(numConst.getUnit(), numConst) + "(" + numConst.getValue() + ",\"" + numConst.getUnit().getName() + "\")";
			else
				return numConst.getValue();
		}
		else if (constant instanceof PTSSpecialConstant)
		{
			return getJavaSpecialConstant(((PTSSpecialConstant)constant).getValue());
		}
		else
		{
			throw new PTSCompilerException("Unknown PTSConstant type: " + constant.getClass().getSimpleName());
		}
	}

	public static String getJavaConvertString(PTSPackageUnit pkgUnit, String innerCode)
	{
		if (pkgUnit.isDerived())
		{
			innerCode = getJavaConvertString(pkgUnit.getDeclaration(), pkgUnit.getExpression(), innerCode);

			PTSPackageUnit pkgBaseUnit = EcoreUtils.getContainerInstanceOf(pkgUnit.getBaseUnit(), PTSPackageUnit.class);
			return getJavaConvertString(pkgBaseUnit, innerCode);
		}
		else
		{
			return innerCode;
		}
	}
	
	public static String getCodeForDeclaration(PTSDeclarator declarator)
	{
		String dataTypeStr;
		if (declarator.getDataType() != null)
		{
			dataTypeStr = PTS2JavaUtil.getJavaDataType(declarator.getDataType(), false, false);
		}
		else
		{
			dataTypeStr = PTSpecUtils.getFinalJvmIdentifiable((PTSJavaReference)declarator.getJavaType()).getQualifiedName();
		}
		
		return dataTypeStr + " " +
			declarator.getDeclaration().getName();
	}
	
	public static String getCode(PTSDeclarationStatement declStm, PTSExpressionWorker expressionWorker)
	{
		String assignmentCode = getCodeForAssignment(declStm.getDeclarator(), expressionWorker);
		
		if (declStm.isWithAssignment())
		{
			assignmentCode = "=" + expressionWorker.getCodeFor(declStm.getAssignment());
		}
		
		String traceInjectCode = null;
		if (assignmentCode != null && declStm.isCapture() && PTSpecUtils.isTraceAnalyzeContext(declStm))
			traceInjectCode = getCodeForInject(declStm.getDeclarator(), expressionWorker);

		return declStm.isFinal() ? "final " : "" + 
			getCodeForDeclaration(declStm.getDeclarator()) +
			(assignmentCode != null ? assignmentCode : "") +
			(traceInjectCode != null ? ";" + traceInjectCode : "");
	}
	
	public static String getCode(PTSDeclarationStatementForLoop declStm, PTSExpressionWorker expressionWorker)
	{
		String assignmentCode = getCodeForAssignment(declStm.getDeclarator(), expressionWorker);
		
		if (declStm.isWithAssignment())
		{
			assignmentCode = "=" + expressionWorker.getCodeFor(declStm.getAssignment());
		}

		String traceInjectCode = null;
		if (assignmentCode != null && declStm.isCapture() && PTSpecUtils.isTraceAnalyzeContext(declStm))
			traceInjectCode = getCodeForInject(declStm.getDeclarator(), expressionWorker);
		
		return getCodeForDeclaration(declStm.getDeclarator()) +
			(assignmentCode != null ? assignmentCode : "") +
			(traceInjectCode != null ? ";" + traceInjectCode : "");
	}
	
	

	private static String getCodeForAssignment(PTSDeclarator declarator, PTSExpressionWorker expressionWorker)
	{
		if (declarator.getDataType() == null)
			return null;

		if (declarator.getDataType().isArray())
		{
			return "=new " + PTS2JavaUtil.getJavaDataType(declarator.getDataType(), false, true) + "[" + expressionWorker.getCodeFor(declarator.getDataType().getArraySizeExpression()) + "]";
		}
		else if (declarator.getDataType().getIntegralType() == PTS_EINTEGRALDATATYPE.EVENT)
		{
			return "=\"" + PTSpecUtils.getElementUID(declarator.getDeclaration()) + "\"";
		}
		else
		{
			return null;
		}
	}

	private static String getCodeForInject(PTSDeclarator declarator, PTSExpressionWorker expressionWorker)
	{
		return PTS2JavaUtil.TRACE_INTANCE_NAME + ".injectValue(" + PTS2JavaUtil.getJavaElementUID(declarator.getDeclaration()) + "," + declarator.getDeclaration().getName() + ")";
	}
	
	private static String getJavaConvertString(PTSUnitDeclaration inputUnit, PTSUnitExpression expr, String innerCode)
	{
		String rhs = "";
		if (expr.getOperationExpression() != null)
		{
			rhs = getJavaUnitOperator(expr.getOperationExpression().getOp()) + getJavaConvertString(inputUnit, expr.getOperationExpression().getRhs(), innerCode);
		}
		
		if (expr.isBracketed())
		{
			return "(" + getJavaConvertString(inputUnit, expr.getInnerExpression(), innerCode) + ")" + rhs;
		}
		else if (expr.getUnit() != null)
		{
			if (expr.getUnit() != inputUnit)
				throw new RuntimeException("Formula defining unit '" + inputUnit + "' depends on another unit '" + expr.getUnit() + "'");

			return "(" + innerCode + ")" + rhs;
		}
		else if (expr.getConstant() != null)
		{
			return expr.getConstant() + rhs;
		}

		throw new PTSCompilerException("Could not resolve unit to SI type");
	}

}
