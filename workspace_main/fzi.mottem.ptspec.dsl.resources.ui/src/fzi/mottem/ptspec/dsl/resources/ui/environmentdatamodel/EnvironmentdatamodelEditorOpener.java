package fzi.mottem.ptspec.dsl.resources.ui.environmentdatamodel;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.ui.editor.LanguageSpecificURIEditorOpener;

import fzi.mottem.model.environmentdatamodel.presentation.EnvironmentdatamodelEditor;

public class EnvironmentdatamodelEditorOpener extends LanguageSpecificURIEditorOpener {

    @Override
    protected void selectAndReveal(IEditorPart openEditor, URI uri,
            EReference crossReference, int indexInList, boolean select) {
    	EnvironmentdatamodelEditor umlEditor = (EnvironmentdatamodelEditor) openEditor.getAdapter(EnvironmentdatamodelEditor.class);
        if (umlEditor != null) {
            EObject eObject = umlEditor.getEditingDomain().getResourceSet().getEObject(uri, true);
            umlEditor.setSelectionToViewer(Collections.singletonList(eObject));
        }
    }
    
}
