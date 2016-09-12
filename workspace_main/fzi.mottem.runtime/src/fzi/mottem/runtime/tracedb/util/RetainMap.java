/**
 * 
 */
package fzi.mottem.runtime.tracedb.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author deuchler
 * 
 * A map that retains the first inserted key after calls to clear() even if that key has been overridden with a new value 
 *
 */
public  class RetainMap<K, V> implements Map<K, V> 
{
	private HashMap<K, V> map = new HashMap<K, V>(); // saves the last state of a variable
	private HashMap<K, V> rMap = new HashMap<K, V>(); // saves the state of the 

	public RetainMap() 
	{
		
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty() && rMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key) || rMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value) || rMap.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return map.containsKey(key) ? map.get(key) : rMap.get(key); 
	}

	@Override
	public V put(K key, V value) {
		if(!rMap.containsKey(key))
		{
			return rMap.put(key, value);
		}
		else
		{
			return map.put(key, value);
		}
	}

	@Override
	public V remove(Object key) {
		return map.remove(key);
	}
	
	@Override
	public void clear() {
		map.clear();		
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		System.err.println(this.getClass().getName() + "$putAll implementationStub!");
	}

	@Override
	public Set<K> keySet() {
		System.err.println(this.getClass().getName() + "$keySet implementationStub!");
		return null;
	}

	@Override
	public Collection<V> values() {
		System.err.println(this.getClass().getName() + "$values implementationStub!");
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		System.err.println(this.getClass().getName() + "$ entrySet implementationStub!");
		return null;
	}
	
	@Override
	public int size() {
		System.err.println(this.getClass().getName() + "$size implementationStub!");
		return 0;
//		@SuppressWarnings("unchecked")
//		Map<K,V> cl = (Map<K, V>) map.clone();
//		cl.putAll(cl);
//		return cl.size();
	}

}
