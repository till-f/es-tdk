package fzi.mottem.ptspec.dsl.resources.environmentdatamodel;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

public class EnvironmentDataModelRuntimeModule extends AbstractGenericResourceRuntimeModule {

	@Override
    protected String getLanguageName() {
        return "fzi.mottem.model.InspectorAttributesModel.presentation.EnvironmentdatamodelEditorID";
    }
 
    @Override
    protected String getFileExtensions() {
        return "etm-edm";
    }
 
    public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
        return InspectorAttributesModelResourceDescriptionStrategy.class;
    }
 
    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return InspectorAttributesModelQualifiedNameProvider.class;
    }
    
}
