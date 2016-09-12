package fzi.mottem.ptspec.compiler.util;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import fzi.mottem.ptspec.dsl.common.PTSpecConstants;

@Deprecated
public class PTSResourceChangeVisitor implements IResourceDeltaVisitor
{
	private final List<IResource> _resourcesToCompile;

	public PTSResourceChangeVisitor(List<IResource> resourcesToCompile)
	{
		_resourcesToCompile = resourcesToCompile;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException
	{
		IResource res = delta.getResource();
		
		switch (delta.getKind())
		{
			case IResourceDelta.ADDED:
				handleResourceChange(res);
				return true;
			case IResourceDelta.CHANGED:
				handleResourceChange(res);
				return true;
			case IResourceDelta.REMOVED:
				return false;
		}
		
		return true;
    }
	
	private void handleResourceChange(IResource resource)
	{
		if (resource instanceof IFile &&
			PTSpecConstants.FILE_EXTENSION.equals(resource.getFileExtension()))
		{
			_resourcesToCompile.add(resource);
		}
	}

}
