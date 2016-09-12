package fzi.mottem.ptspec.compiler;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class PTSCompilerSettings
{
	
	private IPath _outputFolder = new Path("./src-gen");
	public IPath getOutputFolder()
	{
		return _outputFolder;
	}
	public void setOutputFolder(IPath outputDirectory)
	{
		this._outputFolder = outputDirectory;
	}

	private String _outputPackage = "generated";
	public String getOutputPackage()
	{
		return _outputPackage;
	}
	public void setOutputPackage(String outputPackage)
	{
		this._outputPackage = outputPackage;
	}

	private String _testSubPackage = "tests";
	public String getTestSubPackage()
	{
		return _testSubPackage;
	}
	public void setTestSubPackage(String testSubPackage)
	{
		this._testSubPackage = testSubPackage;
	}

	private String _packagesSubPackage = "packages";
	public String getPackagesSubPackage()
	{
		return _packagesSubPackage;
	}
	public void setPackagesSubPackage(String packagesSubPackage)
	{
		this._packagesSubPackage = packagesSubPackage;
	}

	private boolean _compileOnSave = true;
	public boolean getCompileOnSave()
	{
		return _compileOnSave;
	}
	public void setCompileOnSave(boolean compileOnSave)
	{
		this._compileOnSave = compileOnSave;
	}

}
