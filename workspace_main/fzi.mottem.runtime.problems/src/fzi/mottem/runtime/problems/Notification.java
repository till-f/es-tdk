package fzi.mottem.runtime.problems;

import org.eclipse.swt.graphics.Color;

public class Notification extends ProblemEvent {

	public Notification(String date, int index, String filepath, int line, String message) {
		super(date,index, filepath, line, message, new Color(null, 0,0,0), "notification");
		// TODO Auto-generated constructor stub
	}

}
