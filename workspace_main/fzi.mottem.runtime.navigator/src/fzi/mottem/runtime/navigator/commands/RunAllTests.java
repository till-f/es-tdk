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
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestSuiteDeclaration;
import fzi.mottem.ptspec.dsl.ui.PTSpecActivatorCustom;
import fzi.mottem.ptspec.runtestlistener.IRunTestListener;

public class RunAllTests implements IActionDelegate{

	public RunAllTests() {
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
			Object testSuiteDecl = selection.getFirstElement();
			
			if (testSuiteDecl instanceof PTSTestSuiteDeclaration && selection.getPaths().length > 0)
			{
				//Object parent = selection.getPaths()[0].getParentPath().getLastSegment();
				
				PTSTestSuiteDeclaration testsuite = (PTSTestSuiteDeclaration)testSuiteDecl;
				//System.out.println("Run Suite: " + testsuite.getName() + " with " + testsuite.getList().getTests().size() + " tests");
				if (testsuite instanceof PTSTestSuiteDeclaration && selection.getPaths().length > 0)
				{
					Object parent = selection.getPaths()[0].getParentPath().getLastSegment();
					
					if (parent instanceof IFile)
					{
						IRunTestListener runTestListener = PTSpecActivatorCustom.getInstanceCustom().getRunTestListener();
						runTestListener.runTest(((IFile) parent).getProject(), PTS2JavaUtil.getJavaFullQualifiedClassName(testsuite));
					
					} 
				}
				
				/*	for (PTSTestDeclaration test : testsuite.getList().getTests()) {
					if (test instanceof PTSTestDeclaration && selection.getPaths().length > 0)
					{
						Object parent = selection.getPaths()[0].getParentPath().getLastSegment();
						
						if (parent instanceof IFile)
						{
							IRunTestListener runTestListener = PTSpecActivatorCustom.getInstanceCustom().getRunTestListener();
							runTestListener.runTest(((IFile) parent).getProject(), PTS2JavaUtil.getJavaFullQualifiedClassName(test));
							System.out.println("Done with test " + test.getName());
						} 
					}
				} */
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
