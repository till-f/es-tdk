package wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

/**
 * The Class TestRigDiagramWizard creates the new wizard.
 * The files for the diagram data and the model will be created.
 */
public class TestRigDiagramWizard extends Wizard implements INewWizard {

	/** The Constant GRID_UNIT. */
	private static final int GRID_UNIT = 10;

	/** The selection. */
	private ISelection selection;
	
	/** The editor id. */
	private String editorId = "editor";
	
	/** The workspace. */
	private IWorkspace workspace;
	
	/** The new testrig wizard diagram page. */
	private CreateNewTestRigWizardDiagramPage newTestRigWizardDiagramPage;
	
	/** The workbench. */
	private IWorkbench  workbench;
	
	/** The diagram file extension. */
	private final String diagramFileExtension = ".etm-testrig.diagram";
	
	/** The model file extension. */
	private final String modelFileExtension = ".etm-testrig";

	/**
	 * Instantiates a new test rig diagram wizard.
	 */
	public TestRigDiagramWizard() {
		newTestRigWizardDiagramPage = new CreateNewTestRigWizardDiagramPage();
		workspace = ResourcesPlugin.getWorkspace();
//		editorId = "editor";
		workbench = PlatformUI.getWorkbench();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection; 
        setNeedsProgressMonitor(true);
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();	 
		addPage(newTestRigWizardDiagramPage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final String fileName = newTestRigWizardDiagramPage.getFileName();
		final URI diagramUri = URI.createPlatformResourceURI(getCurrentProjectPath() + System.getProperty("file.separator") + fileName + diagramFileExtension, true);
		final URI modelUri = URI.createPlatformResourceURI(getCurrentProjectPath() + System.getProperty("file.separator") + fileName + modelFileExtension, true);
		
		// create diagram and open it
		IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

			@Override
			protected void execute(final IProgressMonitor monitor) throws CoreException,
			InterruptedException {
				Resource diagramResource = createDiagram(diagramUri, fileName,
						modelUri, monitor);
				if (diagramResource != null && editorId != null) {
					try {
						openDiagram(diagramResource);
					} catch (PartInitException exception) {
						exception.printStackTrace();
					}
				}
			}
		};
		
		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
			return false;
		} catch (InvocationTargetException exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Creates the diagram.
	 *
	 * @param diagramURI the diagram uri
	 * @param name the diagram name
	 * @param modelURI the model uri
	 * @param progressMonitor the process monitor
	 * @return the created diagram
	 */
	private Resource createDiagram(final URI diagramURI, final String name, final URI modelURI,
			final IProgressMonitor progressMonitor) {
		progressMonitor.beginTask("Creating diagram and model files", 2);
		TransactionalEditingDomain editingDomain = GraphitiUi.getEmfService()
				.createResourceSetAndEditingDomain();
		ResourceSet resourceSet = editingDomain.getResourceSet();
		CommandStack commandStack = editingDomain.getCommandStack();
		// create resources for the diagram and domain model files
		final Resource diagramResource = resourceSet.createResource(diagramURI);
		final Resource modelResource = resourceSet.createResource(modelURI);
		if (diagramResource != null && modelResource != null) {
			commandStack.execute(new RecordingCommand(editingDomain) {
				@Override
				protected void doExecute() {
					// create diagram and business models
					createModel(diagramResource, name, modelResource,
							modelURI.lastSegment());
				}
			});
		}
		progressMonitor.worked(1);
		try {
			//save models
			modelResource.save(createSaveOptions());
			diagramResource.save(createSaveOptions());
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		progressMonitor.done();
		return diagramResource;
	}

	/**
	 * Create save options.
	 * @return the save options
	 */
	public static Map<?, ?> createSaveOptions() {
		HashMap<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
				Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
		return saveOptions;
	}

/**
 * Opens the created diagram.
 *
 * @param diagramResource the diagram resource
 * @throws PartInitException if diagram could not be opened
 */
	private void openDiagram(final Resource diagramResource) throws PartInitException {
		IFile diagramFile = workspace.getRoot().getFile(new Path(diagramResource.getURI().toPlatformString(true)));
		selectAndReveal(diagramFile);
		openFileToEdit(getShell(), diagramFile);
	}

	/**
	 * Creates diagram and business models.
	 *
	 * @param diagramResource the diagram resource
	 * @param diagramName the diagram file name
	 * @param modelResource the model resource
	 * @param modelName the model name
	 */
	private void createModel(final Resource diagramResource, final String diagramName,
			final Resource modelResource, final String modelName) {
        // create model resource
		modelResource.setTrackingModification(true);
        TestRigInstance modelRootInstance = TestrigmodelFactory.eINSTANCE.createTestRigInstance();
        modelRootInstance.setName(diagramName);
        modelResource.getContents().add(modelRootInstance);
        
        //create diagram resource
        diagramResource.setTrackingModification(true);
        Diagram diagram = Graphiti.getPeCreateService().createDiagram(diagramName + hashCode(), diagramName,
                GRID_UNIT, true);
        diagram.setDiagramTypeId(editorId);
        
        //link model and diagram
        PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
        link.setPictogramElement(diagram);
        link.getBusinessObjects().add(modelRootInstance);
        diagramResource.getContents().add(diagram);
        
	}

	/**
	 * Returns the the path of the current selected project. This code was taken from NewDiagramPage class of spray.
	 * @return the path
	 */
	private String getCurrentProjectPath() {
		if (selection == null || selection.isEmpty() || !(selection instanceof IStructuredSelection))
			return null;
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() > 1)
			return null;
		Object obj = structuredSelection.getFirstElement();
		if (!(obj instanceof IAdaptable))
			return null;

		IResource resource = null;
		resource = (IResource) ((IAdaptable) obj).getAdapter(IResource.class);
		IContainer container;
		if (resource instanceof IContainer)
			container = (IContainer) resource;
		else
			container = ((IResource) resource).getParent();
		return container.getFullPath().toString();
	}
	
    /**
     * Select and reveal.
     *
     * @param file the file
     */
    public void selectAndReveal(IFile file) {
        BasicNewResourceWizard.selectAndReveal(file, workbench.getActiveWorkbenchWindow());
    }

    /**
     * Open file to edit.
     *
     * @param shell the parent shell. May not be <code>null</code>
     * @param file that should be selected. May not be <code>null</code>.
     * @throws PartInitException the part init exception
     */
    public void openFileToEdit(final Shell shell, final IFile file) throws PartInitException {
        shell.getDisplay().asyncExec(new Runnable() {
            public void run() {
                final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
                try {
                    IDE.openEditor(page, file, true);
                } catch (final PartInitException e) {
                	System.out.println("Throws exception");
                }
            }
        });
    }


}
