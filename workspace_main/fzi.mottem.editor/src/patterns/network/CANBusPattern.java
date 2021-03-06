package patterns.network;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.IColorConstant;

import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.testrigmodel.CANBus;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

/**
 * The Class CANBusPattern represents an CAN Bus. 
 * The business object will be created in the create-method.
 * The representation of the business object is realized in the superclass AbstractComponentPattern.
 */
public class CANBusPattern extends NetworkPattern {

	/** The Constant HEIGHT. */
	private static final int HEIGHT = 50;
	
	/** The Constant WIDTH. */
	private static final int WIDTH = 100;
	
	/** The Constant X_TEXT. */
	private static final int X_TEXT = 20;
	
	/** The Constant Y_TEXT. */
	private static final int Y_TEXT = 15;
	
	/** The Constant WIDTH_TEXT. */
	private static final int WIDTH_TEXT = 60;
	
	/** The Constant HEIGHT_TEXT. */
	private static final int HEIGHT_TEXT = 20;
	
	/** The Constant TEXTFIELD. */
	private static final String TEXTFIELD = "CAN Bus";
	
	/** The Constant BACKGROUNDCOLOR. */
	private static final IColorConstant BACKGROUNDCOLOR = IColorConstant.WHITE;
	
	/** The n connector. */
	private List<INetworkConnector> nConnector;
	
	/** The connector. */
	private IInspectorConnector connector;


	/**
	 * Instantiates a new can bus pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public CANBusPattern(IDiagramTypeProvider dtp) {
		this.dtp = dtp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context)
	{
		final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
		
		if (!(target instanceof TestRigInstance))
			throw new RuntimeException("Cannot add CANBus to model element " + target.getClass().getSimpleName());
		
		CANBus canBus = TestrigmodelFactory.eINSTANCE.createCANBus();
		canBus.setName("CANBus");

		((TestRigInstance) target).getNetworks().add(canBus);
		addGraphicalRepresentation(context, canBus);
		
		return new Object[] { canBus };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		return addGraphicalRepresentationWithImageAndText(context, BACKGROUNDCOLOR, WIDTH, HEIGHT, 
				0, 0, 0, 0, TEXTFIELD, X_TEXT, Y_TEXT, WIDTH_TEXT, HEIGHT_TEXT);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return TEXTFIELD;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof CANBus;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Saves the connection (business object) from inspector to the can bus and the network connections (business objects) 
	 * to delete them later if the user wants to delete the can bus.
	 * 
	 */
	@Override
	public void delete(IDeleteContext context) {
		this.nConnector = new ArrayList<>();
		CANBus cANBus = (CANBus) getBusinessObjectForPictogramElement(context.getPictogramElement());
		this.connector = cANBus.getInspectorConnector();
		for (int i = 0; i <cANBus.getNetworkConnector().size(); i++) {
			if(cANBus.getNetworkConnector().get(i) != null){
				this.nConnector.add(cANBus.getNetworkConnector().get(i));
			}
		}
		super.delete(context);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Method will be called if user confirmed to delete the can bus.
	 * Deletes the connections (business objects) from inspector to the can bus 
	 * and the network connections (business objects).
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		for (int i = 0; i < nConnector.size(); i++) {
			if(this.nConnector.get(i)!=null){
				EcoreUtil.delete(this.nConnector.get(i));
			}
		} 
		if(this.connector!=null){
			EcoreUtil.delete(this.connector);
		}
		super.postDelete(context);
	}

	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_CANBus;
	}

	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_CANBus;
	}

}
