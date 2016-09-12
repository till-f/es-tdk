/*
 * Units.pts
 * Created on 28.10.2015 17:43:20
 */

package Units
{
	// for unit-less physicals
	unit One;
	
	// pressure
	unit bar;
	
	// example for SI unit and conversion support
	unit K;
	unit C [K = C + 273.15];
	unit F [C = (F - 32) / 1.8];

	// Base unit for time is "milliseconds" (instead of second)
	// due to legacy support for PTSpec Runtime....
	// should be fixed some day...
	unit ms;
	unit s [ms = s * 1000];
	unit us [ms = us / 1000];
	unit ns [us = ns / 1000];

	unit U⋅min⁻¹;
	unit U⋅h⁻¹ [U⋅min⁻¹ = U⋅h⁻¹ * 60];
	unit U⋅s⁻¹ [U⋅min⁻¹ = U⋅s⁻¹ / 60];
}
