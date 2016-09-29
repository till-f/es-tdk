package fzi.mottem.runtime.util;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class PTSpecConsole
{
	
	private static PTSpecConsole _instance = null;
	
	public static PTSpecConsole getInstance()
	{
		if (_instance == null)
		{
			_instance = new PTSpecConsole();
		}
		
		return _instance;
	}
	
	private final MessageConsole _messageConsole;
	private final MessageConsoleStream _messageConsoleStream;

	private PTSpecConsole()
	{
		_messageConsole = new MessageConsole("ES TDK Console", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{ _messageConsole });
		
		_messageConsoleStream = new MessageConsoleStream(_messageConsole);
	}
	
	public void activate()
	{
		_messageConsole.activate();
	}

	public void println_background(String str)
	{
		_messageConsoleStream.println(str);
	}

}
