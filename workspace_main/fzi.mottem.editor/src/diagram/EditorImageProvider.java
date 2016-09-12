package diagram;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

/**
 * The Class EditorImageProvider has the used images (icons) and makes them available.
 */
public class EditorImageProvider extends AbstractImageProvider {

	// The prefix for all identifiers of this image provider
	/** The Constant PREFIX. */
	protected static final String PREFIX = "fzi.mottem.editor.";
	
	/** The Constant IMG_PROCESSORCORE. */
	public static final String IMG_PROCESSORCORE = PREFIX + "ProcessorCore";
	
	/** The Constant IMG_PROCESSOR. */
	public static final String IMG_PROCESSOR = PREFIX + "Processor";
	
	/** The Constant IMG_Inspector. */
	public static final String IMG_Inspector = PREFIX + "Inspector";

	/** The Constant IMG_Inspector_VN7600. */
	public static final String IMG_Inspector_VN7600 = PREFIX + "Inspector_VN7600";

	/** The Constant IMG_Inspector_IC5000. */
	public static final String IMG_Inspector_IC5000 = PREFIX + "Inspector_IC5000";

	/** The Constant IMG_Inspector_AgilentContainer. */
	public static final String IMG_Inspector_AgilentContainer = PREFIX + "Inspector_Agilent";

	/** The Constant IMG_Inspector_IOne. */
	public static final String IMG_Inspector_IOne = PREFIX + "Inspector_IOne";

	/** The Constant IMG_Inspector_HostInspector. */
	public static final String IMG_Inspector_HostInspector = PREFIX + "Inspector_HostInspector";

	/** The Constant IMG_InspectorConnector. */
	public static final String IMG_InspectorConnector = PREFIX + "InspectorConnector";

	/** The Constant IMG_NetworkConnector. */
	public static final String IMG_NetworkConnector = PREFIX + "NetworkConnector";

	/** The Constant IMG_Ethernet. */
	public static final String IMG_Ethernet = PREFIX + "Ethernet";
	
	/** The Constant IMG_CANBus. */
	public static final String IMG_CANBus = PREFIX + "CANBus";
	
	/** The Constant IMG_UUT. */
	public static final String IMG_UUT = PREFIX + "UUT";
	
	/** The Constant IMG_IOPIN. */
	public static final String IMG_IOPIN = PREFIX + "IOPIN";
	
	/** The Constant IMG_IOPORT. */
	public static final String IMG_IOPORT = PREFIX + "IOPORT";
	
	/** The Constant IMG_THREAD. */
	public static final String IMG_THREAD = PREFIX + "Thread";
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#addAvailableImages()
	 */
	@Override
	protected void addAvailableImages() {
		// register the path for each image identifier
		addImageFilePath(IMG_PROCESSORCORE, "icons/ProcessorCore.gif");
		addImageFilePath(IMG_PROCESSOR, "icons/Processor.gif");
		addImageFilePath(IMG_Inspector, "icons/Inspector.gif");
		addImageFilePath(IMG_Inspector_VN7600, "icons/Inspector_VN7600.gif");
		addImageFilePath(IMG_Inspector_IC5000, "icons/Inspector_IC5000.gif");
		addImageFilePath(IMG_Inspector_AgilentContainer, "icons/Inspector_AgilentContainer.gif");
		addImageFilePath(IMG_Inspector_IOne, "icons/Inspector_IOne.gif");
		addImageFilePath(IMG_Inspector_HostInspector, "icons/Inspector_HostInspector.gif");
		addImageFilePath(IMG_InspectorConnector, "icons/InspectorConnector.gif");
		addImageFilePath(IMG_NetworkConnector, "icons/NetworkConnector.gif");
		addImageFilePath(IMG_Ethernet, "icons/Ethernet.gif");
		addImageFilePath(IMG_CANBus, "icons/CANBus.gif");
		addImageFilePath(IMG_UUT, "icons/UUT.gif");
		addImageFilePath(IMG_IOPIN, "icons/IOPin.gif");
		addImageFilePath(IMG_IOPORT, "icons/IOPort.gif");
		addImageFilePath(IMG_THREAD, "icons/Thread.gif");
	}

}
