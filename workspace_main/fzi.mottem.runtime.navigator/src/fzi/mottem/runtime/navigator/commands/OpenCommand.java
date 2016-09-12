package fzi.mottem.runtime.navigator.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;

public class OpenCommand extends AbstractHandler {
	
	public static void open() {
		try
		{
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();
			TreeSelection selection = (TreeSelection)page.getSelection();
			Object selectedItem = selection.getFirstElement();
			
			if (selectedItem instanceof IFile) {
				
				IFile selectedFile = (IFile) selectedItem;
				IEditorDescriptor desc = PlatformUI.getWorkbench().
				        getEditorRegistry().getDefaultEditor(selectedFile.getName());
				page.openEditor(new FileEditorInput(selectedFile), desc.getId());
			}
			
			if (selectedItem instanceof PTSTestDeclaration && selection.getPaths().length > 0)
			{
				Object parent = selection.getPaths()[0].getParentPath().getLastSegment();
				
				if (parent instanceof IFile)
				{
					
					IFile selectedFile = (IFile) parent;
					IEditorDescriptor desc = PlatformUI.getWorkbench().
					        getEditorRegistry().getDefaultEditor(selectedFile.getName());
					page.openEditor(new FileEditorInput(selectedFile), desc.getId());
				} 
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		open();
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
