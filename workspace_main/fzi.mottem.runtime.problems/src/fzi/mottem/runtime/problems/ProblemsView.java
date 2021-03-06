package fzi.mottem.runtime.problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;

public class ProblemsView extends ViewPart {
	
	private static final ArrayList<ProblemEvent> problems = new ArrayList<ProblemEvent>();

	private static TableViewer viewer;
	private static Table table;
	private static boolean initialized = false;
	private static ProblemsComparator comparator;
	private final String[] titles = { "Date", "Message", "Severity", "File", "Line", "Offset", "Length"};
	
	private static int arrivalInd = 0;
	private static Object tableLocked = new Object();
	

	public static void addProblem(PTS_ESEVERITY severity, String time, String src, int offset, int length, int lineNr, String message) {
		
		synchronized (tableLocked) {
			switch(severity)
			{
			case INFO:
				problems.add(new Notification(time, arrivalInd, src, offset, length, lineNr, message));
				break;
			case WARNING:
				problems.add(new Warning(time, arrivalInd, src, offset, length, lineNr, message));
				break;
			case ERROR:
			case FATAL:
				problems.add(new Error(time, arrivalInd, src, offset, length, lineNr, message));
				break;
			}
			arrivalInd++;
			
			if (!isActive()) {
					Display.getDefault().asyncExec(new Runnable() {
					    @Override
					    public void run() {
							try {
								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(Activator.VIEWER_ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
					    }
					});
			}

			Display.getDefault().asyncExec(new Runnable() {
				public void run() { 
					synchronized (tableLocked) {
						while (!isActive()); // ugly busy wait
						fillTable();
					}
				}
			}); 
		}
	}
	
	public ProblemsView() {
		comparator = new ProblemsComparator();
	    comparator.setColumn(ProblemsComparator.DATE);
	    comparator.setDirection(ProblemsComparator.ASCENDING);
	    initialized = true;
	}
	
	private static synchronized void fillTable() {
		
		synchronized (tableLocked) {
	    // Turn off drawing to avoid flicker
	    table.setRedraw(false);

	    // We remove all the table entries, sort our
	    // rows, then add the entries
	    table.removeAll();
	    Collections.sort(problems, comparator);
	    for (Iterator<ProblemEvent> itr = problems.iterator(); itr.hasNext();) {
	    	synchronized (tableLocked) {
	    		ProblemEvent problem = (ProblemEvent) itr.next();
	  	      TableItem item = new TableItem(table, SWT.NONE);
	  	      int c = 0;
	  	      item.setText(c++, problem.getDate());
	  	      item.setText(c++, problem.getMessage());
	  	      item.setText(c++, problem.getType());
	  	      item.setText(c++, problem.getFilepath());
	  	      item.setText(c++, Integer.toString(problem.getLine()));
	  	      item.setText(c++, Integer.toString(problem.getOffset()));
	  	      item.setText(c++, Integer.toString(problem.getLength()));
	  	      item.setForeground(problem.getClr());
			}
	      
	    }

	    if (problems.size() > 10) {
	    	table.setSelection(arrivalInd-1);
	    	table.showSelection();
	    }
	    
	    // Turn drawing back on
	    table.setRedraw(true);
		}
	  }

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL| SWT.FULL_SELECTION);
		
	    table = viewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    viewer.setContentProvider(new ArrayContentProvider());
	    getSite().setSelectionProvider(viewer);
	    
	    initTable();
	    
	    table.addListener(SWT.MouseDoubleClick, new Listener() {
	        public void handleEvent(Event event) {
	          Point pt = new Point(event.x, event.y);
	          TableItem item = table.getItem(pt);
	          if (item == null)
	            return;
	          for (int i = 0; i < table.getColumnCount(); i++) {
	            Rectangle rect = item.getBounds(i);
	            if (rect.contains(pt)) {
	            	OpenFileCommand.open(item.getText(3),
	            			Integer.parseInt(item.getText(5)),
	            			Integer.parseInt(item.getText(6))
	            			);
	            }
	          }
	        }
	      }); 
	    
	    // INFO:
	    // http://www.vogella.com/tutorials/EclipseJFaceTable/article.html
	    
	    
	}
	
	private void initTable() {
		TableColumn[] columns = new TableColumn[titles.length+1];
	    columns[0] = new TableColumn(table, SWT.NONE);
	    columns[0].setText(titles[0]);
	    columns[0].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        comparator.setColumn(ProblemsComparator.DATE);
	        comparator.reverseDirection();
	        fillTable();
	      }
	    });
	    columns[0].setWidth(160);

	    columns[1] = new TableColumn(table, SWT.NONE);
	    columns[1].setText(titles[1]);
	    columns[1].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	        comparator.setColumn(ProblemsComparator.FILEPATH);
	        comparator.reverseDirection();
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        fillTable();
	      }
	    });
	    columns[1].setWidth(400);

	    columns[2] = new TableColumn(table, SWT.RIGHT);
	    columns[2].setText(titles[2]);
	    columns[2].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	        comparator.setColumn(ProblemsComparator.SEVERITY);
	        comparator.reverseDirection();
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        fillTable();
	      }
	    });
	    columns[2].setWidth(100);

	    
	    columns[3] = new TableColumn(table, SWT.NONE);
	    columns[3].setText(titles[3]);
	    columns[3].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        comparator.setColumn(ProblemsComparator.FILEPATH);
	        comparator.reverseDirection();
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        fillTable();
	      }
	    });
	    columns[3].setWidth(100);

	    
	    columns[4] = new TableColumn(table, SWT.NONE);
	    columns[4].setText(titles[4]);
	    columns[4].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        comparator.setColumn(ProblemsComparator.LINENUMBER);
	        comparator.reverseDirection();
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        fillTable();
	      }
	    });
	    columns[4].setWidth(100);


	    columns[5] = new TableColumn(table, SWT.NONE);
	    columns[5].setText(titles[5]);
	    columns[5].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        comparator.setColumn(ProblemsComparator.OFFSET);
	        comparator.reverseDirection();
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        fillTable();
	      }
	    });
	    columns[5].setWidth(100);
	    
	    
	    columns[6] = new TableColumn(table, SWT.NONE);
	    columns[6].setText(titles[6]);
	    columns[6].addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        comparator.setColumn(ProblemsComparator.SEVERITY);
	        comparator.reverseDirection();
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        fillTable();
	      }
	    });
	    columns[6].setWidth(100);
	    
	    
	    columns[7] = new TableColumn(table, SWT.NONE);
	    columns[7].setText("Clear Table");
	    columns[7].addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
	            switch (e.type) {
	            case SWT.Selection:
	            	comparator = new ProblemsComparator();
	        	    comparator.setColumn(ProblemsComparator.DATE);
	        	    comparator.setDirection(ProblemsComparator.ASCENDING);
	        	    problems.clear();
	        	    arrivalInd = 0;
	        	    fillTable();
	              break;
	            }
	          }
	        });
	    columns[7].setWidth(100);
	    
	    fillTable();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public static boolean isActive() {
		return (initialized && viewer != null && !viewer.getControl().isDisposed());
	}
}
