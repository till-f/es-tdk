package fzi.mottem.runtime.rti.keysight;

import java.util.List;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.PinSignal;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.runtime.TraceDB.TraceDBEvent;
import fzi.mottem.runtime.interfaces.ITrace;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.rti.AbstractTraceDriver;

public class IVITraceDriver extends AbstractTraceDriver
{
	private final IVIWrapper _iviWrapper;
	
	@SuppressWarnings("unused")
	private class Parameters
	{
		double SampleRate = Double.NaN;
		double UpperThreshold = Double.NaN;
		double LowerThreshold = Double.NaN;
		boolean TriggerRising = false;
		boolean TriggerFalling = false;
	}
	
	private Parameters _parameters = null;
	
	private double[] _sampleData = new double[0];
	
	public IVITraceDriver(ITrace trace, AbstractAccessDriver inspector)
	{
		super(trace, inspector);
		
		if (inspector instanceof IVIAccessDriver)
		{
			_iviWrapper = ((IVIAccessDriver) inspector).getIVIWrapper();
		}
		else
		{
			throw new RuntimeException("Unexpected Insepctor type for IVITrace: " + (inspector == null ? "NULL" : inspector.getClass().getSimpleName()));
		}
	}

	@Override
	public void configure(List<ITestReferenceable> elementToCapture)
	{
		_parameters = new Parameters();
		
		for (InternalSetupEvent ise : _setupEvents)
		{
			switch (ise.getProperty())
			{
				case NONE:
				case Address:
				case InstructionPointer:
					throw new RuntimeException("Property not supported by IVITraceDriver");
				case SampleRate:
					_parameters.SampleRate = (Double) ise.getValue();
					break;
				case TriggerAbove:
					_parameters.UpperThreshold = (Double) ise.getValue();
					break;
				case TriggerBelow:
					_parameters.LowerThreshold = (Double) ise.getValue();
					break;
				case TriggerRising:
					_parameters.TriggerRising = (Boolean) ise.getValue();
					break;
				case TriggerFalling:
					_parameters.TriggerFalling = (Boolean) ise.getValue();
					break;
			}
		}
			
//			switch("this was the element name")
//			{
//				case "getScaledWaveform" : 
//					_iviWrapper.setResults(_iviWrapper.getScaledWaveform((double)ise.getArguments()[0]));
//					break;
//				case "scaledTrigger" : 
//					_iviWrapper.setResults(_iviWrapper.scaledTrigger((double)ise.getArguments()[0], (boolean)ise.getArguments()[1], (double)ise.getArguments()[2]));
//					break;
//				case "setReduceFactor" : 
//					_iviWrapper.setSampleRateReduceFactor((int)ise.getArguments()[0]);
//					break;
//				case "getWaveform" : 
//					_iviWrapper.setResults(_iviWrapper.getWaveform());
//					break;

//				case "widthtrigger" : 
//					_iviWrapper.setResults(_iviWrapper.getWaveform_Triggered((double)ise.getArguments()[0],
//						(double)ise.getArguments()[1],(double)ise.getArguments()[2],
//						(int)ise.getArguments()[3],(int)ise.getArguments()[4]));
//					break;
				/* TriggerVal , lowTresh, hiTresh, OPt1,
				 * Level - trigger lvl property?
				 * LO - The low width threshold time. The units are seconds 
				 * HI - The high width threshold time. The units are seconds
				 * POLARITY of the pulse that triggers the osc. - positive/negative/either
				 * CONDITION - within/outside the lo/hi Tres.
				 */
		
//				case "trigger" : 
//					_iviWrapper.setResults(_iviWrapper.getWaveform_Triggered((double)ise.getArguments()[0], (boolean)ise.getArguments()[1])); 
//					break;
//				default : 
//					try {
//						_iviWrapper.getClass().getMethod(e.getName()).invoke(_iviWrapper);
//					} catch (NoSuchMethodException e1) {e1.printStackTrace();
//					} catch (SecurityException e1) {e1.printStackTrace();
//					} catch (IllegalAccessException e1) {e1.printStackTrace();
//					} catch (IllegalArgumentException e1) { e1.printStackTrace();
//					} catch (InvocationTargetException e1) { e1.printStackTrace(); }
//			} 
	}

	@Override
	public void runAndTrace()
	{
		_sampleData = new double[0];

		// !TODO the JNI wrapper for IVI driver needs refactoring!
		
		if (_parameters.UpperThreshold != Double.NaN)
		{
			// The jniTrigger function sets a trigger and fetches waveform (but does not seem to wait for trigger!)
			// first parameter is trigger value.
			// second parameters seems to be rising / falling (?!)
			_sampleData = _iviWrapper.trigger(_parameters.UpperThreshold, true);
		}
		else if (_parameters.LowerThreshold != Double.NaN)
		{
			// The jniTrigger function sets a trigger and fetches waveform (but does not seem to wait for trigger!)
			// first parameter is trigger value.
			// second parameters seems to be rising / falling (?!)
			_sampleData = _iviWrapper.trigger(_parameters.UpperThreshold, false);
		}
		else
		{
			// This simply fetches the waveform, but also does autoscaling (?!)
			_sampleData = _iviWrapper.getWaveform();
		}
	}
	
	@Override
	public void fillTraceDB()
	{
		String uid = PTSpecUtils.getElementUID(getSingleSignal());

		// this shuld calculate correct offset to start of measurement and delta between two samples.
//		double sr = _iviWrapper.getSampleRate();
//		long time = 0;
//		time = (1000000*_iviWrapper.getTriggerDelay());
//		// (1ns/SampleRate) * 50 * , da wir 1 von 50 werte in java bekommen, * reducefactor nochmal.
//		long delta = (long)(1000000000/sr) * 50 * _iviWrapper.getSampleRateReduceFactor(); 
//		time += (long) _iviWrapper.getStartTime(); 
//		time -= _sampleData.length*delta;
		
		double timeMS = 0;
		for (int i = 0; i < _sampleData.length; i++)
		{
			// time calculated above is in nanoseconds, so we need ((double)time)/1000/1000 for milliseonds
			// time += delta;
			
			timeMS += 1;
			_trace.getTraceDB().insertValueMS(timeMS, TraceDBEvent.Write, uid, String.valueOf(_sampleData[i]));
		}
		
	}

	private PinSignal getSingleSignal()
	{
		IOPin ioPin = (IOPin)(ModelUtils.getInspectable(_accessDriver.getInspector()));
		if (ioPin.getPinSignals().size() == 1)
			return ioPin.getPinSignals().get(0);
		else
			throw new RuntimeException("IOPin not supported by IVITraceDriver: must specify exactly one signal");
	}
}
