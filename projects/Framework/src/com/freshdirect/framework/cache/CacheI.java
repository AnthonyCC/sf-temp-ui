package com.freshdirect.framework.cache;

import java.io.Serializable;


/**
 * Generic key-value object cache interface. Implementors are expected to
 * provide thread-safe cache implementations.
 */
public interface CacheI<K extends Serializable,V> {
	
	/**
	 * Retrieve a keyed object from cache.
	 * 
	 * @param key 
	 * @return the cached object, or null if not found
	 */
	public V get(K key);

	/**
	 * Store a keyed object in cache.
	 * 
	 * @param key
	 * @param object
	 */
	public void put(K key, V object);

	/**
	 * Evict the object for a given key from cache.
	 * 
	 * @param key
	 */
	public void remove(K key);

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
