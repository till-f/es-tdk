package fzi.mottem.runtime.rtgraph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.rtgraph.XML.DashboardRepresentation;
import fzi.mottem.runtime.rtgraph.XML.GraphViewRepresentation;
import fzi.mottem.runtime.rtgraph.XML.ProfileUtils;
import fzi.mottem.runtime.rtgraph.editors.DashboardEditor;
import fzi.mottem.runtime.rtgraph.editors.GraphViewEditor;
import fzi.mottem.runtime.rtgraph.views.Dashboard;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;
import fzi.mottem.runtime.rtgraph.views.GraphView;

/**
 * This class provides methods for view calling and hiding. Its intended use is
 * providing an easy way of calling project views programmatically.
 * 
 * @author K Katev
 *
 */
public class ViewCoordinator {

	public static String view_settings_id = "fzi.mottem.runtime.rtgraph.views.SettingsViewPart";


	private ViewCoordinator() {

	}

	/**
	 * Shows the Settings (Setup UI) ViewPart. There is only one instance of it.
	 * 
	 */
	public static void showSettingsViewpart() {
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		//if (page.findViewReference(view_settings_id) == null) {
			try {
				page.showView(view_settings_id);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}

	/**
	 * Checks whether the Settings (Setup UI) ViewPart is open. There is only
	 * one instance of it.
	 * 
	 * @return <b>true</b> if the ViewPart already exists, <b> false </b> if
	 *         otherwise
	 */
	public static boolean checkSettingsViewpart() {
		boolean found = false;

		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWindow != null) {
			page = activeWindow.getActivePage();
			if (page != null)
				found = (page.findViewReference(view_settings_id) != null);
			
		}

		return found;

	}

	/**
	 * Shows the Dashboard EditorPart with the given Dashboard name. If there is
	 * no such ViewPart then a new one will be created.
	 * 
	 * <b>This method is under development</b>
	 * 
	 * @param name
	 * @return <b>true</b> if the ViewPart already exists, <b> false </b> if the
	 *         ViewPart is newly created.
	 */
	public static Dashboard showDashboardEditorPart(String name) {
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		DashboardComposite dashboard = null;

		File fileToOpen = new File(name + ".dashboard");

		if (fileToOpen.exists() && fileToOpen.isFile()) {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());

			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println(
					"File " + fileToOpen.getAbsolutePath() + ".dashboard does not exist. Generating new file.");

			DashboardRepresentation repr = new DashboardRepresentation();
			//repr.setName(name);
			repr.setPath(fileToOpen.getAbsolutePath());
			ProfileUtils.saveDashboardXML(repr);

			IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());

			try {
				System.err.println("Trying to open editor for filestore " + fileStore);
				IDE.openEditorOnFileStore(page, fileStore);

			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

		return dashboard;
	}

	/**
	 * This method returns an ArrayList of active Dashboards. The
	 * ViewCoordinator finds all ViewParts associated with the Dashboard
	 * Viewpart ID, gathers their internal Dashboards and returns them.
	 * 
	 * @return an ArrayList<Dashboard> with the currently active Dashboards
	 */
	public static ArrayList<Dashboard> getDashboards() {
		ArrayList<Dashboard> views = new ArrayList<Dashboard>();
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		IViewReference[] references = page.getViewReferences();

		for (IViewReference ref : references) { // Magic is happening.

			if (ref.getId().compareTo(DashboardEditor.editorId) == 0) {
				if (ref.getView(false) != null) {
					views.add(((DashboardEditor) ref.getView(false)).getDashboard());
				}

			} // I'd feel like a hacker if this actually works.
		}

		return views;
	}

	/**
	 * This method returns an ArrayList of active Dashboards. The
	 * ViewCoordinator finds all ViewParts associated with the Dashboard
	 * Viewpart ID, gathers their internal Dashboards and returns them.
	 * 
	 * @return an ArrayList<Dashboard> with the currently active Dashboards
	 */
	public static ArrayList<DashboardComposite> getDashboardEditors() {
		ArrayList<DashboardComposite> dashboards = new ArrayList<DashboardComposite>();
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		IEditorReference[] references = page.getEditorReferences();

		for (IEditorReference ref : references) { // Wow... Magic is happening.

			if (ref.getId().compareTo(DashboardEditor.editorId) == 0) {
				if (ref.getEditor(false) != null) {
					dashboards.add(((DashboardEditor) ref.getEditor(false)).getDashboard());
				}

			} // I'd feel like a hacker if this actually works.
		}
		return dashboards;
	}

	/**
	 * This method returns an ArrayList of active Dashboards. The
	 * ViewCoordinator finds all ViewParts associated with the Dashboard
	 * Viewpart ID, gathers their internal Dashboards and returns them.
	 * 
	 * @return an ArrayList<Dashboard> with the currently active Dashboards
	 */
	public static ArrayList<DashboardEditor> getDashboardEditorParts() {
		ArrayList<DashboardEditor> dashboards = new ArrayList<DashboardEditor>();
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		IEditorReference[] references = page.getEditorReferences();

		for (IEditorReference ref : references) { // Wow... Magic is happening.

			if (ref.getId().compareTo(DashboardEditor.editorId) == 0) {
				if (ref.getEditor(false) != null) {
					dashboards.add(((DashboardEditor) ref.getEditor(false)));

				}

			} // I'd feel like a hacker if this actually works.
		}
		return dashboards;
	}

	@Deprecated
	/**
	 * Shows the GraphViewPart with the given GraphView name. If there is no
	 * such ViewPart then a new one will be created.
	 * 
	 * <b>This method is no longer working</b>
	 * 
	 * @param name
	 * @return <b>true</b> if the ViewPart already exists, <b> false </b> if the
	 *         ViewPart is newly created.
	 */
	public static GraphView showGraphViewpart(String name) {
		GraphView view = null;	
		return view;
	}

	/**
	 * This method returns an ArrayList of active GraphViews. The
	 * ViewCoordinator finds all ViewParts associated with the GraphViewPart ID,
	 * gathers their internal GraphViews and returns them.
	 * 
	 * @return an ArrayList<GraphView> with the currently active GraphViews
	 */
	public static ArrayList<GraphView> getGraphViews() {

		ArrayList<GraphView> views = new ArrayList<GraphView>();
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		IViewReference[] references = page.getViewReferences();

		for (IViewReference ref : references) { // Wow... Magic is happening.

			if (ref.getId().compareTo(GraphViewEditor.editorId) == 0) {
				// System.out.println("Found GrapView reference: " +
				// ((GraphViewPart)ref.getView(false)).getView().getGraphName());
				if (ref.getView(false) != null) {
					views.add(((GraphViewEditor) ref.getView(false)).getView());
				}

			} // I'd feel like a hacker if this actually works.
		}

		return views;
	}
	
	/**
	 * A method that returns an ArrayList of all currently active GraphViewEditor parts
	 * @return
	 */
	
	public static ArrayList<GraphViewEditor> getGraphEditorParts() {
		ArrayList<GraphViewEditor> editors = new ArrayList<GraphViewEditor>();
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		IEditorReference[] references = page.getEditorReferences();

		for (IEditorReference ref : references) { // Wow... Magic is happening.

			if (ref.getId().compareTo(GraphViewEditor.editorId) == 0) {
				if (ref.getEditor(false) != null) {
					editors.add(((GraphViewEditor) ref.getEditor(false)));
				}
			} // I'd feel like a hacker if this actually works.
		}
		return editors;
	}

	/**
	 * Gathers the signal unique IDs used by the currently opened UI components
	 * (widgets on dashboards, etc)
	 * 
	 * @return
	 */
	public static ArrayList<String> getSignalsConsumedByUI() {
		ArrayList<String> uids = new ArrayList<String>();

		ArrayList<DashboardEditor> dashboardEditors = getDashboardEditorParts();
		ArrayList<GraphViewEditor> graphviewEditors = getGraphEditorParts();

		List<Signal> all_signals = DataExchanger.getSignals();
		ArrayList<String> all_uids = new ArrayList<String>();
		String temp;

		for (Signal s : all_signals) {
			all_uids.add(s.getId());
		}

		for (DashboardEditor de : dashboardEditors) {
			for (AbstractWidgetExchangeLink link : de.getDashboard().getWidgetLinks()) {
				temp = link.getRepresentation().getSignalUID();
				if (!uids.contains(temp) && all_uids.contains(temp)) {
					uids.add(link.getRepresentation().getSignalUID());
				}
			}
		}

		for (GraphViewEditor gve : graphviewEditors) {
			for (TraceExchangeLink link : gve.getView().getTraceExchangeLinks()) {
				temp = link.getSignalUID();
				if (!uids.contains(link.getSignalUID()) && all_uids.contains(temp)) {
					uids.add(link.getSignalUID());
				} else if (link.getSignalUID() == null) {
					System.out.println("Link " + link.getName() + " of " + gve.getTitle() + " has StringUID == null");
				}
			}
		}

		return uids;
	}

	/**
	 * Gathers the signal unique IDs used by the currently opened controller UI
	 * components (controller widgets)
	 * 
	 * @return 
	 */
	public static ArrayList<String> getSignalsProducedByUI() {
		ArrayList<String> uids = new ArrayList<String>();

		ArrayList<DashboardEditor> dashboardEditors = getDashboardEditorParts();

		List<Signal> all_signals = DataExchanger.getSignals();
		ArrayList<String> all_uids = new ArrayList<String>();
		String temp;

		for (Signal s : all_signals) {
			all_uids.add(s.getId());
		}

		for (DashboardEditor de : dashboardEditors) {

			for (AbstractWidgetExchangeLink link : de.getDashboard().getWidgetLinks()) {
				temp = link.getRepresentation().getSignalUID();
				if (link instanceof ControllerWidgetLink && !uids.contains(temp) && all_uids.contains(temp)) {
					uids.add(link.getRepresentation().getSignalUID());
				}
			}
		}

		return uids;
	}

	/**
	 * Will open and return a GraphView Editor on the given file. The editor
	 * will react differently on different (.db or .graphview) file types.
	 * 
	 * @param Java.io file
	 * @return the GraphViewEditor instance
	 */
	public static GraphViewEditor openGraphViewEditor(File file) {
		final IWorkbenchPage page;
		final IWorkbenchWindow activeWindow;
		GraphViewEditor editor = null;
		activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		page = activeWindow.getActivePage();
		
		if(file.getName().endsWith(Constants.EXTENSION_DATABASE)) {
			String rawLocation = file.getAbsolutePath();
			String representationPath = rawLocation + ".graphview";
			GraphViewRepresentation representation = new GraphViewRepresentation();
			representation.traceRepresentations.clear();
			//representation.setName(file.getName() + ".graphview");
			representation.setPath(representationPath);
			
			//TODO What if the representation already exists?
			
			boolean saved = ProfileUtils.saveGraphViewXML(representation, representationPath);
			
			if(saved) {
				File repFile = new File(representationPath);
				
				//IFile ifile= workspace.getRoot().getFileForLocation(location);
				try {
					
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					
					try {
						workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
					
					// TODO path declaration should be more straightforward
					IPath p1 = workspace.getRoot().getLocation().removeLastSegments(128);
					IPath p2 = p1.append(representationPath);
					IFile f = workspace.getRoot().getFileForLocation(p2);
					
					//editor = (GraphViewEditor) IDE.openEditorOnFileStore(page, fileStore);
					//fileStore.toLocalFile(EFS.NONE, null).
					editor = (GraphViewEditor) IDE.openEditor(page, f, true);
					
					editor.setName(repFile.getName());
					
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//editor.applyDB(file, false);
			}
			
		} else {
			//TODO no FileStore usage; use IFile
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
			//fileStore.toLocalFile(options, monitor);
			try {
				editor = (GraphViewEditor) IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			editor.setName(file.getName());	
		}

		return editor;

	}

}
