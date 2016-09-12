package fzi.mottem.runtime.navigator.commands;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fzi.mottem.ptspec.compiler.util.PTS2JavaUtil;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.ui.PTSpecActivatorCustom;
import fzi.mottem.ptspec.runtestlistener.IRunTestListener;

public class RunTest implements IActionDelegate{

	public RunTest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		try
		{
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();
			TreeSelection selection = (TreeSelection)page.getSelection();
			Object testDecl = selection.getFirstElement();
			
			if (testDecl instanceof PTSTestDeclaration && selection.getPaths().length > 0)
			{
				Object parent = selection.getPaths()[0].getParentPath().getLastSegment();
				
				if (parent instanceof IFile)
				{
					IRunTestListener runTestListener = PTSpecActivatorCustom.getInstanceCustom().getRunTestListener();
					runTestListener.runTest(((IFile) parent).getProject(), PTS2JavaUtil.getJavaFullQualifiedClassName((PTSTestDeclaration)testDecl));
					
				} 
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
