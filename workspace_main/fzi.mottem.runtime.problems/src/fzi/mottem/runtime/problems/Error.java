package fzi.mottem.runtime.problems;


import org.eclipse.swt.graphics.Color;

public class Error extends ProblemEvent {

	public Error(String date, int index, String filepath, int line, String message) {
		super(date, index, filepath, line, message, new Color(null, 255,0,0), "error");
		// TODO Auto-generated constructor stub
	}

}
