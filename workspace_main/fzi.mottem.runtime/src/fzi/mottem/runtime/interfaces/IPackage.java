package fzi.mottem.runtime.interfaces;


public interface IPackage
{
	public String getName();
	
	public double convertToBaseUnit(double value, String unit);

	public String getSourceFileUri();

	public String getProject();
	
	public int getSourceOffset();
	
	public int getSourceLength();
}
