/*
 * DemoMixedTest.ets
 * Created on 17.07.2016 11:41:50
 */

include "pkg/Targets.ets";
include "pkg/Units.ets";
include "pkg/Common.ets";

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
test MixedTest_PWM : PWMIntegration
{
    Common.bootToMain();
    
    Core0.pwm_dc = 0.0;

    realtime timeout 5s
    {
    	PWM0.Value:TriggerAbove = 0.5;

    	Core0.updatePWM();
    }
    plot Signalverlauf
    {
    	PWM0: PWM0.Value;
    }
}