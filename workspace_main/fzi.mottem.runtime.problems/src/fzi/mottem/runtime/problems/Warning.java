package fzi.mottem.runtime.problems;

import org.eclipse.swt.graphics.Color;

public class Warning extends ProblemEvent {

	public Warning(String date, int index, String filepath, int offset, int length, int line, String message) {
		super(date, index, filepath, offset, length, line, message, new Color(null, 255,150,0), "warning");
		
	}

}
