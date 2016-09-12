package fzi.mottem.ptspec.compiler.workers;

import java.util.List;

import fzi.mottem.jjet.interfaces.IJJETContext;
import fzi.mottem.jjet.interfaces.IJJETTemplate;
import fzi.mottem.ptspec.compiler.precompiled.JET_PackageElement;
import fzi.mottem.ptspec.compiler.precompiled.JET_Statement;
import fzi.mottem.ptspec.compiler.precompiled.JET_StatementAsync;
import fzi.mottem.ptspec.compiler.util.PTSConcurrentStatementWrapper;
import fzi.mottem.ptspec.dsl.pTSpec.PTSImplementation;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageElement;
import fzi.mottem.ptspec.dsl.pTSpec.PTSStatement;

public class PTSWorker
{

	private final IJJETContext _context;
	
	public PTSWorker(IJJETContext context)
	{
		_context = context;
	}
	
	public void compile_PTSPackageFunctions(List<PTSPackageElement> pkgElements)
	{
		_context.pushIndent(1);
		
		for (PTSPackageElement pkgElement : pkgElements)
		{
			compile(pkgElement);
		}

		_context.popIndent();
	}

	public void compile(PTSPackageElement pkgElement)
	{
		IJJETTemplate pkgFuncTemplate = new JET_PackageElement();
		_context.getCompiler().compile(pkgFuncTemplate, pkgElement, _context);
	}
	
	public void compile(PTSConcurrentStatementWrapper wrapper)
	{
		IJJETTemplate asyncStatementTemplate = new JET_StatementAsync();
		_context.getCompiler().compile(asyncStatementTemplate, wrapper, _context);
	}

	public void compile(PTSImplementation impl)
	{
		for (PTSStatement statement : impl.getStatements())
		{
			_context.pushIndent(1);
			
			IJJETTemplate statementTemplate = new JET_Statement();
			_context.getCompiler().compile(statementTemplate, statement, _context);

			_context.popIndent();
		}
	}
}
