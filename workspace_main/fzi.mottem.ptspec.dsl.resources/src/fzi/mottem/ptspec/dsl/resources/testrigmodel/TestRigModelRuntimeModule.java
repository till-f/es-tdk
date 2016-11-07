package fzi.mottem.ptspec.dsl.resources.testrigmodel;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

import fzi.mottem.model.util.ModelUtils;

public class TestRigModelRuntimeModule extends AbstractGenericResourceRuntimeModule {

	@Override
    protected String getLanguageName() {
        return "fzi.mottem.model.testrigmodel.presentation.TestrigmodelEditorID";
    }
 
    @Override
    protected String getFileExtensions() {
        return ModelUtils.FILE_EXTENSION_TESTRIG_MODEL;
    }
 
    public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
        return TestRigModelResourceDescriptionStrategy.class;
    }
 
    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return TestRigModelQualifiedNameProvider.class;
    }
    
}
