package fzi.mottem.runtime.rti;

import java.util.LinkedList;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.ITrace;
import fzi.mottem.runtime.rti.interfaces.ITraceDriver;

public abstract class AbstractTraceDriver implements ITraceDriver
{
	protected class InternalSetupEvent
	{
		private final double _timeStamp;
		private final IExecutor _executor;
		private final ITrace.SetupEvent _setupEvent;
		private final ITestReferenceable _element;
		private final EItemProperty _property;
		private final Object _value;
		private final Object[] _arguments;
		
		public InternalSetupEvent(double timeStamp, IExecutor executor, ITrace.SetupEvent setupEvent, ITestReferenceable element, EItemProperty property, Object value, Object... arguments)
		{
			_timeStamp = timeStamp;
			_executor = executor;
			_setupEvent = setupEvent;
			_element = element;
			_property = property;
			_arguments = arguments;
			_value = value;
		}

		public double getTimeStamp()
		{
			return _timeStamp;
		}

		public IExecutor getExecutor()
		{
			return _executor;
		}

		public ITrace.SetupEvent getSetupEvent()
		{
			return _setupEvent;
		}

		public ITestReferenceable getElement()
		{
			return _element;
		}

		public EItemProperty getProperty()
		{
			return _property;
		}

		public Object[] getArguments()
		{
			return _arguments;
		}

		public Object getValue()
		{
			return _value;
		}
	}

	protected final ITrace _trace;
	protected final AbstractAccessDriver _accessDriver;
	
	protected final LinkedList<InternalSetupEvent> _setupEvents = new LinkedList<InternalSetupEvent>();
	
	public ITrace getTrace()
	{
		return _trace;
	}

	public AbstractAccessDriver getAccessDriver()
	{
		return _accessDriver;
	}

	public AbstractTraceDriver(ITrace trace, AbstractAccessDriver inspector)
	{
		_trace = trace;
		_accessDriver = inspector;
	}
	
	@Override
	public <T> void addStimulus(double timeStamp, IExecutor executor, ITrace.SetupEvent setupEvent, ITestReferenceable element, EItemProperty property, T value, Object... arguments)
	{
		_setupEvents.add(new InternalSetupEvent(timeStamp, executor, setupEvent, element, property, value, arguments));
	}

}
