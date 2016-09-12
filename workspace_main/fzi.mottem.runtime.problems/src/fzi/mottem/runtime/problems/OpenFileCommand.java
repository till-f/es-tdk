package fzi.mottem.runtime.problems;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import fzi.util.ecore.EcoreUtils;

public class OpenFileCommand {

	public static void open(String fileStr, int line)
	{
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();

		String[] fileStrParts = fileStr.split("/");
		IProject pr = myWorkspaceRoot.getProject(fileStrParts[2]);

		if (pr.exists() && !pr.isOpen())
		try {
			pr.open(null);
		} catch (CoreException e) {
			return;
		}
		
		URI fileURI = URI.createURI(fileStr);
		IFile file = EcoreUtils.getFileForEMFURI(fileURI);

		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		
		try
		{
			IEditorPart editorpart = page.openEditor(new FileEditorInput(file), desc.getId());
			revealInEditor(editorpart, line, 6);
		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static void revealInEditor(IEditorPart editor, final int offset, final int length) {
		if (editor instanceof ITextEditor) {
			((ITextEditor)editor).selectAndReveal(offset, length);
			return;
		}

		// Support for non-text editor - try IGotoMarker interface
		final IGotoMarker gotoMarkerTarget;
		if (editor instanceof IGotoMarker)
			gotoMarkerTarget= (IGotoMarker)editor;
		else
			gotoMarkerTarget= editor != null ? (IGotoMarker)editor.getAdapter(IGotoMarker.class) : null;
		if (gotoMarkerTarget != null) {
			final IEditorInput input= editor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				WorkspaceModifyOperation op= new WorkspaceModifyOperation() {
					protected void execute(IProgressMonitor monitor) throws CoreException {
						IMarker marker= null;
						try {
							marker= ((IFileEditorInput)input).getFile().createMarker(IMarker.TEXT);
							marker.setAttribute(IMarker.CHAR_START, offset);
							marker.setAttribute(IMarker.CHAR_END, offset + length);

							gotoMarkerTarget.gotoMarker(marker);

						} finally {
							if (marker != null)
								marker.delete();
						}
					}
				};

				try {
					op.run(null);
				} catch (InvocationTargetException ex) {
					// reveal failed
				} catch (InterruptedException e) {
					Assert.isTrue(false, "this operation can not be canceled"); //$NON-NLS-1$
				}
			}
			return;
		}
	}
}
