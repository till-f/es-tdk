package fzi.mottem.runtime.util;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fzi.mottem.runtime.Runtime;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.interfaces.ITest;
import fzi.mottem.runtime.reportgenerator.ReportGenerator;

public class PTSpecRunTestsJob extends Job
{
	
	public static final int TEST_CANCEL_TIMEOUT_MILLIS = 500;

	// multiple test runners in parallel would be a mess!
	private static final Lock TEST_RUNNER_LOCK = new ReentrantLock();
	
	private final LinkedList<ITest> _tests = new LinkedList<ITest>();
	private final Runtime _runtime;

	public PTSpecRunTestsJob(Runtime runtime, List<Class<ITest>> testClasses)
	{
		super("Execute PTSpec test case(s)");
		
		_runtime = runtime;

		this.setPriority(Job.BUILD);
		
		for (Class<ITest> testClass : testClasses)
		{
			try
			{
				ITest test = testClass.getConstructor(IRuntime.class).newInstance(_runtime);
				_tests.add(test);
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		if (TEST_RUNNER_LOCK.tryLock())
		{
			try
			{
				monitor.beginTask("", _tests.size() * 4);
				
				_runtime.getUIDResolver().reset();

				for (final ITest test : _tests)
				{
					monitor.subTask("Test " + test.getName() + ": initializing");

					test.init();

					monitor.worked(1); // INIT done
					
					monitor.subTask("Test" + test.getName() + ": running");
					
					/**
					 * This "pretty" construction of wrapping a Runnable inside a Runnable is
					 * required to guide the ClassLoader.
					 * See {@link http://stackoverflow.com/questions/23392620/java-classloader-stops-working-when-used-in-new-thread-runnable}
					 */
					Thread testRunnerThread = new Thread(new Runnable()
						{
							public void run() { test.run(); }
						}
						, "TestRunner for '" + test.getName() + "'");
					testRunnerThread.start();
					
					while (testRunnerThread.isAlive())
					{
						if (monitor.isCanceled())
						{
							testRunnerThread.interrupt();
							
							try
							{
								testRunnerThread.join(TEST_CANCEL_TIMEOUT_MILLIS);
								
								if (testRunnerThread.isAlive())
								{
									System.err.println("Request for cancelation of test execution timed out; forced stop");
									
									// use brutal force to stop test execution if no reaction after timeout
									testRunnerThread.stop();
									break;
								}
							}
							catch (InterruptedException e)
							{
								System.err.println("Interrupted while waiting for TestRunner thread; this should not occur.");
								e.printStackTrace();
								break;
							}
						}
						
						try
						{
							Thread.sleep(200);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
							return Status.CANCEL_STATUS;
						}
					}
					monitor.worked(1); // RUN done

					monitor.subTask("Test " + test.getName() + ": clean-up");

					test.cleanup();
					
					monitor.worked(1); // CLEANUP done
					
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;

					monitor.subTask("Test " + test.getName() + ": report generation");

					ReportGenerator rg = new ReportGenerator(test);
					rg.generate();

					monitor.worked(1); // REPORT GENERATION done

					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
				}
			}
			finally
			{
				TEST_RUNNER_LOCK.unlock();
			}
			
			return Status.OK_STATUS;
		}
		else
		{
			return Status.CANCEL_STATUS;
		}
	}

}
