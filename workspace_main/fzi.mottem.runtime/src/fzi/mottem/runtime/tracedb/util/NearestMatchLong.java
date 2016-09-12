/**
 * 
 */
package fzi.mottem.runtime.tracedb.util;

/**
 * @author deuchler
 * 
 * fix this class, causes heap overflow
 * 
 * find nearest Longeger, based on 
 * http://stackoverflow.com/questions/6599371/java-method-to-find-nearest-match-to-a-given-no-in-unsorted-array-of-Longegers
 *
 */
public class NearestMatchLong 
{
	
	/**
	 * same as above, for Iterable types
	 * @param find
	 * @param integer
	 * @return
	 */
	public static Double closest2(Double find, Iterable<Double> iterable)
	{
		Double closest = iterable.iterator().next();
		Double distance = Math.abs(closest - find);
	    for(Double i : iterable) {
	    	Double distanceI = Math.abs(i - find);
	       if(distance > distanceI) {
	           closest = i;
	           distance = distanceI;
	       }
	    }
	    return closest;		
	}
	
	
	/**
	 * test method
	 * @param s
	 */
	public static void main(String[] s)
	{
	}

}
