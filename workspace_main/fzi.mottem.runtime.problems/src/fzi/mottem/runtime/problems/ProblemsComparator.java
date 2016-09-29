package fzi.mottem.runtime.problems;

import java.util.Comparator;

class ProblemsComparator implements Comparator<Object> {
	
	  public static final int DATE = 0;

	  public static final int MESSAGE = 1;

	  public static final int SEVERITY = 2;
	  
	  public static final int FILEPATH = 3;
	  
	  public static final int LINENUMBER = 4;

	  public static final int OFFSET = 5;

	  public static final int LENGTH = 6;
	  
	  /** Constant for ascending */
	  public static final int ASCENDING = 0;

	  /** Constant for descending */
	  public static final int DESCENDING = 1;

	  private int column;
	  private int direction;

	  /**
	   * Compares two Player objects
	   * 
	   * @param obj1 the first Player
	   * @param obj2 the second Player
	   * @return int
	   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	   */
	  public int compare(Object obj1, Object obj2) {
	    int rc = 0;
	    ProblemEvent p1 = (ProblemEvent) obj1;
	    ProblemEvent p2 = (ProblemEvent) obj2;

	    // Determine which field to sort on, then sort
	    // on that field
	    switch (column) {
	    case DATE:
	      rc = p1.getDate().compareTo(p2.getDate());
	      if (rc == 0 ) {
	    	  rc = (p1.getArrivalInd() < p2.getArrivalInd()) ? -1 : 1;
	      }
	      break;
	    case MESSAGE:
	      rc = p1.getFilepath().compareTo(p2.getFilepath());
	      break;
	    case SEVERITY:
	      rc = (p1.getLine() < p2.getLine()) ? -1 : 1;
	      break;
	    case FILEPATH:
	      rc = p1.getMessage().compareTo(p2.getMessage());
	      break;
	    case LINENUMBER: 
	    	rc = p1.getType().compareTo(p2.getType());
	    	break;
	    }

	    // Check the direction for sort and flip the sign
	    // if appropriate
	    if (direction == DESCENDING) {
	      rc = -rc;
	    }
	    return rc;
	  }

	  /**
	   * Sets the column for sorting
	   * 
	   * @param column the column
	   */
	  public void setColumn(int column) {
	    this.column = column;
	  }

	  /**
	   * Sets the direction for sorting
	   * 
	   * @param direction the direction
	   */
	  public void setDirection(int direction) {
	    this.direction = direction;
	  }

	  /**
	   * Reverses the direction
	   */
	  public void reverseDirection() {
	    direction = 1 - direction;
	  }
	}
