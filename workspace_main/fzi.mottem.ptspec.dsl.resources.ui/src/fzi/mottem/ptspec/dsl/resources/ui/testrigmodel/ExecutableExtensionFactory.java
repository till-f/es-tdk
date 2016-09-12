package fzi.mottem.ptspec.dsl.resources.ui.testrigmodel;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import fzi.mottem.ptspec.dsl.resources.ui.Activator;

public class ExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
    protected Bundle getBundle() {
        return Activator.getDefault().getBundle();
    }
 
    @Override
    protected Injector getInjector() {
        return Activator.getDefault().getTestRigResourceInjector();
    }
    
}
