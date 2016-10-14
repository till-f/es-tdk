package fzi.mottem.code2model;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Code2ModelPlugin extends AbstractUIPlugin implements IStartup
{

	public static final String PLUGIN_ID = "fzi.mottem.cdt2ecore"; //$NON-NLS-1$

	public static Code2ModelPlugin Instance = null;
	
	private Code2Model _cdt2Ecore = null;

	public Code2ModelPlugin() 
	{
	}

	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		
		_cdt2Ecore = new Code2Model(this);
		
		Instance = this;
	}

	public void stop(BundleContext context) throws Exception 
	{
		Instance = null;
		
		_cdt2Ecore.shutdown();
		
		super.stop(context);
	}
	
    public Code2Model getCDT2Ecore()
    {
        return _cdt2Ecore;
    }

    @Override
	public void earlyStartup()
	{
		// Nothing to do. This plugin does not show a different behavior
		// when it is started using "early startup", thus there is no need to
		// distinguish between normal and early startup.
    	
    	// Nevertheless deriving from IStartup is needed, so that this plugin
    	// can register a handler for workspace changes (triggering re-load of code models)
		
		System.out.println("Successful early startup of CDT2Ecore plugin.");
	}

}