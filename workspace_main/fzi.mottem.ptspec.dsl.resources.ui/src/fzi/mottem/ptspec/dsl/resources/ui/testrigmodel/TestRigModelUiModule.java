package fzi.mottem.ptspec.dsl.resources.ui.testrigmodel;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;
import org.eclipse.xtext.ui.LanguageSpecific;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.resource.generic.EmfUiModule;

import fzi.mottem.ptspec.dsl.ui.hovering.PTSpecEObjectDocumentationProvider;
import fzi.mottem.ptspec.dsl.ui.hovering.PTSpecEObjectHoverProvider;

public class TestRigModelUiModule extends EmfUiModule
{

    public TestRigModelUiModule(AbstractUIPlugin plugin)
    {
        super(plugin);
    }
 
    @Override
    public void configureLanguageSpecificURIEditorOpener(com.google.inject.Binder binder)
    {
        binder.bind(IURIEditorOpener.class).annotatedWith(LanguageSpecific.class).to(TestrigmodelEditorOpener.class);
    }
    
	// for custom hovering
    public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider()
    {
        return PTSpecEObjectHoverProvider.class;
    }
 
	// for custom hovering
    public Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr()
    {
        return PTSpecEObjectDocumentationProvider.class;
    }
	    

}
