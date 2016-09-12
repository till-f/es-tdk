package fzi.mottem.runtime.rtgraph.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.rtgraph.commands.CreateDBOptions;

public class DBMessageDialog extends TitleAreaDialog {

	private Text txtTitleField;
	private List signals;
	private Label labelRegister;
	private Button btnRegister;
	
	private Label labelApplyMeta;
	private Button btnApplyMeta;

	private String graphTitle;
	private String lastName;
	private CreateDBOptions options;

	public DBMessageDialog(Shell parentShell, CreateDBOptions options) {
		super(parentShell);
		this.options = options;
	}

	@Override
	public void create() {
		super.create();
		setTitle("DB plotting options");
		setMessage("Select signal UIDs to plot", IMessageProvider.INFORMATION);
		txtTitleField.setText(options.graphName);
		
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createTitleField(container);
		// createLastName(container);
		createSignalsList(container);
		
		labelRegister = new Label(container, SWT.NONE);
		labelRegister.setText("Connect Traces to Signals for real-time plotting?");
		btnRegister = new Button(container, SWT.CHECK);
		
		labelApplyMeta = new Label(container, SWT.NONE);
		labelApplyMeta.setText("Apply DB metadata to the Graph View?");
		btnApplyMeta = new Button(container, SWT.CHECK);
		btnApplyMeta.setSelection(true);
		
		return area;
	}

	private void createTitleField(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("GraphView Title");

		GridData dataTitleField = new GridData();
		dataTitleField.grabExcessHorizontalSpace = true;
		dataTitleField.horizontalAlignment = GridData.FILL;

		txtTitleField = new Text(container, SWT.BORDER);
		txtTitleField.setLayoutData(dataTitleField);
	}


	private void createSignalsList(Composite container) {
		signals = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData dataSignalsList = new GridData();
		dataSignalsList.grabExcessHorizontalSpace = true;
		dataSignalsList.horizontalAlignment = GridData.FILL;
		dataSignalsList.horizontalSpan = 2;
		signals.setLayoutData(dataSignalsList);
	}
	
	public void setListItems(String[] items) {
		signals.setItems(items);
		signals.selectAll();
	}
	
	public String[] getListItems() {
		return signals.getItems();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		graphTitle = txtTitleField.getText();
		// lastName = lastNameText.getText();

	}

	@Override
	protected void okPressed() {
		saveInput();
		options.uids.clear(); 
		for(String s : signals.getSelection()) {
			options.uids.add(s);
		}
		if(txtTitleField.getText().length() > 0) {
			options.graphName = txtTitleField.getText();
		}
		options.connectToDataExchanger = btnRegister.getSelection();
		options.applyMetaData = btnApplyMeta.getSelection();
	
		
		setReturnCode(OK);
		close();
	}
	
	
	
	public void setGraphTitle(String title) {
		txtTitleField.setText(title);
		graphTitle = title;
	}

	public String getGraphTitle() {
		return graphTitle;
	}

	public String getLastName() {
		return lastName;
	}
}
