package com.freshdirect.framework.cache;

import java.io.Serializable;

import com.freshdirect.framework.util.LruCache;
import com.freshdirect.framework.util.TimedLruCache;

/**
 * Basic {@link com.freshdirect.framework.cache.CacheI} implementation
 * using a synchronized HashMap. Objects never automatically expire.
 */
public class SimpleLruCache implements CacheI {

	private LruCache cache = null;

	private int timeout;
	
	private int capacity;

	private String name;
	
	public SimpleLruCache() {
	}
	
	private synchronized LruCache getCache() {
		if (cache == null) {
			cache = createCacheInstance();
		}
		return cache;
	}
	
	private LruCache createCacheInstance() {
		if (getTimeout() <= 0) {
			return new LruCache(getCapacity());
		} else {
			return new TimedLruCache(getCapacity(), getTimeout()*1000); // Timeout has to be passed in milliseconds. 
		}
	}

	public Object get(Serializable key) {
		return getCache().get(key);
	}

	public void put(Serializable key, Object object) {
		getCache().put(key, object);
	}

	public void remove(Serializable key) {
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
