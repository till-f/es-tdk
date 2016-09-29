package fzi.mottem.runtime.problems;

import org.eclipse.swt.graphics.Color;

public abstract class ProblemEvent {

	private String date;
	private String message;
	private int offset;
	private int length;
	private int line;
	private String filepath;
	private Color clr;
	private int arrivalInd;
	private String type;
	
	public ProblemEvent(String date, int arrivalInd, String filepath, int offset, int length, int line, String message, Color clrnum, String type) {
		this.date = date;
		this.arrivalInd = arrivalInd;
		this.filepath = filepath;
		this.offset = offset;
		this.length = length;
		this.line = line;
		this.message = message;
		this.type = type;
		this.clr = clrnum;
	}
	
	public String getDate() {
		return date;
	}
	public String getMessage() {
		return message;
	}
	public int getOffset() {
		return offset;
	}
	public int getLength() {
		return length;
	}
	public int getLine() {
		return line;
	}
	public String getFilepath() {
		return filepath;
	}
	public Color getClr() {
		return clr;
	}

	public int getArrivalInd() {
		return arrivalInd;
	}

	public void setArrivalInd(int arrivalInd) {
		this.arrivalInd = arrivalInd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
}
