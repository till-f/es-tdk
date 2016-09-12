package fzi.mottem.ptspec.dsl.resources.ui;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.osgi.framework.BundleContext;

import fzi.mottem.ptspec.dsl.resources.codemodel.CodeModelRuntimeModule;
import fzi.mottem.ptspec.dsl.resources.datastreammodel.DataStreamModelRuntimeModule;
import fzi.mottem.ptspec.dsl.resources.environmentdatamodel.EnvironmentDataModelRuntimeModule;
import fzi.mottem.ptspec.dsl.resources.testrigmodel.TestRigModelRuntimeModule;
import fzi.mottem.ptspec.dsl.resources.ui.codemodel.CodeModelUiModule;
import fzi.mottem.ptspec.dsl.resources.ui.datastreammodel.DataStreamModelUiModule;
import fzi.mottem.ptspec.dsl.resources.ui.environmentdatamodel.EnvironmentDataModelUiModule;
import fzi.mottem.ptspec.dsl.resources.ui.testrigmodel.TestRigModelUiModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

public class Activator extends AbstractUIPlugin {

	private static final Logger logger = Logger.getLogger(Activator.class);
	 
    // The plug-in ID
    public static final String PLUGIN_ID = "fzi.mottem.ptspec.dsl.resources.ui";
 
    // The shared instance
    private static Activator plugin;
 
    private Injector injector_TestRigResouce;
 
    private Injector injector_CodeResouce;
    
    private Injector injector_DataStreamResouce;
    
    private Injector injector_EnvironmentDataResouce;
    
    /**
     * The constructor
     */
    public Activator() {
    }
 
    public Injector getTestRigResourceInjector() {
        return injector_TestRigResouce;
    }
 
    public Injector getCodeResourceInjector() {
        return injector_CodeResouce;
    }
 
    public Injector getDataStreamResourceInjector() {
        return injector_DataStreamResouce;
    }
 
    public Injector getInspectorAttributesResourceInjector() {
        return injector_EnvironmentDataResouce;
    }
 
    private void initializeEcoreInjectors() {
        injector_TestRigResouce = Guice.createInjector(
                Modules.override(Modules.override(new TestRigModelRuntimeModule())
                .with(new TestRigModelUiModule(plugin)))
                .with(new SharedStateModule()));

        injector_CodeResouce = Guice.createInjector(
                Modules.override(Modules.override(new CodeModelRuntimeModule())
                .with(new CodeModelUiModule(plugin)))
                .with(new SharedStateModule()));

        injector_DataStreamResouce = Guice.createInjector(
                Modules.override(Modules.override(new DataStreamModelRuntimeModule())
                .with(new DataStreamModelUiModule(plugin)))
                .with(new SharedStateModule()));

        injector_EnvironmentDataResouce = Guice.createInjector(
                Modules.override(Modules.override(new EnvironmentDataModelRuntimeModule())
                .with(new EnvironmentDataModelUiModule(plugin)))
                .with(new SharedStateModule()));
    }
 
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        try {
            initializeEcoreInjectors();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
 
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        injector_TestRigResouce = null;
        super.stop(context);
    }
 
    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }
    
    public void log(int severity, String msg) {
        log(severity, msg, null);
    }
    
    public void log(int severity, String msg, Exception e) {
        getLog().log(new Status(severity, PLUGIN_ID, Status.OK, msg, e));
    }
    
}
