/**
 * 
 */
package fzi.mottem.runtime.tracedb.util;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author deuchler
 * 
 *  a map implementation that only allows increasing keys by automatically adding the first 
 *  key that is smaller than the highest key to the highest key. All subsequent inserted values
 *  that are higher than the last inserted key, are then added to to this highest key.  
 *  
 *  the highest key persists even after calls to clear()
 */
public class IncreaseKeyMap<V> extends TreeMap<Long, V> {

	private Long peakKey = null;
	private static final long serialVersionUID = -2483093777790880394L;

	public IncreaseKeyMap() 
	{
		
	}
	
	@Override
	public	V	put(Long key, V value)
	{
		if(peakKey == null)
			peakKey = key;
		
		if(isEmpty())
		{
			return safePut(key >= peakKey ? key : peakKey + key, value);
		}
		
		if(key >= this.lastKey())
		{
			peakKey = key;
			return safePut(key, value);
		}
		
		if(key < lastKey() && ((key+peakKey)  > lastKey())) // new key greater than last inserted key
		{
			return safePut(key + peakKey, value);	 // does not work after clear()
		}
		else if(key < lastKey() && ((key+peakKey)  < lastKey()))
		{
			peakKey = lastKey();
			return safePut(key + peakKey, value);	
		}
		return safePut(key >= lastKey() ? key : lastKey() + key, value); // fallback, should not happen
	}
	
	@Override
	public void clear()
	{
		peakKey = lastKey();
		super.clear();
	}
	
	private V safePut(Long key, V value)
	{
		if(!isEmpty() && key < lastKey())
		{
			System.err.println("Error putting value");
		}
		
		return super.put(key, value);
	}
	
	public void	putAll(Map<? extends Long,? extends V> map)
	{
		throw new UnsupportedOperationException();
	}

}
