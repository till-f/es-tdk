package fzi.mottem.runtime.navigator.navigator;

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.swt.graphics.Image;

import fzi.mottem.ptspec.dsl.pTSpec.PTSContainerDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSPackageDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTargetDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestDeclaration;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestSuiteDeclaration;
import fzi.mottem.runtime.navigator.Activator;

// ILabelProvider:
// DefaultLabelProvider
// DecoratingLabelProvider
// NavigatorDecoratingLabelProvider

// ICommonLabelProvider:
// ResourceExtensionLabelProvider
@SuppressWarnings("restriction")
public class LabelProvider extends DefaultLabelProvider
{
	
//	private final NavigatorDecoratingLabelProvider _innerProvider;
//	
//	public LabelProvider()
//	{
//		_innerProvider = new NavigatorDecoratingLabelProvider(null);
//	}
//
//	@Override
//	public Image getImage(Object element) {
//		return _innerProvider.getImage(element);
//	}
//
//	@Override
//	public String getText(Object element) {
//		return _innerProvider.getText(element);
//	}
//
//	@Override
//	public void addListener(ILabelProviderListener listener) {
//		 _innerProvider.addListener(listener);
//	}
//
//	@Override
//	public void dispose() {
//		 _innerProvider.dispose();
//	}
//
//	@Override
//	public boolean isLabelProperty(Object element, String property) {
//		return _innerProvider.isLabelProperty(element, property);
//	}
//
//	@Override
//	public void removeListener(ILabelProviderListener listener) {
//		_innerProvider.removeListener(listener);
//	}

  @Override
  public String getText(Object element)
	{
		if (element instanceof PTSContainerDeclaration)
		{	
			System.out.println("INSTANCE OF PTSContainerDeclaration ! " + ((PTSContainerDeclaration)element).getName());
			return ((PTSContainerDeclaration)element).getName();
		}
		
		return super.getText(element);
    }
	
	@Override
	public Image getImage(Object element)
	{
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
		}
	}

}
