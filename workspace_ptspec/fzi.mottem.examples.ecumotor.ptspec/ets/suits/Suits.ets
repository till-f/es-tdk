/*
 * Suits.ets
 * Created on 17.07.2016 12:37:09
 */

include "../DemoTraceCANStimCAN.ets";
include "../DemoTraceCPUStimCAN.ets";
include "../DemoTraceCPUStimCPU.ets";

suite AllTracingDemos { DemoTraceCANStimCAN, DemoTraceCPUStimCAN, DemoTraceCPUStimCPU }
