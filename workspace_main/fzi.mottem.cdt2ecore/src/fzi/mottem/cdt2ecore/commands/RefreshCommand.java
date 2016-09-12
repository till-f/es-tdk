package fzi.mottem.cdt2ecore.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import fzi.mottem.cdt2ecore.CDT2EcorePlugin;

public class RefreshCommand extends AbstractHandler 
{

	@Override
	public Object execute(ExecutionEvent event) 
	{
	    CDT2EcorePlugin.Instance.getCDT2Ecore().updateCurrentPTSProject();
		return null;
	}

}
