package diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;

import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.INetwork;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.InspectorContainer;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import patterns.IOPinPattern;
import patterns.Connector.InspectorConnectorPattern;
import patterns.Connector.NetworkConnectorPattern;

/**
 * The Class EditorToolBehaviorProvider is used to describe the tool behavior.
 * Implemented methods are getPalette to add palette entries into the palette,
 * getContextButtonPad to change context buttons and getToolTip to add tooltip text.
 */
public class EditorToolBehaviorProvider extends DefaultToolBehaviorProvider {

	/**
	 * Instantiates a new editor tool behavior provider and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public EditorToolBehaviorProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.tb.DefaultToolBehaviorProvider#getPalette()
	 * 
	 * Adds palette entries into the palette. 
	 * First there have compartments to be created.
	 * Then the create features have to be assigned to a compartment.
	 * And then the create connection have to be assigned to a compartment.  
	 */
	@Override
	public IPaletteCompartmentEntry[] getPalette()
	{
		List<IPaletteCompartmentEntry> paletteCompartments =
				new ArrayList<IPaletteCompartmentEntry>();

		PaletteCompartmentEntry compartmentEntryComponents =
				new PaletteCompartmentEntry("Components", null);
		paletteCompartments.add(compartmentEntryComponents);

		PaletteCompartmentEntry compartmentEntryInspector =
				new PaletteCompartmentEntry("Access HW", null);
		paletteCompartments.add(compartmentEntryInspector);
		
		PaletteCompartmentEntry compartmentEntryNetwork =
				new PaletteCompartmentEntry("Network", null);
		paletteCompartments.add(compartmentEntryNetwork);
		
		PaletteCompartmentEntry compartmentEntryConnectors =
				new PaletteCompartmentEntry("Connectors", null);
		paletteCompartments.add(compartmentEntryConnectors);
		
		// add all create-features 
		IFeatureProvider featureProvider = getFeatureProvider();
		ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
		
		for (ICreateFeature createFeature : createFeatures)
		{
			ObjectCreationToolEntry objectCreationToolEntry =
					new ObjectCreationToolEntry(createFeature.getCreateName(),
							createFeature.getCreateDescription(), createFeature.getCreateImageId(),
							createFeature.getCreateLargeImageId(), createFeature);
			String createName = createFeature.getName();
			
			if( createName.equals("Board") || createName.equals("Processor") || createName.equals(IOPinPattern.INITIAL_NAME) 
					|| createName.equals("CAN Port") || createName.equals("Ethernet Port")
					)
			{
				compartmentEntryComponents.addToolEntry(objectCreationToolEntry);
			} 
			else if( createName.equals("VN7600") || createName.equals("IC5000") ||
					 createName.equals("IOne") || createName.equals("Eclipse Debugger") ||
					 createName.equals("DSOX2012A")
					)
			{
				compartmentEntryInspector.addToolEntry(objectCreationToolEntry);
			}
			else if( createName.equals("CAN Bus") || createName.equals("Ethernet") 
					 )
			{
				compartmentEntryNetwork.addToolEntry(objectCreationToolEntry);
			}
		}

		// add all create-connection-features
		ICreateConnectionFeature[] createConnectionFeatures =
				featureProvider.getCreateConnectionFeatures();
		for (ICreateConnectionFeature createConnectionFeature : createConnectionFeatures) {
			ConnectionCreationToolEntry connectionCreationToolEntry =
					new ConnectionCreationToolEntry(createConnectionFeature.getCreateName(), createConnectionFeature
							.getCreateDescription(), createConnectionFeature.getCreateImageId(),
							createConnectionFeature.getCreateLargeImageId());
			connectionCreationToolEntry.addCreateConnectionFeature(createConnectionFeature);
			String createConnectionName = createConnectionFeature.getCreateName();
			if(createConnectionName.equals(InspectorConnectorPattern.INITIAL_NAME)
					 ){
				compartmentEntryConnectors.addToolEntry(connectionCreationToolEntry);
			}
			else if(createConnectionName.equals(NetworkConnectorPattern.INITIAL_NAME)
					){ 
				compartmentEntryConnectors.addToolEntry(connectionCreationToolEntry);
			}
		}

		return paletteCompartments.toArray(new IPaletteCompartmentEntry[paletteCompartments.size()]);	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.tb.DefaultToolBehaviorProvider#getContextButtonPad(org.eclipse.graphiti.features.context.IPictogramElementContext)
	 * 
	 * Changes the context buttons of the graphical representations, so that there is no remove button.
	 * Remove = remove figure from diagram but not from model (should be not supported).
	 */
	@Override
	public IContextButtonPadData getContextButtonPad(
	                                   IPictogramElementContext context) {
	    IContextButtonPadData contextButtonPadData = super.getContextButtonPad(context);
	    
	    //data.getGenericContextButtons().get(1) = Remove_ContextButton
	    //Remove = remove figure from diagram but not from model
	    contextButtonPadData.getGenericContextButtons().remove(1);
	    return contextButtonPadData;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.tb.DefaultToolBehaviorProvider#getToolTip(org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm)
	 * 
	 * Creates tooltip text for some graphical representations.
	 * Get business object of pictogram element.
	 * Check the business object for special Eclasses.
	 * If true -> sets tooltip text.
	 */
	@Override
	public String getToolTip(GraphicsAlgorithm ga) {
	    PictogramElement pictogramElement = ga.getPictogramElement();
	    Object businessObject = getFeatureProvider().getBusinessObjectForPictogramElement(pictogramElement);
	    if (businessObject instanceof InspectorContainer) {
	        String name = ((InspectorContainer)businessObject).getName();
	        if (name != null && !name.isEmpty()) {
	            return name;        
	        }
	    }
	    else if (businessObject instanceof IInspector) {
	    	String name = ((InspectorContainer) ((IInspector)businessObject).eContainer()).getName();
	    	if (name != null && !name.isEmpty()) {
	    		return name;        
	    	}
	    }
	    else if (businessObject instanceof INetwork) {
	    	String name = ((INetwork)businessObject).getName();
	    	if (name != null && !name.isEmpty()) {
	    		return name;        
	    	}
	    }
	    else if (businessObject instanceof ProcessorCore) {
	    	String name = ((ProcessorCore)businessObject).getName();
	    	if (name != null && !name.isEmpty()) {
	    		return name;        
	    	}
	    }
	    else if (businessObject instanceof IOPin) {
	    	String name = ((IOPin)businessObject).getName();
	    	if (name != null && !name.isEmpty()) {
	    		return name;        
	    	}
	    }
	    return (String) super.getToolTip(ga);
	}
}