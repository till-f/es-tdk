/*
 * DemoRunControl.pts
 * Created on 04.06.2014 16:39:15
 */

include "pkg/Targets.ets";
include "pkg/Units.ets";
include "pkg/Common.ets";

test DemoRunControl : Demonstrator_PPC
{
    Common.bootToMain();
	
	concurrent { run Core0; }
	concurrent { run Core1; }
	
    report info : "Execution resumed, waiting...";
    
	wait 1s;
	
	scope Core1
	{
		while(true)
		{
			report info : "-2000 RPM...";
			rpm_tar_CAN = -2000;
			wait 2s;
			
			report info : "-1000 RPM...";
			rpm_tar_CAN = -1000;
			wait 2s;
			
			report info : "1000 RPM...";
			rpm_tar_CAN = 1000;
			wait 2s;
			
			report info : "2000 RPM...";
			rpm_tar_CAN = 2000;
			wait 2s;
			
			report info : "3000 RPM...";
			rpm_tar_CAN = 3000;
			wait 2s;
		}
	}
}
