package fzi.mottem.cdt2ecore.util;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class PTSpecResourceChangeVisitor implements IResourceDeltaVisitor
{

	HashSet<IProject> _changedCProjects = new HashSet<IProject>();
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException
	{
		IProject project = delta.getResource().getProject();
		
		if (project == null)
			return true;
		
		if (!project.isOpen())
			return false;
		
		if (_changedCProjects.contains(project))
			return false;
		
		if (CoreModel.hasCNature(project) || CoreModel.hasCCNature(project))
		{
			switch (delta.getKind())
			{
				case IResourceDelta.CHANGED:
				case IResourceDelta.ADDED:
					_changedCProjects.add(project);
					return true;
				default:
					return false;
			}
		}

		return false;
	}

	public Collection<IProject> getChangedCProjects()
	{
		return _changedCProjects;
	}

}
