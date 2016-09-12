package fzi.mottem.model;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ModelPlugin
{
	public static final String PLUGIN_ID = "fzi.mottem.model";
	
	public static final Bundle PLUGIN_BUNDLE = Platform.getBundle(PLUGIN_ID);

}
