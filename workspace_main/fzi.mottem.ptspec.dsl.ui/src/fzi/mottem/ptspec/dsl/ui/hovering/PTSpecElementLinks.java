package fzi.mottem.ptspec.dsl.ui.hovering;

import java.net.URI;

import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.xtext.ui.editor.hover.html.XtextElementLinks;

import fzi.mottem.ptspec.dsl.ui.PTSpecActivatorCustom;
import fzi.mottem.ptspec.runtestlistener.IRunTestListener;
import fzi.util.eclipse.IntegrationUtils;

public class PTSpecElementLinks extends XtextElementLinks
{
	public static final String PTSPEC_SCHEME = "ptspec";
	public static final String URL_CMD_RUNTEST = "runtest";

	public LocationListener createLocationListener(final ILinkHandler handler)
	{
		return new PTSpecLinkAdapter(handler);
	}

	protected class PTSpecLinkAdapter extends XtextLinkAdapter
	{
		protected PTSpecLinkAdapter(ILinkHandler handler)
		{
			super(handler);
		}
		
		@Override
		public void changing(LocationEvent event)
		{
			URI uri = initURI(event);
			
			if(uri == null)
				return;
			
			String scheme= uri.getScheme();
			
			if (PTSpecElementLinks.PTSPEC_SCHEME.equals(scheme))
			{
				String commandString = event.location.split("//")[1];
				
				if (commandString.startsWith(URL_CMD_RUNTEST))
				{
					String[] runTestArguments = commandString.substring(URL_CMD_RUNTEST.length() + 1).split(":");

					IRunTestListener runTestListener = PTSpecActivatorCustom.getInstanceCustom().getRunTestListener();
					runTestListener.runTest(IntegrationUtils.getProjectForName(runTestArguments[0]), runTestArguments[1]);
				}
			}
			else
			{
				super.changing(event);
			}
		}
		
	}
}
