package fzi.mottem.runtime.util;

import java.util.UUID;

import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.interfaces.ITrace;

public abstract class AbstractAsyncExecutable implements Runnable
{
	private final String _uid;
	private final IRuntime _runtime;
	private final ITrace _trace;
	private final Thread _backgroundThread;
	
	protected volatile boolean _interrupted = false;
	
	public Thread getThread()
	{
		return _backgroundThread;
	}
	
	public String getUID()
	{
		return _uid;
	}

	public AbstractAsyncExecutable(String asyncExecUID, IRuntime runtime, ITrace trace)
	{
		if (asyncExecUID == null || asyncExecUID.isEmpty())
			_uid = "anonAsyncExec_" + UUID.randomUUID().toString();
		else
			_uid = asyncExecUID;
		
		_runtime = runtime;
		_trace = trace;
		
		_backgroundThread = new Thread(this, "AsyncExecutable for '" + _uid + "'");
		construct();
	}
	
	private void construct()
	{
		_runtime.registerAsyncExecutable(this);
		
		if (_trace != null)
			_trace.registerAsyncExecutable(this);
	}
	
	/*
	 * The code to be executed asynchronously. Is generated by PTSpec compiler.
	 */
	public abstract void execute() throws InterruptedException;
	
	@Override
	public void run()
	{
		try
		{
			execute();
		}
		catch (InterruptedException e)
		{
			if (_interrupted)
				return;
			else
				throw new RuntimeException("Internal failure: AsyncExecutable interrupted, but _interrupted flag was not set", e);
		}
		catch (ExecutionInterruptedException e)
		{
			// ignore quitely (execution was interrupted)
		}
		catch (Throwable t)
		{
			System.err.println("An unhandled exception was thrown in async execution");
			t.printStackTrace();
		}
		finally
		{
			_runtime.unregisterAsyncExecutable(this);
			
			if (_trace != null)
				_trace.unregisterAsyncExecutable(this);

			System.out.println("Finished async execution " + _uid);
		}
	}

	public void start()
	{
		System.out.println("Starting async execution " + _uid);

		_backgroundThread.start();
	}
	
	public void stop()
	{
		_interrupted = true;
		if (_backgroundThread != null)
			_backgroundThread.interrupt();
	}

}
