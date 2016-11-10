package fzi.mottem.runtime.rtgraph.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.linearscale.Range;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.SQLiteTraceReader;
import fzi.mottem.runtime.rtgraph.TraceExchangeLink;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.XML.GraphViewRepresentation;
import fzi.mottem.runtime.rtgraph.XML.ProfileUtils;
import fzi.mottem.runtime.rtgraph.commands.CreateDBOptions;
import fzi.mottem.runtime.rtgraph.dialogs.DBMessageDialog;
import fzi.mottem.runtime.rtgraph.views.GraphView;

public class GraphViewEditor extends EditorPart {

	GraphView view;

	private FileEditorInput input;
	private GraphViewRepresentation representation;
	private CreateDBOptions options;
	private IFile dbFile = null;

	private boolean openDialog = true;

	public static final String editorId = "fzi.mottem.runtime.rtgraph.editors.GraphViewEditor";

	public GraphView getView() {
		return view;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof FileEditorInput || input instanceof FileStoreEditorInput)) {
			throw new RuntimeException(
					"Wrong input, tooltip: " + input.getToolTipText() + " " + input.getClass().getSimpleName());
		}

		setSite(site);
		setInput(input);
		options = new CreateDBOptions();
		
		setName(input.getName());

		if (input.getName().endsWith(Constants.EXTENSION_DATABASE)) {
			// IFile file =
			// this.input = new FileEditorInput(file)
			String rawLocation = ((FileEditorInput) input).getFile().getRawLocation().toOSString();
			String representationPath = rawLocation + ".graphview";
			
			representation = new GraphViewRepresentation();
			representation.traceRepresentations.clear();
			representation.setPath(representationPath);

			this.input = (FileEditorInput) input;
			// this.input.getFile().set
			dbFile = ((FileEditorInput) input).getFile();
			// this.setDbFile(((FileEditorInput) input).getFile());

		} else {
			// IEditorInput fei = (IEditorInput) input;
			this.input = (FileEditorInput) input;
			// again, haxxor stuff...
			IFile f = this.input.getFile();
			
			String rawOSLocation = f.getRawLocation().toOSString();
			representation = ProfileUtils.getGraphViewModel(rawOSLocation);
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			// ".graphview" has 10 symbols
			IPath dblocation = f.getLocation().removeFileExtension();
			dbFile = workspace.getRoot().getFileForLocation(dblocation);

			// if dbFile != null, then it will be applied in the
			// createPartControl method
			if (dbFile != null && dbFile.getName().endsWith(Constants.EXTENSION_DATABASE)) {
				System.out.println("Found .db file for " + input.getName() + "!");
				options.applyMetaData = false;
				openDialog = false;
			}
		}

		setSite(site);
		// setInput(this.input);

	}

	@Override
	public void createPartControl(Composite parent) {
		initializeMainHolder(parent);

		if (dbFile != null && dbFile.getName().endsWith(Constants.EXTENSION_DATABASE)) {

			System.out.println("GraphViewEditor: Applying DB file " + dbFile.getName() + "...");
			System.out.println(dbFile.getRawLocation());

			IFileStore fileStore = EFS.getLocalFileSystem().getStore(dbFile.getRawLocation());

			try {
				applyDB(fileStore.toLocalFile(0, null), openDialog, options);
			} catch (CoreException e1) {
				System.out.println("GraphViewEditor: There is a problem with the DB File conversion...");
				e1.printStackTrace();
			}
			// view.getGraph().performAutoScale();

			for (Axis axis : view.getGraph().getXAxisList()) {

				Range dataRange = view.getGraph().primaryXAxis.getTraceDataRange();
				if (dataRange != null) {
					axis.setRange(dataRange.getUpper(), dataRange.getLower(), true);
					axis.repaint();
				}
			}
			for (Axis axis : view.getGraph().getYAxisList()) {
				axis.performAutoScale(true);
				axis.repaint();
				// TODO better Y-Axis management
			}
		}

		/*
		 * It is very (!) important to add dispose listeners; Otherwise there
		 * will be leftover TraceExchangeLinks that may be registered to the
		 * DataExchanger but will have nowhere to send their data to. This may
		 * lead to inconsistencies and, in the worst case, to exceptions.
		 */
		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				for (Trace t : view.getTraces()) {
					view.removeTrace(t);
				}
			}
		});
		
		view.setDirty(false);
		ViewCoordinator.showSettingsViewpart();
	}

	private void initializeMainHolder(Composite parent) {
		view = new GraphView(parent, SWT.None, representation);
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		view.initializeGraph();
		view.applyRepresentation();
		view.setEditor(this);
	}

	public void setName(String name) {
		//if(name.endsWith(Constants.EXTENSION_GRAPHVIEW)) {
		//	setPartName(name.substring(0, name.length() - Constants.EXTENSION_GRAPHVIEW.length()));
		//} else {
			setPartName(name);
		//}
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		boolean saved = false;
		try {
			subMonitor.beginTask("Save", 2);
			view.updateRepresentation();
			subMonitor.worked(1); // call worked() for each (sub-) job
			String location = input.getFile().getRawLocation().toOSString();

			if (!location.endsWith(".graphview")) {
				saved = ProfileUtils.saveGraphViewXML(representation, location + ".graphview");

				// TODO change input
				if (saved) {
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					
					try {
						workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					IPath newpath = input.getFile().getLocation();
					//newpath.removeFileExtension();
					newpath = newpath.addFileExtension("graphview");

					IFile f = workspace.getRoot().getFileForLocation(newpath);
					setInput(new FileEditorInput(f));
				}

			} else {
				saved = ProfileUtils.saveGraphViewXML(representation, location);
			}
			subMonitor.worked(1); // call worked() for each (sub-) job
			view.setDirty(false); // inner call of
									// firePropertyChange(PROP_DIRTY);

		} finally {
			subMonitor.done();
		}
	}

	@Override
	public void doSaveAs() {
		Shell shell = new Shell(Display.getCurrent());
		// shell.setSize(400, 400);
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText("Save GraphView XML");
		fd.setFilterPath(representation.getPath());
		String[] filterExt = { "*.*", "*" + Constants.EXTENSION_GRAPHVIEW };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();

		if (selected != null) {
			System.out.println("GraphViewEditor: Saving " + view.getGraphName() + " in " + selected);
			String newName = selected.substring(selected.lastIndexOf(File.separator) + 1);
			view.updateRepresentation();
			ProfileUtils.saveGraphViewXML(view.getRepresentation(), selected, newName);
			view.setDirty(false); // inner call of
									// firePropertyChange(PROP_DIRTY);
		}
	}

	@Override
	public boolean isDirty() {

		return view.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * This method applies a .db file to the GraphView Editor. Application means
	 * loading the saved signal entries (unique id x value x time stamp) to
	 * designated traces in the GraphView.
	 * 
	 * @param file
	 *            The .db file
	 * @param openDialog
	 *            If true, a UI Window dialog will open and the user will be
	 *            prompted to select a subset of signals and options for the .db
	 *            application
	 * @param opt
	 *            An object used to both represent the user Dialog input and
	 *            give additional application information to the method. May be
	 *            set to null. It will be overridden if a dialog is called
	 *            (openDialog == true)
	 */

	public void applyDB(File file, boolean openDialog, CreateDBOptions opt) {

		if (file != null) {
			String path = file.getAbsolutePath();

			// IPath osPath =
			// IntegrationUtils.getSystemPathForWorkspaceRelativePath(iFile.getFullPath());
			SQLiteTraceReader reader = new SQLiteTraceReader();
			// reader.printTraceTable(osPath.toOSString());
			/*
			 * Question! HashMap<Signal,...> or HashMap<uid,...> ???
			 */

			if (opt != null) {
				options = opt;
			} else {
				options = new CreateDBOptions();
			}
			
			HashMap<String, ArrayList<DEMessage>> msgMap = reader.getDEMessages(path);
			Shell shell = new Shell(Display.getCurrent());
			
			if (msgMap.size() > 0) {

				if (openDialog) {

					options.uids = new ArrayList<String>(msgMap.keySet());
					options.graphName = file.getName();

					DBMessageDialog dialog = new DBMessageDialog(shell, options);
					dialog.create();

					dialog.setListItems(msgMap.keySet().toArray(new String[0]));
					// user pressed cancel
					if (dialog.open() == Window.OK) {

						if (options.applyMetaData) {

							for (String uid : options.uids) {
								view.addTraceByUID(uid, false);
								// automatically connects the trace link to the
								// data exchanger
							}

							view.updateRepresentation();
							reader.applyDBMetaData(view.getRepresentation(), path);
							view.applyRepresentation();
						}
					
						for (String uid : options.uids) {
							TraceExchangeLink tl = view.addTraceGetLink(uid);
							tl.consumeBurst(msgMap.get(uid));
							if (options.connectToDataExchanger)
								DataExchanger.replaceSignal(uid, tl);
							Display.getCurrent().asyncExec(tl.getTraceUpdater());
						}
					}
				} else {

					// first add all needed traces
					if (options.applyMetaData) {

						for (String uid : msgMap.keySet()) {
							view.addTraceByUID(uid, true);
						}

						view.updateRepresentation();
						// then apply the metadata on their representations
						reader.applyDBMetaData(view.getRepresentation(), path);
						view.applyRepresentation();
						System.out.println("GraphViewEditor: Applying metadata from " + path);
					}

					for (TraceExchangeLink tl : view.getTraceExchangeLinks()) {
						if(msgMap.get(tl.getSignalUID()) != null) {
							tl.consumeBurst(msgMap.get(tl.getSignalUID()));
							Display.getCurrent().asyncExec(tl.getTraceUpdater());
						}	
					}
					// so far so good
				}

			} else {
				MessageDialog.openInformation(shell, "DB Information", "DB \n" + path + "\nis empty");
			}
		}

	}

	public IFile getDbFile() {
		return dbFile;
	}

	public void setDbFile(IFile dbFile) {
		this.dbFile = dbFile;
	}

	public void fireChange() {
		firePropertyChange(PROP_DIRTY);
	}

}
