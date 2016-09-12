package fzi.mottem.ptspec.dsl.resources.datastreammodel;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

public class DataStreamModelRuntimeModule extends AbstractGenericResourceRuntimeModule {

	@Override
    protected String getLanguageName() {
        return "fzi.mottem.model.datastreammodel.presentation.DatastreammodelEditorID";
    }
 
    @Override
    protected String getFileExtensions() {
        return "etm-dstream";
    }
 
    public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
        return DataStreamModelResourceDescriptionStrategy.class;
    }
 
    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return DataStreamModelQualifiedNameProvider.class;
    }
    
}
