package fzi.mottem.runtime.rtgraph;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

/**
 * This class provides an interface between the eclipse file system and the OS
 * file system
 * 
 * @author K Katev
 *
 */
public class EclipseFileSystemHelper {

	IWorkspaceRoot root;
	String mona_lisa2 = "C:\\Work\\FZI\\git\\daves\\workspace_ptspec\\images\\Mona_Lisa2.jpg";

	public EclipseFileSystemHelper() {
		root = ResourcesPlugin.getWorkspace().getRoot();
		/*
		System.out.println("Try to find file in workspace: " + getWorkspaceRelativeFromAbsolute(mona_lisa2));
		System.out.println("Try to find file in OS: " + getAbsoluteFromWorkspaceRelative(mona_lisa2));
		
		
		System.out.println("Try to find file in workspace: " + getWorkspaceRelativeFromAbsolute("C:\\Work\\Mona_Lisa2.jpg"));
		System.out.println("Try to find file in OS: " + getAbsoluteFromWorkspaceRelative("C:\\Work\\Mona_Lisa2.jpg"));
		
		
		System.out.println("Try to find file in workspace: " + getWorkspaceRelativeFromAbsolute("images/Mona_Lisa2.jpg"));
		System.out.println("Try to find file in OS: " + getAbsoluteFromWorkspaceRelative("images/Mona_Lisa2.jpg"));
		*/	
	}

	public boolean fileIsInWorkspace(Path p) {
		boolean fileExists = false;

		System.out.println("-----------------check file " + p.toOSString());

		URI combined;
		if (p.isAbsolute()) {
			File fstr = new File(p.toOSString());
			URI uripath = fstr.toURI();

			System.out.println("Path is absolute, " + uripath.toString());
			
			// try to get the relative path to the workspace root
			combined = root.getLocationURI().relativize(uripath);
			File fcombi = new File(root.getLocation().toOSString() + "/" + combined.toString());
			if (fcombi.exists()) {
				System.out.println("File is found in workspace: " + fcombi.getAbsolutePath());
				fileExists = true;
			}
		} else {

			File fcombi = new File(root.getLocation().toOSString() + "/" + p.toOSString());
			
			
			if (fcombi.exists()) {
				System.out.println("File is found in workspace: " + fcombi.getAbsolutePath());
				fileExists = true;
			}
		}
		return fileExists;
	}
	
	public boolean isAbsolutePath(String path) {
		Path p = new Path(path);
		return p.isAbsolute();
	}
	
	public boolean fileIsInWorkspace(String path) {
		Path p = new Path(path);
		return fileIsInWorkspace(p);
	}
	
	public String getWorkspaceRelativeFromAbsolute(String absolutePath) {
		String p = null;
		if(fileIsInWorkspace(absolutePath)) {
			
			File fstr = new File(absolutePath);
			URI uripath = fstr.toURI();
			URI combined = root.getLocationURI().relativize(uripath);
			File fcombi = new File(root.getLocation().toOSString() + "/" + combined.toString());
			
			if (fcombi.exists()) {
				//URI combined = root.getLocationURI().relativize(fcombi.toURI());
				p = combined.toString();
			}
		}
		return p;
	}
	
	public String getAbsoluteFromWorkspaceRelative(String relativePath) {
		String p = null;
		
		File fcombi = new File(root.getLocation().toOSString() + "/" + relativePath);
		if (fcombi.exists()) {
			p = fcombi.getAbsolutePath();
		}
		return p;
	}
	
	

	

}
