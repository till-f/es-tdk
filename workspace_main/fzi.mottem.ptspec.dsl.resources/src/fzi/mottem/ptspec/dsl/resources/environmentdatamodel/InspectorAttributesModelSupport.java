package fzi.mottem.ptspec.dsl.resources.environmentdatamodel;

import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Module;

public class InspectorAttributesModelSupport extends AbstractGenericResourceSupport {

    @Override
    protected Module createGuiceModule() {
        return new EnvironmentDataModelRuntimeModule();
    }
    
}
