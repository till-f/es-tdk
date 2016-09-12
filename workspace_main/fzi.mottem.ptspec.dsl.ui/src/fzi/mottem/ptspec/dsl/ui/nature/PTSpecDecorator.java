package fzi.mottem.ptspec.dsl.ui.nature;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import fzi.mottem.ptspec.dsl.ui.PTSpecUiModule;

public class PTSpecDecorator implements ILightweightLabelDecorator
{
	
	private final IPath ICON_PTS_PROJECT = new Path("icons/pts_project_overlay.gif");

	@Override
	public void addListener(ILabelProviderListener listener)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void removeListener(ILabelProviderListener listener)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLabelProperty(Object element, String property)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void decorate(Object element, IDecoration decoration)
	{
		if (element instanceof IProject)
		{
			IProject project = (IProject) element;
			
			if (!project.isOpen())
				return;
			
			try
			{
				if (project.hasNature(PTSpecNature.NATURE_ID))
				{
		    		URL iconURL = FileLocator.find(PTSpecUiModule.PLUGIN_BUNDLE, ICON_PTS_PROJECT, null);
					decoration.addOverlay(ImageDescriptor.createFromURL(iconURL), IDecoration.TOP_RIGHT);
				}
			}
			catch (CoreException e)
			{
				// ignore silently
				// e.printStackTrace();
			}
		}
	}

}
