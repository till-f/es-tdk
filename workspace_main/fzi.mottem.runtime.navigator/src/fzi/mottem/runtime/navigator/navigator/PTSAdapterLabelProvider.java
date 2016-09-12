package fzi.mottem.runtime.navigator.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

import fzi.mottem.ptspec.dsl.ui.labeling.PTSpecLabelProvider;

public class PTSAdapterLabelProvider extends AdapterFactoryLabelProvider {
	
	PTSpecLabelProvider plp;
	
	
	  public PTSAdapterLabelProvider() {
		super(ProjectAdapterFactoryProvider.getAdapterFactory());
		plp   = new PTSpecLabelProvider(this);
	}

	  
	  
	 @Override
	public String getText(Object element)
		{
			/*if (element instanceof PTSContainerDeclaration)
			{	
				System.out.println("INSTANCE OF PTSContainerDeclaration ! " + ((PTSContainerDeclaration)element).getName());
				return ((PTSContainerDeclaration)element).getName();
			}
			*/
			 
			return plp.getText(element);
	    }
		
	 @Override	
		public Image getImage(Object element)
		{	
		 
		 	return plp.getImage(element);
		 	/*
	        if (element instanceof PTSTestDeclaration)
	        {	
	        	System.out.println("INSTANCE OF PTSTestDeclaration ! ");
				return Activator.getImage("icons/pts_test.gif");
			} 
	        else if (element instanceof PTSTargetDeclaration)
			{	
	        	System.out.println("INSTANCE OF PTSTargetDeclaration ! ");
				return Activator.getImage("icons/pts_target.gif");
			} 
			else if (element instanceof PTSPackageDeclaration)
			{
				System.out.println("INSTANCE OF PTSPackageDeclaration ! ");
				return Activator.getImage("icons/pts_test_package.gif");
			} 
			else if (element instanceof PTSTestSuiteDeclaration)
			{
				System.out.println("INSTANCE OF PTSTestSuiteDeclaration ! ");
				return Activator.getImage("icons/pts_test_suite.gif");
			}
			else
			{
				return super.getImage(element);
			}*/
		}

}
