package fzi.mottem.ptspec.dsl.resources.testrigmodel;

import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Module;

public class TestRigModelSupport extends AbstractGenericResourceSupport {

    @Override
    protected Module createGuiceModule() {
        return new TestRigModelRuntimeModule();
    }
    
}
