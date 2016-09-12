package fzi.mottem.ptspec.dsl.ui.wizard;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtext.ui.util.PluginProjectFactory;

import com.google.common.collect.Lists;

public class PTSpecProjectFactory extends PluginProjectFactory
{
	protected List<String> nonJavaFolders;

	public PTSpecProjectFactory addNonJavaFolders(List<String> folders)
	{
		if (this.nonJavaFolders == null)
			this.nonJavaFolders = Lists.newArrayList();
		this.nonJavaFolders.addAll(folders);
		return this;
	}
	
	@Override
	protected void createFolders(IProject project, SubMonitor subMonitor, Shell shell) throws CoreException
	{
		super.createFolders(project, subMonitor, shell);
		
		if (nonJavaFolders != null)
		{
			for (final String folderName : nonJavaFolders)
			{
				final IFolder folder = project.getFolder(folderName);
				if (!folder.exists())
				{
					folder.create(false, true, subMonitor.newChild(1));
				}
			}
		}
	}
	
	@Override
	protected void createBuildProperties(IProject project, IProgressMonitor progressMonitor)
	{
		final StringBuilder content = new StringBuilder("source.. = ");
		
		for (final Iterator<String> iterator = folders.iterator(); iterator.hasNext();)
		{
			content.append(iterator.next()).append('/');
			if (iterator.hasNext()) {
				content.append(",\\\n");
				content.append("          ");
			}
		}
		
		content.append("\n");
		content.append("bin.includes = META-INF/,\\\n");
		content.append("               .");

		createFile("build.properties", project, content.toString(), progressMonitor);
	}
}

