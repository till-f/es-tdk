package fzi.mottem.runtime.reportgenerator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import fzi.mottem.runtime.interfaces.ITest;

public class ReportHelper
{
	public static String getSeverityImagePath(ITest test)
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(test.getProject());
		IFolder imgFolder = project.getFolder(new Path("/reports/img"));
		IFile yesFile = imgFolder.getFile("yes.png");
		IFile warnFile = imgFolder.getFile("warn.png");
	    IFile noFile = imgFolder.getFile("no.png");
	    
		String pathToImage = noFile.getLocation().toOSString();
		switch(test.getTestState())
		{
			case Passed: pathToImage = yesFile.getLocation().toOSString(); break;
			case Indecisive: pathToImage = warnFile.getLocation().toOSString(); break;
			case Failed: pathToImage = noFile.getLocation().toOSString(); break;
		}
		
		return pathToImage;
	}

	public static String getSeverityImagePath(ITest test, int indecisiveCount, int failedCount)
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(test.getProject());
		IFolder imgFolder = project.getFolder(new Path("/reports/img"));
		IFile yesFile = imgFolder.getFile("yes.png");
		IFile warnFile = imgFolder.getFile("warn.png");
	    IFile noFile = imgFolder.getFile("no.png");

	    if (failedCount > 0)
		{
			return noFile.getLocation().toOSString();
		}
		else if (indecisiveCount > 0)
		{
			return warnFile.getLocation().toOSString();
		}
		else
		{
			return yesFile.getLocation().toOSString();
		}
	}

}
