package com.freshdirect.framework.cache;

import java.io.IOException;
import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * {@link com.freshdirect.framework.cache.CacheI} implementation using EHCache.
 * Configuration is handled via a {@link net.sf.ehcache.CacheManager} instance.
 * See:
 * <blockquote><pre>
 *     <a href="http://ehcache.sourceforge.net/">http://ehcache.sourceforge.net/</a>
 * </pre></blockquote> 
 */
public class EHCache<K extends Serializable,V extends Serializable> implements CacheI<K,V> {

	public static final int DEFAULT_CACHE_SIZE = 5000;
	public static final long DEFAULT_TTL = 3600;
	public static final long DEFAULT_TTI = 3600;
	
	private Cache cache;
	private String cacheName;
	private int cacheSize = DEFAULT_CACHE_SIZE;
	private long timeToLiveSeconds = DEFAULT_TTL;
	private long timeToIdleSeconds = DEFAULT_TTI;

	@SuppressWarnings( "unchecked" )
	public V get(K key) {
		try {
			Element e = getCache().get(key);
			return e == null ? null : (V)e.getValue();
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	public void put(K key, V object) {
		Element e = new Element(key, object);
		getCache().put(e);
	}

	public void remove(K key) {
		getCache().remove(key);
	}

	public void clear() {
		try {
			getCache().removeAll();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized Cache getCache() {
		if (cache == null) {
			cache = createCacheInstance();
		}
		return cache;
	}
	
	private Cache createCacheInstance() {
		try {
			CacheManager manager = CacheManager.create();
			Cache newCache = new Cache(getName(), 
							  getLruCacheSize(), 
							  false,
							  false,
							  getTimeToLiveSeconds(),
							  getTimeToIdleSeconds()
							  );
			manager.addCache(newCache);
			return newCache;
		} catch (CacheException e) {
		}
		return null;
	}

	private long getTimeToIdleSeconds() {
		return timeToIdleSeconds;
	}

	public synchronized void setTimeToIdleSeconds(long timeToIdleSeconds) {
		assert (cache == null);
		this.timeToIdleSeconds = timeToIdleSeconds;
	}

	public long getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}
	
	public synchronized void setTimeToLiveSeconds(long timeToLiveSeconds) {
		assert (cache == null);
		this.timeToLiveSeconds = timeToLiveSeconds;
	}

	public synchronized void setLruCacheSize(int cacheSize) {
		assert (cache == null);
		this.cacheSize = cacheSize;
	}

	public int getLruCacheSize() {
		return cacheSize;
	}

	public String getName() {
		return cacheName;
	}
	
	public synchronized void setName(String cacheName) {
		assert (cache == null);
		this.cacheName = cacheName;
	}

	public int size() {		
		return -1;
	}
	
}
