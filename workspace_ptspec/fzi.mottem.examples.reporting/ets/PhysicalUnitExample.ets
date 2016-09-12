/*
 * PhysicalUnitExample.pts
 * Created on 13.06.2016 20:13:40
 */

include "Units.ets";

package UnitTest
{
	func void dummy()
	{
		
		physical[U⋅min⁻¹] umin1 = 1 U⋅h⁻¹;
		physical[U⋅min⁻¹] umin2 = umin1 + umin1; 
		
		physical[Hz] frequency;
		physical[s⁻¹] frequency2;
		frequency = 1 / 1 s; 
		
	}
}