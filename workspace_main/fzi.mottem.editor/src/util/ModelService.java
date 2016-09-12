package util;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

/**
 * This class gives access to the domain model root element for a diagram.
 * On first access, a new resource will be created to which the model
 * is added. Most parts of this class are use from the generated ModelService class of 
 * the framework Spray.
 */
public class ModelService {
    
    /** The Constant FILE_EXTENSION. */
    public static final String             FILE_EXTENSION = "etm-testrig";
    
    /** The pe service. */
    protected IPeService                   peService;
    
    /** The DiagramTypeProvider. */
    protected IDiagramTypeProvider         dtp;

    /** The model service. */
    static protected ModelService modelService   = null;

    /**
     * return the model service, create one if it does not exist yet.
     *
     * @param dtp the DiagramTypeProvider
     * @return the model service
     */
    static public ModelService getModelService(IDiagramTypeProvider dtp) {
        modelService = new ModelService(dtp);
        return modelService;
    }

    /**
     * return the model service.
     * returns null if there is no model service.
     *
     * @return the model service
     */
    static public ModelService getModelService() {
        return modelService;
    }

    /**
     * Instantiates a new model service.
     *
     * @param dtp the DiagramTypeProvider
     */
    protected ModelService(IDiagramTypeProvider dtp) {
        this.dtp = dtp;
        this.peService = Graphiti.getPeService();
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public TestRigInstance getModel() {
        final Diagram diagram = dtp.getDiagram();
        Resource resource = diagram.eResource();
        ResourceSet resourceSet = resource.getResourceSet();
        EObject businessObject = (EObject) dtp.getFeatureProvider().getBusinessObjectForPictogramElement(diagram);
        fzi.mottem.model.testrigmodel.TestRigInstance model = null;
        if (businessObject != null) {
            // If its a proxy, resolve it
            if (businessObject.eIsProxy()) {
                if (businessObject instanceof InternalEObject) {
                    model = (fzi.mottem.model.testrigmodel.TestRigInstance) resourceSet.getEObject(((InternalEObject) businessObject).eProxyURI(), true);
                }
            } else {
                if (businessObject instanceof fzi.mottem.model.testrigmodel.TestRigInstance) {
                    model = (fzi.mottem.model.testrigmodel.TestRigInstance) businessObject;
                }
            }
        }

        if (model == null) {
            model = createModel();
        }
        return model;
    }

    /**
     * Gets the business object.
     *
     * @param pe the pictogram element
     * @return the business object
     */
    public Object getBusinessObject(PictogramElement pe) {
        return dtp.getFeatureProvider().getBusinessObjectForPictogramElement(dtp.getDiagram());
    }

    /**
     * Creates the domain model element and store it in the file.
     *
     * @return the testrig instance
     */
    protected TestRigInstance createModel() {
        final Diagram diagram = dtp.getDiagram();
        try {
            TestRigInstance model = TestrigmodelFactory.eINSTANCE.createTestRigInstance();
            model.setName(diagram.getName());
            createModelResourceAndAddModel(model);
            peService.setPropertyValue(diagram, null, EcoreUtil.getURI(model).toString());
            // link the diagram with the model element
            dtp.getFeatureProvider().link(diagram, model);
            return model;
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates the model resource and add model.
     *
     * @param model the model
     * @throws CoreException the core exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void createModelResourceAndAddModel(final fzi.mottem.model.testrigmodel.TestRigInstance model) throws CoreException, IOException {
        final Diagram diagram = dtp.getDiagram();
        URI uri = diagram.eResource().getURI();
        uri = uri.trimFragment();
        uri = uri.trimFileExtension();
        uri = uri.appendFileExtension(FILE_EXTENSION);
        ResourceSet rSet = diagram.eResource().getResourceSet();
        final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IResource file = workspaceRoot.findMember(uri.toPlatformString(true));
        if (file == null || !file.exists()) {
            Resource resource = rSet.createResource(uri);
            resource.setTrackingModification(true);
            resource.getContents().add(model);
        } else {
            final Resource resource = rSet.getResource(uri, true);
            resource.getContents().add(model);
        }
    }
}
