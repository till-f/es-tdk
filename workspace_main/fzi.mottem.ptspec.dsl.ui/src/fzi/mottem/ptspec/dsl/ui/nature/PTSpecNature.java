package fzi.mottem.ptspec.dsl.ui.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class PTSpecNature implements IProjectNature
{
	
	public static final String NATURE_ID = "fzi.mottem.ptspec.dsl.ui.ptspecNature"; //$NON-NLS-1$
	
	private IProject _project = null;

	@Override
	public void configure() throws CoreException
	{
	}

	@Override
	public void deconfigure() throws CoreException
	{
	}

	@Override
	public IProject getProject()
	{
		return _project;
	}

	@Override
	public void setProject(IProject project)
	{
		_project = project;
	}

}
