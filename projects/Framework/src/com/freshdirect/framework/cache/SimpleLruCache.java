package com.freshdirect.framework.cache;

import java.io.Serializable;

import com.freshdirect.framework.util.LruCache;
import com.freshdirect.framework.util.TimedLruCache;

/**
 * Basic {@link com.freshdirect.framework.cache.CacheI} implementation
 * using a synchronized HashMap. Objects never automatically expire.
 */
public class SimpleLruCache<K extends Serializable,V> implements CacheI<K,V> {

	private LruCache<K,V> cache = null;

	private int timeout;
	
	private int capacity;

	private String name;
	
	public SimpleLruCache() {
	}
	
	private synchronized LruCache<K,V> getCache() {
		if (cache == null) {
			cache = createCacheInstance();
		}
		return cache;
	}
	
	private LruCache<K,V> createCacheInstance() {
		if (getTimeout() <= 0) {
			return new LruCache<K,V>(getCapacity());
		} else {
			return new TimedLruCache<K,V>(getCapacity(), getTimeout()*1000); // Timeout has to be passed in milliseconds. 
		}
	}

	public V get(K key) {
		return getCache().get(key);
	}

	public void put(K key, V object) {
		getCache().put(key, object);
	}

	public void remove(K key) {
		getCache().remove(key);
	}

	public void clear() {
		getCache().clear();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int size() {
		return cache.size();
	}

}
