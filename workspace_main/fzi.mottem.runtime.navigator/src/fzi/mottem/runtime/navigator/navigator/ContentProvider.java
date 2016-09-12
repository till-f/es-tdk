package fzi.mottem.runtime.navigator.navigator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import fzi.mottem.ptspec.dsl.common.PTSpecConstants;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.util.ecore.EcoreUtils;

public class ContentProvider implements ITreeContentProvider, IResourceChangeListener {
	
    Viewer _viewer;
    
    public ContentProvider() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    @Override
    public void resourceChanged(IResourceChangeEvent event) {
    	
    	Display.getDefault().asyncExec(new Runnable() {
			public void run() { 
				TreeViewer viewer = (TreeViewer) _viewer;
			     
		        TreePath[] treePaths = viewer.getExpandedTreePaths();
		        viewer.refresh();
		        viewer.setExpandedTreePaths(treePaths);
		        }
		});
      /*  TreeViewer viewer = (TreeViewer) _viewer;
     
        TreePath[] treePaths = viewer.getExpandedTreePaths();
        viewer.refresh();
        viewer.setExpandedTreePaths(treePaths); */
    }
    
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    _viewer = viewer;
	    
	    _viewer.refresh();
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
    
    @Override
    public Object[] getChildren(Object parentElement) {
    	if (parentElement instanceof IWorkspaceRoot) {
			return ((IWorkspaceRoot) parentElement).getProjects();
		} else if ( parentElement instanceof IProject) {
			try {
				IProject prj = (IProject)parentElement;
				List<IResource> resources = new ArrayList<IResource>();
				if (!prj.isOpen())
					return resources.toArray();
				for (IResource res : prj.members()) {
					if (res.getName().equals("bin") || res.getName().equals("src-gen") 
							|| res.getName().equals("META-INF") || res.getName().equals(".settings")) {
						continue;
					} 
					if (res instanceof IFile) {
						continue;
					}
					resources.add(res);
				}
				return resources.toArray();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (parentElement instanceof IFolder) {
			try {
				return ((IFolder)parentElement).members();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (parentElement instanceof IFile) {
			if (((IFile) parentElement).getFileExtension().equals(PTSpecConstants.FILE_EXTENSION)) {
				IFile file = (IFile) parentElement;
				URI codeInstanceURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				
				PTSRoot ptsRoot;
				try {
					ptsRoot = (PTSRoot)EcoreUtils.loadFullEMFModel(codeInstanceURI);
					if (ptsRoot != null && ptsRoot.getContainerDeclarations().size() > 0) {
						return ptsRoot.getContainerDeclarations().toArray();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new Object[0];
 
    }
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object getParent(Object element) {
	    Object parent = null;
	 
	    if (IProject.class.isInstance(element)) {
	        parent = ((IProject)element).getWorkspace().getRoot();
	    } else if (IFolder.class.isInstance(element)) {
	        parent = ((IFolder)element).getParent();
	    } else if (IFile.class.isInstance(element)) {
	    	parent = ((IFile)element).getParent();
	    } // else parent = null if IWorkspaceRoot or anything else
	 
	    return parent;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}

}
