/*
 * DemoTraceCAN.pts
 * Created on 04.11.2014 15:15:29
 */

include "pkg/Targets.ets";
include "pkg/Units.ets";
include "pkg/Common.ets";

test DemoTraceCPUStimCAN : Demonstrator default CANPort
{
    Common.bootToMain();
	
	// This starts a background task
	// sending CAN messages via 
	// Vector VN7600 Logger
	concurrent
	{
        wait 1s;
        report info: "Started stimulation task.";
        Drehzahl = 500 U⋅min⁻¹;
        report info: "Drehzahl gesetzt.";
        wait 1.5s;
		Drehzahl = -3000 U⋅min⁻¹;
        report info: "Drehzahl gesetzt.";
		wait 1.5s;
		Drehzahl = 1500 U⋅min⁻¹;
        report info: "Drehzahl gesetzt.";
		wait 1.5s;
		Drehzahl = 3000 U⋅min⁻¹;
        report info: "Drehzahl gesetzt.";
	}
    
	// This configures and starts a realtime session.
	// Both cores resume execution at the function
	// 'run_app'. Data capture is stopped after 8
	// seconds.
	report info : "Starting trace...";
	realtime timeout 8s
	{
        Core0.run_app();
        Core1.run_app();
	}
    
	// When data capture has finished the following
	// analyze block is executed. Timing information is
	// extracted out of the captured data.
	analyze
	{
		double t0 = 0;
		while (!Runtime:EOT)
		{
			wait until Core0.etimer00ISR´called;
			capture double isrTimeBetween = Runtime:Timestamp - t0;
			t0 = Runtime:Timestamp;
			
			wait until Core0.etimer00ISR´returned;
			capture double isrExecTime = Runtime:Timestamp - t0;
		}
	}
	
	// This creates a plot showing the RPM value as
	// measured by the µController and the internal
	// target value
	plot RPM_Analysis range [0s, 10s]
	{
		Drehzahl_CPU  : Core0.rpm                 range [-3500, 3500] ;
        Drehzahl_CAN  : CANPort.Drehzahl_Messung  range [-3500, 3500] ;
		Drehzahl_soll : Core0.rpm_tar             range [-3500, 3500] hold;
	}
	
	// This creates a plot based on data calculated
	// in the previous analyze section which again is
	// based on data recorded in the trace session
	// (the ISR execution time and time between interrupts)
	plot Timing_Analysis range [0s, 10s]
	{
        Exec_Time               : isrExecTime     range [3us, 1us];
        Time_Between_Interrupts : isrTimeBetween  range [0ms, 10ms];
	}
	
	report info : "Test execution completed.";
}