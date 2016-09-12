package diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

/**
 * The Class EditorDiagramTypeProvider is the starting point for the editor.
 * It sets a FeatureProvider and a ToolBehaviorProvider.
 */
public class EditorDiagramTypeProvider extends AbstractDiagramTypeProvider {
	
	/** The tool behavior providers. */
	private IToolBehaviorProvider[] toolBehaviorProviders;
	
	/**
	 * Instantiates a new editor diagram type provider and sets a FeatureProvider.
	 */
	public EditorDiagramTypeProvider() {
		super();
		setFeatureProvider(new EditorFeatureProvider(this));
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.graphiti.dt.AbstractDiagramTypeProvider#getAvailableToolBehaviorProviders()
     */
    @Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders =
                new IToolBehaviorProvider[] { new EditorToolBehaviorProvider(this) };
        }
        return toolBehaviorProviders;
    }

}

