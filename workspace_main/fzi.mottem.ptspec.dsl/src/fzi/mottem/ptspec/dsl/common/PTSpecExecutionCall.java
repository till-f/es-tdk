package fzi.mottem.ptspec.dsl.common;

import java.util.LinkedList;
import java.util.List;

import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpression;
import fzi.mottem.ptspec.dsl.pTSpec.PTSExpressionStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStatement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSSymbolReference;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageFunctionDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSValueSymbol;

public class PTSpecExecutionCall
{
	
	private final ITestReferenceable _testReferenceable;
	
	private final List<PTSExpression> _parameterExpressions;
	
	public PTSpecExecutionCall(PTSStatement statement)
	{
		if (!(statement instanceof PTSExpressionStatement))
			throw new RuntimeException("Bad PTSStatement type: " + statement.getClass().getSimpleName());
		
		PTSExpression expr = ((PTSExpressionStatement)statement).getExpression();
		
		if (expr.isBracketed())
			throw new RuntimeException("Execution call must not be bracketed");
		
		PTSValueSymbol valSym = expr.getValueSymbol();
		
		if (!(valSym instanceof PTSSymbolReference))
			throw new RuntimeException("Bad PTSValueSymbol type: " + valSym.getClass().getSimpleName());
		
		PTSSymbolReference symRef = (PTSSymbolReference)valSym;
		
		_testReferenceable = PTSpecUtils.getFinalTestReferenceable(symRef);
		
		if (!(_testReferenceable instanceof ITestCallable) ||
			_testReferenceable instanceof PTSPackageFunctionDeclaration)
			throw new RuntimeException("Bad ITestReferenceable type: " + _testReferenceable.getClass().getSimpleName());
		
		if (expr.getCallParameterList() == null)
			throw new RuntimeException("No PTSCallParameterList on TestReferenceable " + _testReferenceable.getName());
		
		_parameterExpressions = new LinkedList<PTSExpression>();
		
		for (PTSExpression paramExpr : expr.getCallParameterList().getExpressions())
			_parameterExpressions.add(paramExpr);
	}
	
	public ITestReferenceable getTestReferenceable()
	{
		return _testReferenceable;
	}

	public List<PTSExpression> getParameterExpressions()
	{
		return _parameterExpressions;
	}

}
