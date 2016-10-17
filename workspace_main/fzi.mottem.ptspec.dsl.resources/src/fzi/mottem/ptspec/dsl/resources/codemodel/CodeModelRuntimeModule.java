package fzi.mottem.ptspec.dsl.resources.codemodel;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

import fzi.mottem.model.util.ModelUtils;

public class CodeModelRuntimeModule extends AbstractGenericResourceRuntimeModule {

	@Override
    protected String getLanguageName() {
        return "fzi.mottem.model.codemodel.presentation.CodemodelEditorID";
    }
 
    @Override
    protected String getFileExtensions() {
        return ModelUtils.FILE_EXTENSION_CODE_MODEL;
    }
 
    public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
        return CodeModelResourceDescriptionStrategy.class;
    }
 
    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return CodeModelQualifiedNameProvider.class;
    }
    
}
