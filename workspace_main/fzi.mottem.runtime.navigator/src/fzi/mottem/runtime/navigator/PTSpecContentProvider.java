package fzi.mottem.runtime.navigator;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import fzi.mottem.ptspec.dsl.common.PTSpecConstants;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.util.ecore.EcoreUtils;

public class PTSpecContentProvider implements ITreeContentProvider, IResourceChangeListener
{
    Viewer _viewer;
    
    public PTSpecContentProvider()
    {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
	@Override
	public void dispose() 
	{
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
    
	@Override
    public void resourceChanged(IResourceChangeEvent event)
    {
    	Display.getDefault().asyncExec(new Runnable() 
	    	{
				public void run() 
				{ 
					TreeViewer viewer = (TreeViewer) _viewer;
				     
			        TreePath[] treePaths = viewer.getExpandedTreePaths();
			        viewer.refresh();
			        viewer.setExpandedTreePaths(treePaths);
			    }
			});
    }
    
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
	    _viewer = viewer;
	    
	    _viewer.refresh();
	}
	
    @Override
    public Object[] getChildren(Object parentElement)
    {
    	if (parentElement instanceof IFile)
    	{
    		String parentextension = ((IFile) parentElement).getFileExtension();
			if (parentextension != null && parentextension.equals(PTSpecConstants.FILE_EXTENSION))
			{				
				IFile file = (IFile) parentElement;
				URI codeInstanceURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				
				PTSRoot ptsRoot;
				try
				{
					
					ptsRoot = (PTSRoot)EcoreUtils.loadFullEMFModel(codeInstanceURI);
					if (ptsRoot != null && ptsRoot.getContainerDeclarations().size() > 0)
					{
						return ptsRoot.getContainerDeclarations().toArray();
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return new Object[0];
    }
	
	@Override
	public Object[] getElements(Object inputElement)
	{
		return getChildren(inputElement);
	}

	@Override
	public Object getParent(Object element)
	{
	    Object parent = null;
	 
	    if (IProject.class.isInstance(element))
	    {
	        parent = ((IProject)element).getWorkspace().getRoot();
	    } 
	    else if (IFolder.class.isInstance(element)) 
	    {
	        parent = ((IFolder)element).getParent();
	    } 
	    else if (IFile.class.isInstance(element)) 
	    {
	    	parent = ((IFile)element).getParent();
	    }
	 
	    return parent;
	}

	@Override
	public boolean hasChildren(Object element)
	{
		return this.getChildren(element).length > 0;
	}

}
