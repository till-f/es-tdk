package property.sheet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.util.eclipse.IntegrationUtils;

public class ProcessorCorePropertySheet extends SoftwareExecutorPropertySheet
{
	/** The name widget. */
	private Text binaryFileWidget;
	private Text symbolInfoFileWidget;
	private Button changeBinaryFileButton;
	private Button changeSymbolInfoFileButton;

	
    private ModifyListener binaryFileListener = new ModifyListener()
    {
        public void modifyText(ModifyEvent arg0)
        {
            TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
            editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain)
	            {
	                @Override
	                protected void doExecute()
	                {
	                	changeBinaryFilePropertyValue();
	                }
	            });
        }
    };

    private ModifyListener symbolInfoFileListener = new ModifyListener()
    {
        public void modifyText(ModifyEvent arg0)
        {
            TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
            editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain)
	            {
	                @Override
	                protected void doExecute()
	                {
	                	changesymbolInfoFilePropertyValue();
	                }
	            });
        }
    };

    private void changeBinaryFilePropertyValue()
    {
    	if(binaryFileWidget.getText()== "")
    	{
    		return;
    	}
    	
    	CodeInstance ci = (CodeInstance) softwareExecutor.getSymbolContainer();
    	
    	if (ci == null)
    	{
    		return;
    	}
    	
    	String newValue = binaryFileWidget.getText();
    	if (!newValue.equals(ci.getBinaryFile())) 
    	{
    		ci.setBinaryFile(newValue);
    		
			// do something to refresh/call the method getToolTip of EditorToolBehaviorProvider
			PictogramElement pe = getSelectedPictogramElement();
			pe.setActive(false);
			pe.setActive(true);
    	}
    }
	
	private void changesymbolInfoFilePropertyValue()
    {
    	if(symbolInfoFileWidget.getText()== "")
    	{
    		return;
    	}
    	
    	CodeInstance ci = (CodeInstance) softwareExecutor.getSymbolContainer();
    	
    	if (ci == null)
    	{
    		return;
    	}
    	
    	String newValue = symbolInfoFileWidget.getText();
    	if (!newValue.equals(ci.getSymbolInfoFile())) 
    	{
    		ci.setSymbolInfoFile(newValue);
    		
			// do something to refresh/call the method getToolTip of EditorToolBehaviorProvider
			PictogramElement pe = getSelectedPictogramElement();
			pe.setActive(false);
			pe.setActive(true);
    	}
    }
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage)
	{
		super.createControls(parent, tabbedPropertySheetPage);

		// --------------------------------------------------------
		// Binary File
		// --------------------------------------------------------

		getWidgetFactory().createLabel(parent, "Binary File: ");
		binaryFileWidget = getWidgetFactory().createText(parent, "");
		binaryFileWidget.addModifyListener(binaryFileListener);
		binaryFileWidget.setSize(500, 25);
		
		getWidgetFactory().createLabel(parent, "");
		changeBinaryFileButton = getWidgetFactory().createButton(parent, "Select Binary File", SWT.PUSH);
		changeBinaryFileButton.addSelectionListener(new SelectionListener()
			{
				
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					final PictogramElement pe = getSelectedPictogramElement();
					
					if (pe != null)
					{
						FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
						dialog.setText("Select Binary File");
						
						dialog.setFilterPath(Platform.getLocation().toOSString());
						String [] extensions = {"*.elf", "*.axf", "*.*"};
						dialog.setFilterExtensions(extensions);

						final String binFileStr = dialog.open();
						if (binFileStr == null)
							return;

						final IPath binFile = IntegrationUtils.getWorkspaceRelativePathForSystemPath(new Path(binFileStr));
						final EObject procCore = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
						if(procCore == null || !(procCore instanceof ProcessorCore))
						{
							return;
						}
						
						final CodeInstance ci = (CodeInstance)((ProcessorCore) procCore).getSymbolContainer();
						if (ci == null)
						{
							return;
						}
						
			            TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
			            editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain)
				            {
				                @Override
				                protected void doExecute()
				                {
				                	ci.setBinaryFile(binFile.toString());
				                	refresh();
				                }
				            });
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e)
				{
					// TODO Auto-generated method stub
				}
			});
		
		
		// --------------------------------------------------------
		// Symbol Info File
		// --------------------------------------------------------

		getWidgetFactory().createLabel(parent, "Symbol File: ");
		symbolInfoFileWidget = getWidgetFactory().createText(parent, "");
		symbolInfoFileWidget.addModifyListener(symbolInfoFileListener);
		symbolInfoFileWidget.setSize(500, 25);
		
		getWidgetFactory().createLabel(parent, "");
		changeSymbolInfoFileButton = getWidgetFactory().createButton(parent, "Select Symbol File", SWT.PUSH);
		changeSymbolInfoFileButton.addSelectionListener(new SelectionListener()
			{
				
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					final PictogramElement pe = getSelectedPictogramElement();
					
					if (pe != null)
					{
						FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
						dialog.setText("Select Symbol File");
						
						dialog.setFilterPath(Platform.getLocation().toOSString());
						String [] extensions = {"*.sym", "*.*"};
						dialog.setFilterExtensions(extensions);

						final String symFileStr = dialog.open();
						if (symFileStr == null)
							return;

						final IPath symInfoFile = IntegrationUtils.getWorkspaceRelativePathForSystemPath(new Path(symFileStr));
						final EObject procCore = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
						if(procCore == null || !(procCore instanceof ProcessorCore))
						{
							return;
						}
						
						final CodeInstance ci = (CodeInstance)((ProcessorCore) procCore).getSymbolContainer();
						if (ci == null)
						{
							return;
						}
						
			            TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
			            editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain)
				            {
				                @Override
				                protected void doExecute()
				                {
				                	ci.setSymbolInfoFile(symInfoFile.toString());
				                	refresh();
				                }
				            });
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e)
				{
					// TODO Auto-generated method stub
				}
			});
		
		
		
		

		
		refresh();
	}
	
	@Override
	public void refresh()
	{
		super.refresh();

		if (
			softwareExecutor == null ||
		    binaryFileWidget == null ||
			symbolInfoFileWidget == null
		   )
		{
			return;
		}
		
		try
		{
			binaryFileWidget.removeModifyListener(binaryFileListener);
			symbolInfoFileWidget.removeModifyListener(symbolInfoFileListener);

	    	CodeInstance ci = (CodeInstance) softwareExecutor.getSymbolContainer();
	    	if (ci != null)
	    	{
	        	binaryFileWidget.setText(ci.getBinaryFile() == null ? "" : ci.getBinaryFile());
	    		binaryFileWidget.setSize(500, 25);

	    		symbolInfoFileWidget.setText(ci.getSymbolInfoFile() == null ? "" : ci.getSymbolInfoFile());
	    		symbolInfoFileWidget.setSize(500, 25);
	    	}
		}
		finally
		{
        	binaryFileWidget.addModifyListener(binaryFileListener);
			symbolInfoFileWidget.addModifyListener(symbolInfoFileListener);
		}
	}

}
