package fzi.util.eclipse;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class IntegrationUtils
{
	public static IResource getActiveResource() throws Exception
	{
	    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    
	    if (workbenchWindow == null)
	    	throw new Exception("Cannot get active resource (no active WorkbenchWindow)");
	    
    	ISelection selection = workbenchWindow.getSelectionService().getSelection();
    	
    	if (selection instanceof IStructuredSelection)
    	{
    		// if the selection is a structured selection, we should be able to
    		// get the resource by using the adapter for IResource
    		
        	Object element = ((IStructuredSelection) selection).getFirstElement();
	    	if (element instanceof IAdaptable)
	        {
	    		Object obj = ((IAdaptable)element).getAdapter(IResource.class);
	    		return (IResource)obj;
	        }
    	}
    	
		// Oterwise we try to get the resource over the active active editor

		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		
		if (workbenchPage == null)
			throw new Exception("Cannot get active resource (no active WorkbenchPage)");
		
		IEditorPart editor = workbenchPage.getActiveEditor();

		if (editor == null)
			throw new Exception("Cannot get active resource (no active EditorPart)");

		IEditorInput input = editor.getEditorInput();
		
		if (input == null)
			throw new Exception("Cannot get active resource (no EditorInput)");

		Object obj = input.getAdapter(IResource.class);
		return (IResource)obj;
	}
	
	public static List<IResource> getResources(IPath startLocation, String fileExtension)
	{
		List<IResource> resultResources = new LinkedList<IResource>();
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		getResources(resultResources, startLocation, workspaceRoot, fileExtension);
		return resultResources;
	}
	
	private static void getResources(List<IResource> resultResources, IPath startLocation, IWorkspaceRoot workspaceRoot, String fileExtension)
	{
		IContainer container = workspaceRoot.getContainerForLocation(startLocation);
		
	    try
	    {
	        IResource[] allResources;
	        allResources = container.members();
	        
	        for (IResource resource : allResources)
	        {
	        	if (resource instanceof IProject)
	        	{
	        		if (!((IProject) resource).isOpen())
	        			continue;
	        	}
	        	
	            if (fileExtension.equalsIgnoreCase(resource.getFileExtension()))
	            {
	            	resultResources.add(resource);
	            }
	            
	            if (resource.getType() == IResource.FOLDER)
	            {
	                IPath tempPath = resource.getLocation();
	                getResources(resultResources, tempPath, workspaceRoot, fileExtension);
	            }

	            if (resource.getType() == IResource.PROJECT)
	            {
	                IPath tempPath = resource.getLocation();
	                getResources(resultResources, tempPath, workspaceRoot, fileExtension);
	            }

	        }
	    }
	    catch (CoreException e)
	    {
	        e.printStackTrace();
	    }
	}

	public static List<IResource> getResourcesOfProject(IProject project, String fileExtension)
	{
		return IntegrationUtils.getResources(project.getLocation(), fileExtension);
	}

	public static IProject getProjectForName(String projectName)
	{
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		return workspaceRoot.getProject(projectName);
	}
	
	public static IPath packageNameToPath(String packageName)
	{
		String path = packageName.replace('.', IPath.SEPARATOR);
		return new Path(path);
	}
	
	public static IPath getWorkspaceRelativePathForSystemPath(IPath systemPath)
	{
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		String wsPathStr = ws.getRoot().getLocation().toString();
		String sysPathStr = systemPath.toString();
		if (sysPathStr.startsWith(wsPathStr))
		{
			return new Path(sysPathStr.substring(wsPathStr.length() + 1)); 
		}
		else
		{
			throw new RuntimeException("'" + systemPath.toString() + "' is not within workspace.");
		}
	}
	
	public static IPath getSystemPathForWorkspaceRelativePath(IPath workspacePath)
	{
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		return ws.getRoot().getLocation().append(workspacePath);
	}
	
	public static void checkAndcreateFolder(IFolder folder)
	{
		if (!folder.exists())
		{
			try
			{
				folder.create(true, true, null);
			}
			catch (CoreException e)
			{
				if (!folder.exists())
					throw new RuntimeException("Could not create output folder for geneated sources");
			}
			while (!folder.exists());
		}
	}
}
