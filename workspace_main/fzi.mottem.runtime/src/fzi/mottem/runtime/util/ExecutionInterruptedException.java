package fzi.mottem.runtime.util;

/**
 * This Exception is needed as wrapper for the regular InterruptedException.
 * A version deriving from RuntimeException is required to solve the issue
 * that *generated* code like ITest.run() might contain calls to functions
 * throwing InterruptedException, but cannot be marked "throws InterruptedException"
 * because it could be that code is generated that does not contain any 
 * respective calls leeding to compilation errors with the throws declaration...
 */
public class ExecutionInterruptedException extends RuntimeException
{

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 8723050697373253140L;
	
	public ExecutionInterruptedException()
	{
		super();
	}

	public ExecutionInterruptedException(Exception e)
	{
		super(e);
	}

	public ExecutionInterruptedException(String message, Exception e)
	{
		super(message, e);
	}

}
