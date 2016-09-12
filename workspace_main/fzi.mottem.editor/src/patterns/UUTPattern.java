package patterns;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import util.ModelService;
import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.mottem.model.testrigmodel.UUT;

/**
 * The Class UUTPattern represents an UUT. 
 * The business object will be created in the create-method.
 * The representation of the business object is realized in the superclass AbstractComponentPattern.
 */
public class UUTPattern extends AbstractComponentPattern {

	/** The Constant WIDTH_SIZE. */
	private static final int WIDTH_SIZE = 350;
	
	/** The Constant HEIGHT_SIZE. */
	private static final int HEIGHT_SIZE = 150;
	
	/** The Constant X_IMAGE. */
	private static final int X_IMAGE = 2;
	
	/** The Constant Y_IMAGE. */
	private static final int Y_IMAGE = 2;
	
	/** The Constant WIDTH_IMAGE. */
	private static final int WIDTH_IMAGE = 15;
	
	/** The Constant HEIGHT_IMAGE. */
	private static final int HEIGHT_IMAGE = 15;
	
	/** The Constant X_TEXT. */
	private static final int X_TEXT = 20;
	
	/** The Constant Y_TEXT. */
	private static final int Y_TEXT = 5;
	
	/** The Constant WIDTH_TEXT. */
	private static final int WIDTH_TEXT = 150;
	
	/** The Constant HEIGHT_TEXT. */
	private static final int HEIGHT_TEXT = 20;
	
	/** The Constant BACKGROUNDCOLOR. */
	private static final IColorConstant BACKGROUNDCOLOR = new ColorConstant(154, 205, 50);
	
	/** The Constant TEXTFIELD. */
	private static final String TEXTFIELD = "Board";
	
	/** The Constant MIN_HEIGHT. */
	private static final int MIN_HEIGHT = 30;
	
	/** The Constant MIN_WIDTH. */
	private static final int MIN_WIDTH = 170;
	
	/** The dtp. */
	private IDiagramTypeProvider dtp;
	
	/** The inspector connectors. */
	private List<IInspectorConnector> inspectorConnectors;
	
	/** The network connectors. */
	private List<INetworkConnector> networkConnectors;
	
	
	/**
	 * Instantiates a new UUT pattern and sets the DiagramTypeProvider.
	 *
	 * @param dp the DiagramTypeProvider
	 */
	public UUTPattern(IDiagramTypeProvider dp) {
		this.dtp = dp;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		if(context.getWidth() > MIN_WIDTH && context.getHeight() > MIN_HEIGHT){
			return addGraphicalRepresentationWithImageAndText(context, BACKGROUNDCOLOR, 
					context.getWidth(), context.getHeight(), X_IMAGE, Y_IMAGE, WIDTH_IMAGE, 
					HEIGHT_IMAGE, TEXTFIELD, X_TEXT, Y_TEXT, WIDTH_TEXT, HEIGHT_TEXT);
		}
		else{
			return addGraphicalRepresentationWithImageAndText(context, BACKGROUNDCOLOR, 
					WIDTH_SIZE, HEIGHT_SIZE, X_IMAGE, Y_IMAGE, WIDTH_IMAGE, HEIGHT_IMAGE, TEXTFIELD,
					X_TEXT, Y_TEXT, WIDTH_TEXT, HEIGHT_TEXT);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context) {
		//creates a business object of UUT.
		UUT uut = TestrigmodelFactory.eINSTANCE.createUUT();
		uut.setName("Board");
		ModelService modelService = ModelService.getModelService(dtp);
		TestRigInstance model = modelService.getModel();
		model.getUuts().add(uut);
		addGraphicalRepresentation(context, uut);
		return new Object[] { uut };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return "Board";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof UUT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateImageId()
	 */
	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_UUT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateLargeImageId()
	 */
	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_UUT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canDirectEdit(org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
		return bo instanceof INamed && ga instanceof Text;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getInitialValue(org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		INamed entity = (INamed) getBusinessObjectForPictogramElement(pe);
		return entity.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#checkValueValid(java.lang.String, org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		// null = valid value
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#setValue(java.lang.String, org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		INamed as = (INamed) getBusinessObjectForPictogramElement(pe);
		as.setName(value);
		// text with the name = second children (get(1))
		if (null != pe.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()
				.get(1)
				&& pe.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()
						.get(1) instanceof Text) {
			Text text = (Text) pe.getGraphicsAlgorithm()
					.getGraphicsAlgorithmChildren().get(1);
			text.setValue(as.getName());
		}
		updatePictogramElement(context.getPictogramElement());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getEditingType()
	 */
	public int getEditingType() {
		return TYPE_TEXT;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 */
	@Override
	public void delete(IDeleteContext context) {
		this.inspectorConnectors = new ArrayList<>();
		this.networkConnectors = new ArrayList<>();
		UUT uut = (UUT) getBusinessObjectForPictogramElement(context.getPictogramElement());
		for (int i = 0; i <uut.getProcessors().size(); i++) {
				Processor processor = uut.getProcessors().get(i);
				if(processor.getInspectorConnector() != null){
					this.inspectorConnectors.add(processor.getInspectorConnector());
				}
		}
		for (int j = 0; j < uut.getIoPorts().size(); j++) {
			IOPort port = uut.getIoPorts().get(j);
			if(port.getNetworkConnector() != null){
				this.networkConnectors.add(port.getNetworkConnector());
			}
		}
		super.delete(context);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		for (int i = 0; i < inspectorConnectors.size(); i++) {
			if(this.inspectorConnectors.get(i)!=null){
				EcoreUtil.delete(this.inspectorConnectors.get(i));
			}
		} 
		for (int i = 0; i < networkConnectors.size(); i++) {
			if(this.networkConnectors.get(i)!=null){
				EcoreUtil.delete(this.networkConnectors.get(i));
			}
		} 
		super.postDelete(context);
	}
	
}