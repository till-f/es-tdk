package fzi.mottem.runtime.rtgraph.settingsViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import fzi.mottem.runtime.rtgraph.SetupUnit;

public class SettingsViewPart extends ViewPart {
	
	@Override
	public void createPartControl(Composite parent) {
		
		parent.setLayout(new FillLayout());
		final ScrolledComposite sc1 = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		final Composite mainHolder = new Composite(sc1, SWT.NONE);
		sc1.setContent(mainHolder);
		sc1.setMinSize(800, 600);
		//TODO Remove listeners @ close
		sc1.setExpandHorizontal(true);
	    sc1.setExpandVertical(true);
		
		mainHolder.setSize(800, 800);
		
		SetupUnit.presentSetupUI(mainHolder);	
		SetupUI.setOpen(true);
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
