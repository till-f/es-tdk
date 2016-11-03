package diagram;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;

import patterns.IOPinPattern;
import patterns.UUTPattern;
import patterns.Connector.InspectorConnectorPattern;
import patterns.Connector.NetworkConnectorPattern;
import patterns.inspectorContainer.AgilentContainerPattern;
import patterns.inspectorContainer.HostInspectorContainerPattern;
import patterns.inspectorContainer.IC5000Pattern;
import patterns.inspectorContainer.IOnePattern;
import patterns.inspectorContainer.VN7600Pattern;
import patterns.inspectorPort.AgilentInspectorPattern;
import patterns.inspectorPort.CANInspectorPortPattern;
import patterns.inspectorPort.CDIInspectorPortPattern;
import patterns.inspectorPort.IC5000PortPattern;
import patterns.inspectorPort.IOnePortPattern;
import patterns.network.CANBusPattern;
import patterns.network.EthernetPattern;
import patterns.networkPort.CANPortPattern;
import patterns.networkPort.EthernetPortPattern;
import patterns.swExecutor.ProcessorCorePattern;
import patterns.swExecutor.ProcessorPattern;
import features.AddDataStreamInstanceToIOPortFeature;
import features.ReconnectionFeature;
import fzi.mottem.model.util.ModelUtils;


/**
 * The Class EditorFeatureProvider adds the patterns, connection patterns and features.
 * It also returns a reconnection feature class.
 */
public class EditorFeatureProvider extends DefaultFeatureProviderWithPatterns {

	/**
	 * Instantiates a new editor feature provide, sets the DiagramTypeProvider
	 * and adds patter and connection pattern classes.
	 * 
	 * @param dtp the DiagramTypeProvider
	 */
	public EditorFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
		
		addPattern(new UUTPattern(dtp));
		addPattern(new IOPinPattern(dtp));
		addPattern(new VN7600Pattern(dtp));
		addPattern(new IC5000Pattern(dtp));
		addPattern(new IOnePattern(dtp));
		addPattern(new HostInspectorContainerPattern(dtp));
		addPattern(new AgilentContainerPattern(dtp));
		addPattern(new EthernetPortPattern(dtp));
		addPattern(new CANPortPattern(dtp));
		addPattern(new ProcessorPattern(dtp));
		addPattern(new ProcessorCorePattern(dtp));
//		addPattern(new ThreadPattern(dtp));

		addPattern(new EthernetPattern(dtp));
		addPattern(new CANBusPattern(dtp));

		addPattern(new IOnePortPattern(dtp));
		addPattern(new IC5000PortPattern(dtp));
		addPattern(new CANInspectorPortPattern(dtp));
		addPattern(new CDIInspectorPortPattern(dtp));
		addPattern(new AgilentInspectorPattern(dtp));
		
		addConnectionPattern(new InspectorConnectorPattern(dtp));
//		addConnectionPattern(new TraceConnectorPattern(dtp));
//		addConnectionPattern(new CANConnectorPattern(dtp));
//		addConnectionPattern(new EthernetConnectorPattern(dtp));
//		addConnectionPattern(new JTAGConnectorPattern(dtp));
		
		addConnectionPattern( new NetworkConnectorPattern(dtp));
//		addConnectionPattern(new CANPortConnectorPattern(dtp));
//		addConnectionPattern(new EthernetPortConnectorPattern(dtp));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns#getAddFeature(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		if (context.getNewObject() instanceof IFile) {
			IFile file = (IFile) context.getNewObject();
			
			//for Drag&Drop of a data stream file onto an I/O-Port.
			if(file.getFileExtension().equals(ModelUtils.FILE_EXTENSION_ND_MODEL)){
				return new AddDataStreamInstanceToIOPortFeature(this);
			}
			return null;
         }
		
		if (context.getNewObject() instanceof IFolder) {
//			System.out.println("IFolder");
			return null;
		}
		if (context.getNewObject() instanceof IProject) {
//			System.out.println("IProject");
			return null;
		}
		 
		return super.getAddFeature(context);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.impl.AbstractFeatureProvider#getReconnectionFeature(org.eclipse.graphiti.features.context.IReconnectionContext)
	 */
	@Override
	public IReconnectionFeature getReconnectionFeature(IReconnectionContext context) {
	    return new ReconnectionFeature(this, this.getDiagramTypeProvider());
	} 
	
}
