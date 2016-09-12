package fzi.mottem.runtime.reportgenerator;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.osgi.framework.Bundle;

import fzi.mottem.jjet.JJETCompiler;
import fzi.mottem.runtime.TraceDB;
import fzi.mottem.runtime.interfaces.ITest;
import fzi.mottem.runtime.interfaces.ITestSuite;
import fzi.mottem.runtime.reportgenerator.precompiled.JET_TestReport;
import fzi.mottem.runtime.reportgenerator.precompiled.JET_TestSuiteReport;

public class ReportGenerator
{
	
	private final ITest _test;

	public ReportGenerator(ITest test)
	{
		_test = test;
	}
	
	public void generate()
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(_test.getProject());
		IFolder outFolder = project.getFolder(new Path("reports"));

		if (!outFolder.exists()) { try {
			outFolder.create(IResource.NONE, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		} }
		
		IFolder imgFolder = project.getFolder(new Path("reports/img"));
		
		if (!imgFolder.exists()) {
			try {
				imgFolder.create(IResource.NONE, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		Bundle bundle = Platform.getBundle("fzi.mottem.runtime");
        IPath yes_path = new Path("res/yes.png");
        IPath warn_path = new Path("res/warn.png");
        IPath no_path = new Path("res/no.png");
        URL yes_url =FileLocator.find(bundle,yes_path, null);
        URL warn_url =  FileLocator.find(bundle, warn_path, null);
        URL no_url = FileLocator.find(bundle, no_path, null);
        
        ImageDescriptor yes_desc = ImageDescriptor.createFromURL(yes_url);
        ImageDescriptor warn_desc = ImageDescriptor.createFromURL(warn_url);
        ImageDescriptor no_desc = ImageDescriptor.createFromURL(no_url);

        Image yes = yes_desc.createImage(true);
        Image warn = warn_desc.createImage(true);
        Image no = no_desc.createImage(true);
        
        IFile yesFile = imgFolder.getFile("yes.png");
        IFile warnFile = imgFolder.getFile("warn.png");
        IFile noFile = imgFolder.getFile("no.png");
		if (! yesFile.exists()) {
			final ImageLoader imageLoader = new ImageLoader();
	        imageLoader.data = new ImageData[] { yes.getImageData() };
	        imageLoader.save(yesFile.getLocation().toOSString(), SWT.IMAGE_PNG); 
		}
		
		if (! warnFile.exists()) {
			final ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] {warn.getImageData() };
			imageLoader.save(warnFile.getLocation().toOSString(), SWT.IMAGE_PNG);
		}
		
		if( !noFile.exists()) {
			final ImageLoader imageLoader = new ImageLoader();
	        imageLoader.data = new ImageData[] { no.getImageData() };
	        imageLoader.save(noFile.getLocation().toOSString(), SWT.IMAGE_PNG); 
		}
        
        
        
        
		JJETCompiler compiler = new JJETCompiler(outFolder);
		
		if (_test instanceof ITestSuite) {
			ITestSuite suite = (ITestSuite) _test;
			
			for (ITest test : suite.getAllTests()) {
				ReportGenerator rg = new ReportGenerator(test);
				rg.generate();
			}
			
			compiler.compile(new JET_TestSuiteReport(), suite, null);
		} else {
			String path = yesFile.getLocation().toOSString();
			path = path.substring(0, path.lastIndexOf('\\')+1);
			for (TraceDB tracedb: _test.getTestPlots()) {
				
				System.out.println("SAVE ALL OTHER PICS HERE: " + path);
				tracedb.generateImage(path);
			}
			compiler.compile(new JET_TestReport(), _test, null);
		}
	}
	
}
