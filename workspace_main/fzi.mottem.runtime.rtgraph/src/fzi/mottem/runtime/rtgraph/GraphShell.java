package fzi.mottem.runtime.rtgraph;

import java.util.ArrayList;
import java.util.HashMap;

import org.csstudio.swt.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.rtgraph.views.GraphView;

/**
 * A Comprehensive Example
 * 
 * @author Xihui Chen
 */
public class GraphShell {
	
	private XYGraph myGraph;
	private String path;
	private GraphView view;
	private Shell shell;
	private SQLiteTraceReader reader;
	private boolean autoscale = false;
	
	public GraphShell(String p) {
		path = p;
		
		shell = new Shell(Display.getDefault());
		shell.setSize(800, 400); 
		//shell.moveBelow(myGraph);
		
		shell.setLocation(-800, -800);
		
		shell.setLayout(new GridLayout());
		view  = new GraphView(shell,SWT.FILL);
		view.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		view.initializeGraph();
		reader = new SQLiteTraceReader();
		
		HashMap<String, ArrayList<DEMessage>> messages = reader.getDEMessages(path);
		
		for (String uid : messages.keySet()) {
			view.addTraceByUID(uid, false);
		}
		view.updateRepresentation();
		reader.applyDBMetaData(view.getRepresentation(), path);
		autoscale = reader.shouldDBAutoscale();
		view.applyRepresentation();
		
		for (String uid : messages.keySet()) {
			TraceExchangeLink link = view.addTraceGetLink(uid);
			link.consumeBurst(messages.get(uid));
			Display.getCurrent().asyncExec(link.getTraceUpdater());
			//view.updateRepresentation();
		}
	}
	
	//returns path to image;
	public String generatePNG(String path) {
	 
		String imagename = "";
	    shell.open();
	    
	    if(autoscale) {
	    	 Display.getCurrent().timerExec(100, new Runnable() {
	 			@Override
	 			public void run() {
	 				// TODO Auto-generated method stub
	 				view.getGraph().performAutoScale();
	 			}
	 		});
	    }
	   
	    
	    Display.getCurrent().timerExec(1000, new Runnable() {
			
			@Override
			public void run() {
				view.getGraph().performAutoScale();
				// TODO Auto-generated method stub
				Image i = view.getGraph().getImage();
			    ImageLoader loader = new ImageLoader();
			    loader.data = new ImageData[] {i.getImageData()};
			    loader.save(path, SWT.IMAGE_PNG);
			    shell.close();
				
			}
		});
	    
	   
	    return imagename;
	   
	}


	public XYGraph getMyGraph() {
		return myGraph;
	}


	public void setMyGraph(XYGraph myGraph) {
		this.myGraph = myGraph;
	}


}
