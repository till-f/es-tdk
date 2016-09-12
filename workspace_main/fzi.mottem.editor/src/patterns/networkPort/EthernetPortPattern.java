package patterns.networkPort;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import fzi.mottem.model.testrigmodel.EthernetPort;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.mottem.model.testrigmodel.UUT;

/**
 * The Class EthernetPortPattern represents a ethernet port. 
 * The business object will be created in the create-method.
 * The representation of the business object is realized in the superclass AbstractComponentPattern.
 * An ethernet port can be added to an UUT.
 */
public class EthernetPortPattern extends IOPortPattern {
	
	/** The Constant WIDTH_SIZE. */
	private static final int WIDTH_SIZE = 20;
	
	/** The Constant HEIGHT_SIZE. */
	private static final int HEIGHT_SIZE = 25;
	
	/** The Constant X_IMAGE. */
	private static final int X_IMAGE = 2;
	
	/** The Constant Y_IMAGE. */
	private static final int Y_IMAGE = 2;
	
	/** The Constant WIDTH_IMAGE. */
	private static final int WIDTH_IMAGE = 15;
	
	/** The Constant HEIGHT_IMAGE. */
	private static final int HEIGHT_IMAGE = 15;
	
	/** The Constant BACKGROUNDCOLOR. */
	private static final IColorConstant BACKGROUNDCOLOR = new ColorConstant(240, 230, 140);
	
	
	/**
	 * Instantiates a new ethernet port pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public EthernetPortPattern(IDiagramTypeProvider dtp) {
		this.dtp = dtp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context) {
		//creates a business object of EthernetPort.
		EthernetPort port = TestrigmodelFactory.eINSTANCE.createEthernetPort();
		port.setName("EthernetPort");
        final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
        if (target instanceof UUT) {
           UUT uut = (UUT) target;
            uut.getIoPorts().add(port);
            port.setUut(uut);
        }

		addGraphicalRepresentation(context, port);
		return new Object[] { port };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return "Ethernet Port";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		return addGraphicalRepresentationWithImage(context, BACKGROUNDCOLOR, WIDTH_SIZE, HEIGHT_SIZE,
				X_IMAGE, Y_IMAGE, WIDTH_IMAGE, HEIGHT_IMAGE);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof EthernetPort;
	}

}
