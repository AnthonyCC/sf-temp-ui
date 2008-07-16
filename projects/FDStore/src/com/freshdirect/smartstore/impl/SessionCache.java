package com.freshdirect.smartstore.impl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Session cache.
 * 
 * This implementation is an access-ordered LRU cache,
 * which purges the least recently accessed item when
 * the cache size reaches the given capacity.
 * 
 * @author istvan
 *
 */
public class SessionCache extends LinkedHashMap {

	private static final long serialVersionUID = 6732419652977205549L;
	
	/**
	 * Actual capacity limit.
	 */
	protected int capacity;
	
	/**
	 * Constructor.
	 * 
	 * Creates a cache for 100 entries.
	 */
	public SessionCache() {
		this(100, 0.75f);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param capacity cache capacity
	 * @param loadFactor load factor
	 */
	public SessionCache(int capacity, float loadFactor) {
		super(3*capacity/2+1,loadFactor,true);
		this.capacity = capacity;
	}
	
	/**
	 * Decide whether to remove entry.
	 * @param e ignored (the entry that gets potentially removed)
	 * @return whether the number of entries has reached the capacity
	 */
	protected boolean removeEldestEntry(Map.Entry e) {
		return size() > capacity;
	}
	
	
	/**
	 * Retrieve object.
	 *  
	 * @param key 
	 * @return object stored under key or null
	 */
	public synchronized Object get(Object key) { 
		return super.get(key);
	}
	
	/**
	 * Cache object.
	 * @param key 
	 * @param value
	 */
	public synchronized Object put(Object key, Object value) {
		return super.put(key, value);
	}
	

}
