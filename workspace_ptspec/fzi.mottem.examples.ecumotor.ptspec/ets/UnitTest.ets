/*
 * DemoSimpleTest.pts
 * Created on 04.11.2015 09:26:17
 */

include "pkg/Targets.ets";
include "pkg/Units.ets";
include "pkg/Common.ets";

/*
 * unit test for update_sector()
 */
test UnitTest_UpdateSector : Demonstrator_PPC default Core0
{
	Common.bootToMain();
	
	update_sector(0, 0, 0);
	assert (sector == 0) error;

	update_sector(1, 0, 1);
	assert(sector == 1) error;

	update_sector(1, 0, 0);
	assert(sector == 2) error;

	update_sector(1, 1, 0);
	assert(sector == 3) error;

	update_sector(0, 1, 0);
	assert(sector == 4) error;

	update_sector(0, 1, 1);
	assert(sector == 5) error;

	update_sector(0, 0, 1);
	assert(sector == 6) error;
}