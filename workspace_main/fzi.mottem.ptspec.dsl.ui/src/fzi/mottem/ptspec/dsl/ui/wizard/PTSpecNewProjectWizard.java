package fzi.mottem.ptspec.dsl.ui.wizard;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import org.eclipse.xtext.ui.wizard.IProjectInfo;

import com.google.inject.Inject;

public class PTSpecNewProjectWizard extends org.eclipse.xtext.ui.wizard.XtextNewProjectWizard
{

	private WizardNewProjectCreationPage mainPage;

	@Inject
	public PTSpecNewProjectWizard(IProjectCreator projectCreator) {
		super(projectCreator);
		setWindowTitle("New PTSpec Project");
	}

	/**
	 * Use this method to add pages to the wizard.
	 * The one-time generated version of this class will add a default new project page to the wizard.
	 */
	public void addPages() {
		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");
		mainPage.setTitle("ETSpec Project");
		mainPage.setDescription("Create a new PTSpec project.");
		addPage(mainPage);
	}

	/**
	 * Use this method to read the project settings from the wizard pages and feed them into the project info class.
	 */
	@Override
	protected IProjectInfo getProjectInfo() {
		fzi.mottem.ptspec.dsl.ui.wizard.PTSpecProjectInfo projectInfo = new fzi.mottem.ptspec.dsl.ui.wizard.PTSpecProjectInfo();
		projectInfo.setProjectName(mainPage.getProjectName());
		return projectInfo;
	}

}
