package fzi.mottem.runtime.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import fzi.mottem.ptspec.compiler.PTSCompilerPlugin;
import fzi.mottem.ptspec.compiler.PTSCompilerSettings;
import fzi.mottem.runtime.interfaces.ITest;
import fzi.util.FileUtils;
import fzi.util.eclipse.IntegrationUtils;

public class PTSpecReflector
{
	
	private final URLClassLoader _urlClassLoader;
	private final LinkedList<IPath> _outputDirs = new LinkedList<IPath>();
	
	public PTSpecReflector(IProject project) throws JavaModelException, MalformedURLException
	{
		IWorkspace workspace = project.getWorkspace();
		IPath baseDir = workspace.getRoot().getLocation();
		IJavaProject javaProject = JavaCore.create(project);
		
		// add default output location for java project
		_outputDirs.add(baseDir.append(javaProject.getOutputLocation()));
		
		
		// add additional output locations specified in classpath entries (if any)
		for (IClasspathEntry cpe : javaProject.getRawClasspath())
		{
			if (cpe.getOutputLocation() != null)
				_outputDirs.add(baseDir.append(cpe.getOutputLocation()));
		}
		

		// build array with URLs needed for URLClassLoader
		URL[] classURLsArray = new URL[_outputDirs.size()];
		int idx = 0;
		for (IPath path : _outputDirs)
		{
			URL defaultOutputURL = path.toFile().toURI().toURL();
			classURLsArray[idx] = defaultOutputURL;
			idx++;
		}
		
		_urlClassLoader = new URLClassLoader(classURLsArray, Thread.currentThread().getContextClassLoader());
		return;
	}
	
	public List<Class<ITest>> getTestClasses()
	{
		LinkedList<Class<ITest>> testClasses = new LinkedList<Class<ITest>>();
		
		PTSCompilerSettings settings = PTSCompilerPlugin.Instance.getSettings();
		String packageName = settings.getOutputPackage() + "." + settings.getTestSubPackage();

		IPath testPackageDir = IntegrationUtils.packageNameToPath(packageName);

		for (IPath outDir : _outputDirs)
		{
			IPath fullPackageDir = outDir.append(testPackageDir);
			File dir = fullPackageDir.toFile();
			if (dir.exists() && dir.isDirectory())
			{
				File[] classFiles = dir.listFiles();
				for (File classFile : classFiles)
				{
					String binaryClassName = packageName + "." + FileUtils.getNameWithoutExtension(classFile);
					Class<ITest> testClass = tryLoadClass(binaryClassName, ITest.class);
					if (testClass != null)
					{
						testClasses.add(testClass);
					}
				}
			}
		}
		
		return testClasses;
	}
	
	public Class<ITest> getTestClass(String className)
	{
		Class<ITest> testClass = tryLoadClass(className, ITest.class);
		return testClass;
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> tryLoadClass(String binaryClassName, Class<T> baseType)
	{
		try
		{
			Class<?> testClass = _urlClassLoader.loadClass(binaryClassName);
			
			if (!baseType.isAssignableFrom(testClass))
				return null;
			
			return (Class<T>)testClass;
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("class not found");
			return null;
		}
	}

}
