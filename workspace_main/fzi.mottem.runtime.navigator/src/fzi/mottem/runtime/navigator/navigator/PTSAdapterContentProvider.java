package fzi.mottem.runtime.navigator.navigator;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fzi.mottem.ptspec.dsl.common.PTSpecConstants;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.util.ecore.EcoreUtils;

public class PTSAdapterContentProvider extends AdapterFactoryContentProvider{
public PTSAdapterContentProvider() {
		super(ProjectAdapterFactoryProvider.getAdapterFactory());
		// TODO Auto-generated constructor stub
	}

	Viewer _viewer;
   
      /*  TreeViewer viewer = (TreeViewer) _viewer;
     
        TreePath[] treePaths = viewer.getExpandedTreePaths();
        viewer.refresh();
        viewer.setExpandedTreePaths(treePaths); */
    
    
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    _viewer = viewer;
	    
	    _viewer.refresh();
	}
    
    @Override
    public Object[] getChildren(Object parentElement) {
    	if (parentElement instanceof IFile) {
    		//System.out.println("ContentProvider2: INSTANCE OF IFile !");
    		String parentextension = ((IFile) parentElement).getFileExtension();
			if (parentextension != null && parentextension.equals(PTSpecConstants.FILE_EXTENSION)) {
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
		} else if (parentElement instanceof PTSTestDeclaration)
        {
			//System.out.println("ContentProvider: INSTANCE OF PTSTestDeclaration !");
			//return Activator.getImage("icons/pts_test.gif");
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
