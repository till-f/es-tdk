package Units
{
	// time
	// base unit for time MUST be 'ms'
	unit ms;
	unit s [ms = s * 1000];
	unit us [ms = us / 1000];
	unit ns [us = ns / 1000];
	
	// length
	unit m;
	unit km [m = km * 1000];
	unit cm [m = cm / 100];
	unit mm [m = mm / 1000];
	
	// speed
	unit m⋅ms⁻¹;
	unit m⋅s⁻¹ [m⋅ms⁻¹ = m⋅s⁻¹ / 1000];
	unit km⋅h⁻¹ [m⋅s⁻¹ = km⋅h⁻¹ / 3.6];
	
	// temperature
	unit K;
	unit C [K = C + 273.15];
	unit F [C = (F - 32) / 1.8];
}
