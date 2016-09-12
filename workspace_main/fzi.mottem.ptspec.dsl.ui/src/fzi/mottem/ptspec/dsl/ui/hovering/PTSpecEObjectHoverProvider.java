package fzi.mottem.ptspec.dsl.ui.hovering;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;
import org.osgi.framework.Bundle;

import fzi.mottem.model.ModelPlugin;

import fzi.mottem.ptspec.compiler.util.PTS2JavaUtil;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSRoot;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestSuiteDeclaration;
import fzi.mottem.ptspec.dsl.ui.PTSpecUiModule;

import fzi.util.ecore.EcoreUtils;

public class PTSpecEObjectHoverProvider extends DefaultEObjectHoverProvider
{
	
	@Override
	protected boolean hasHover(EObject eObj)
	{
		return true;
	}

    @Override
    protected String getFirstLine(EObject eObj)
    {
    	String label = PTSpecUtils.getDisplayName(eObj.eClass()) + " <b>" + getLabel(eObj) + "</b>";
    	
    	String iconName = getIconName(eObj);
    	
    	String iconPrefix = "";
    	if (iconName != null)
    	{
    		Bundle pluginBundle;
    		IPath iconPath = new Path("icons");
    		
    		if (EcoreUtils.hasContainerInstanceOf(eObj, PTSRoot.class))
    		{
        		pluginBundle = PTSpecUiModule.PLUGIN_BUNDLE;
        		iconPath = iconPath.append(iconName);
    		}
    		else
    		{
    			pluginBundle = ModelPlugin.PLUGIN_BUNDLE;
        		iconPath = iconPath.append("full");
        		iconPath = iconPath.append("obj16");
        		iconPath = iconPath.append(iconName);
    		}
        	
    		URL iconURL = FileLocator.find(pluginBundle, iconPath, null);
    		
    		try
    		{
    			iconPrefix = "<img src=\"" + FileLocator.resolve(iconURL) + "\"/> ";
    		}
    		catch (IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	
    	String extraText = "";
    	if (eObj instanceof PTSTestDeclaration)
    	{
    		String testClassName = PTS2JavaUtil.getJavaFullQualifiedClassName((PTSTestDeclaration)eObj);
    		IProject project = EcoreUtils.getFileForEMFURI2(eObj.eResource().getURI()).getProject();
    		extraText = "<p><a href=\"" + PTSpecElementLinks.PTSPEC_SCHEME + "://" + 
	    		PTSpecElementLinks.URL_CMD_RUNTEST + "/" + 
	    		project.getName() + ":" + 
	    		testClassName +
	    		"\">Run Test</a></p>";
    	}
    	else if (eObj instanceof PTSTestSuiteDeclaration)
    	{
    		// this is duplicate code of the case above (currently suits are started exactly the same way as tests) 
    		String testClassName = PTS2JavaUtil.getJavaFullQualifiedClassName((PTSTestSuiteDeclaration)eObj);
    		IProject project = EcoreUtils.getFileForEMFURI2(eObj.eResource().getURI()).getProject();
    		extraText = "<p><a href=\"" + PTSpecElementLinks.PTSPEC_SCHEME + "://" + 
	    		PTSpecElementLinks.URL_CMD_RUNTEST + "/" + 
	    		project.getName() + ":" + 
	    		testClassName +
	    		"\">Run Suite</a></p>";
    	}
		
		
		return iconPrefix + label + extraText;
    }
    
    @Override
	protected String getLabel(EObject eObj)
    {
    	String label = super.getLabel(eObj);
    	if (label != null)
    		return label;
    	else
    		return "";
	}
    
	protected String getIconName(EObject eObj)
	{
		return PTSpecUtils.getImageName(eObj.eClass());
	}

}
