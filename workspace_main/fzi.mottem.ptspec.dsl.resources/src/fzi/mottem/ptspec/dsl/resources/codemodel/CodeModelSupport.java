package fzi.mottem.ptspec.dsl.resources.codemodel;

import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Module;

public class CodeModelSupport extends AbstractGenericResourceSupport {

    @Override
    protected Module createGuiceModule() {
        return new CodeModelRuntimeModule();
    }
    
}
