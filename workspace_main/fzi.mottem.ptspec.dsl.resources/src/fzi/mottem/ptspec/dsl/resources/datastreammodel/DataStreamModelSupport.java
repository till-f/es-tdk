package fzi.mottem.ptspec.dsl.resources.datastreammodel;

import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Module;

public class DataStreamModelSupport extends AbstractGenericResourceSupport {

    @Override
    protected Module createGuiceModule() {
        return new DataStreamModelRuntimeModule();
    }
    
}
