package com.freshdirect.framework.cache;

import java.io.Serializable;


/**
 * Generic key-value object cache interface. Implementors are expected to
 * provide thread-safe cache implementations.
 */
public interface CacheI {
	
	/**
	 * Retrieve a keyed object from cache.
	 * 
	 * @param key 
	 * @return the cached object, or null if not found
	 */
	public Object get(Serializable key);

	/**
	 * Store a keyed object in cache.
	 * 
	 * @param key
	 * @param object
	 */
	public void put(Serializable key, Object object);

	/**
	 * Evict the object for a given key from cache.
	 * 
	 * @param key
	 */
	public void remove(Serializable key);

	/**
	 * Evict all objects from cache.
	 */
	public void clear();
	
	/**
	 * Returns the number of elements in the cache
	 * @throws UnsupportedOperationException if the cache is not capable of determining its size
	 */
	public int size();
	
	/**
	 * A unique name for the cache.
	 */
	public String getName();
	
}
