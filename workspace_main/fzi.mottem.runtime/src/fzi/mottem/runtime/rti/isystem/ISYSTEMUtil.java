package fzi.mottem.runtime.rti.isystem;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;

import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.testrigmodel.ISystemInspectorContainer;

public class ISYSTEMUtil
{

	public static IPath getAbsoluteWorkspacePath(IInspector inspector)
	{
		EObject inspectorContainer = inspector.eContainer();
		
		if (inspectorContainer instanceof ISystemInspectorContainer)
		{
			IPath eclipseWorkspacePath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation();
			String winIdeaPathStr = ((ISystemInspectorContainer) inspectorContainer).getWorkspacePath();
			
			if (winIdeaPathStr == null)
				throw new RuntimeException("WinIDEA workspace Path not set for Inspector '" + inspector.getName() + "'");
			
			IPath winIdeaPath = new Path(winIdeaPathStr);
			
			return eclipseWorkspacePath.append(winIdeaPath);
		}
		else
		{
			throw new RuntimeException("Unexpected Inspector type for iSYSTEM Debugger: " + inspector.getClass().getSimpleName());
		}
	}
	
	public static String getWinIDEABinaryName(String binaryFile)
	{
		if (binaryFile == null || binaryFile.isEmpty())
			throw new RuntimeException("Binary file property in the TestRigModel is not set correctly");
		int idx = binaryFile.lastIndexOf('/');
		return binaryFile.substring(idx + 1);
	}
	
}
