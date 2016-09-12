package wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The Class CreateNewTestRigWizardDiagramPage creates the wizard page.
 */
public class CreateNewTestRigWizardDiagramPage extends WizardPage {
	
	/** The Constant WIZARD_DESCRIPTION. */
	private static final String WIZARD_DESCRIPTION = "Enter diagram name";
	
	/** The Constant WIZARD_TITLE. */
	private static final String WIZARD_TITLE = "Testrig Diagram";
	
	/** The diagram name. */
	private Text diagramName;

	
	/**
	 * Instantiates a new creates the new test rig wizard diagram page.
	 */
	public CreateNewTestRigWizardDiagramPage() {
		super("wizardPage");
        setTitle(WIZARD_TITLE);
        setDescription(WIZARD_DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setPageComplete(false);

        setControl(container);
        container.setLayout(new GridLayout(3, false));
        
        Label lableDiagramName = new Label(container, SWT.NONE);
        lableDiagramName.setText("Diagram Name");

        diagramName = new Text(container, SWT.BORDER);
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gridData.widthHint = 300;
        diagramName.setLayoutData(gridData);
        diagramName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String name = diagramName.getText();
				setPageComplete(name.matches("^[a-zA-Z0-9]+$") ? true : false);
				
			}
		});

        diagramName.setFocus();
		
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return diagramName.getText();
	}

}
