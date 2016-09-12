package util;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class NumberOfCoresDialog will be called at a creation of a Processor 
 * to ask for the number of ProcessorCores.
 */
public class NumberOfCoresDialog extends TitleAreaDialog {

  /** The number. */
  private Text number;
  
  /** The number of cores. */
  private String numberOfCores;

  /**
   * Instantiates a new number of cores dialog.
   *
   * @param parentShell the parent shell
   */
  public NumberOfCoresDialog(Shell parentShell) {
    super(parentShell);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#create()
   */
  @Override
  public void create() {
    super.create();
    setTitle("How many processor cores?");
    setMessage("The processor consists of how many processor cores?", IMessageProvider.INFORMATION);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite dialogArea = (Composite) super.createDialogArea(parent);
    Composite composite = new Composite(dialogArea, SWT.NONE);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout layout = new GridLayout(2, false);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    composite.setLayout(layout);
    createFirstName(composite);
    
    return dialogArea;
  }

  /**
   * Creates the first name.
   *
   * @param container the container
   */
  private void createFirstName(Composite container) {
    Label labelFirstName = new Label(container, SWT.NONE);
    labelFirstName.setText("Number of processor cores");

    GridData dataFirstName = new GridData();
    dataFirstName.grabExcessHorizontalSpace = true;
    dataFirstName.horizontalAlignment = GridData.FILL;

    number = new Text(container, SWT.BORDER);
    number.setLayoutData(dataFirstName);
  }
  



  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#isResizable()
   */
  @Override
  protected boolean isResizable() {
    return true;
  }

  // save content of the Text fields because they get disposed
  // as soon as the Dialog closes
  /**
   * Save input.
   */
  private void saveInput() {
    numberOfCores = number.getText();

  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  /**
   * Gets the number.
   *
   * @return the number
   */
  public String getNumber() {
    return numberOfCores;
  }

} 