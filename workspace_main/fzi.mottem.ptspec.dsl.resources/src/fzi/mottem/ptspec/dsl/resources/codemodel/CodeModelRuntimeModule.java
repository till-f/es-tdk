package fzi.mottem.ptspec.dsl.resources.codemodel;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

public class CodeModelRuntimeModule extends AbstractGenericResourceRuntimeModule {

	@Override
    protected String getLanguageName() {
        return "fzi.mottem.model.codemodel.presentation.CodemodelEditorID";
    }
 
    @Override
    protected String getFileExtensions() {
        return "etm-code";
    }
 
    public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
        return CodeModelResourceDescriptionStrategy.class;
    }
 
    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return CodeModelQualifiedNameProvider.class;
    }
    
}
