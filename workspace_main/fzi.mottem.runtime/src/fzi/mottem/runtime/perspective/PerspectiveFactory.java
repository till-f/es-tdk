package fzi.mottem.runtime.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class PerspectiveFactory implements IPerspectiveFactory
{
	@Override
	public void createInitialLayout(IPageLayout layout)
	{
        // Get the editor area ID.
        String editorArea = layout.getEditorArea();

        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
        topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);

        IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.75f, "topLeft");
        bottomLeft.addView(IPageLayout.ID_PROGRESS_VIEW);

        IFolderLayout bottomMain = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.75f, editorArea);
        bottomMain.addView(IPageLayout.ID_PROBLEM_VIEW);
        bottomMain.addView(IPageLayout.ID_PROP_SHEET);
        bottomMain.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        
        // ouch, no interface class for global constants...
        bottomMain.addView(fzi.mottem.runtime.problems.Activator.VIEWER_ID);
        //bottomMain.addView(fzi.mottem.runtime.rtgraph.ViewCoordinator.view_settings_id);
	}

}
