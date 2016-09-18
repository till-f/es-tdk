/*
 * DemoMixedTest.ets
 * Created on 17.07.2016 11:41:50
 */

include "pkg/Targets.ets";
include "pkg/Units.ets";
include "pkg/Common.ets";
include "pkg/SignalAnalyzer.ets";

/*
 * timing test for etimer00ISR()
 */
test MixedTest_TimingISR0 : Demonstrator_PPC default Core0
{
    Common.bootToMain();
    
    realtime
    {
        Core0.etimer00ISR();
    }
    analyze
    {
        wait until etimer00ISR´called;
        physical[s] t0 = Runtime:Timestamp;
        
        wait until etimer00ISR´returned;
        physical[s] tDelta = Runtime:Timestamp - t0;
        
        assert(tDelta < 1 ms) error;
    }
}


/*
 * HW / SW integration test for PWM module
 */
test MixedTest_PWM : PWMIntegration default PWM0
{
    Common.bootToMain();
    
    Core0.pwm_dc = 0.5;
   	Core0.updatePWM();

    realtime timeout 5s
    {
    	Value:TriggerAbove = 0.1 V;
    }
    analyze
    {
    	physical[s] t0 = SignalAnalyzer.findRisingEdge(ref Value, 0 s, 0.1 V);
    	physical[s] t1 = SignalAnalyzer.findRisingEdge(ref Value, t0, 0.1 V);
    	physical[s] t2 = SignalAnalyzer.findRisingEdge(ref Value, t1, 0.1 V);
    	
	    physical[s] period = t2-t0;
	    double duty = <double>((period-(t2-t1))/period);
	    assert ((duty > 0.49) && (duty < 0.51)) error;
	    assert ((period > 24us) && (period < 26us)) error;
    }
}