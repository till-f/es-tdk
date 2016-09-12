package fzi.mottem.runtime.navigator.navigator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;

import fzi.mottem.runtime.navigator.commands.OpenCommand;

public class CustomNavigator extends CommonNavigator {
	
	private static CustomNavigator INSTANCE;
	
	public CustomNavigator() {
		INSTANCE = this;
	}

    @Override
    protected Object getInitialInput() {
    	this.getCommonViewer().refresh();
		return super.getInitialInput();
    }  
    
    @Override
    public void createPartControl(Composite aParent) {
    	super.createPartControl(aParent);
    	this.getCommonViewer().addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                OpenCommand.open();
            }
          });
    	
    }
    
    public static CustomNavigator get() {
    	if (INSTANCE == null) { INSTANCE = new CustomNavigator(); }
    	return INSTANCE;
    }
}
