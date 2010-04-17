package com.freshdirect.framework.cache;

import java.io.Serializable;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ManagedCache<K extends Serializable,V> implements CacheI<K,V>, ManagedCacheMBean {

	private static final Category LOGGER = LoggerFactory.getInstance(ManagedCache.class);

	CacheI<K,V> cache;

	int numOfNulls = 0;
	final Object numOfNullsMutex = new Object();
	
	long numOfHits = 0;
	final Object numOfHitsMutex = new Object();
	
	long numOfMisses = 0;
	final Object numOfMissesMutex = new Object();
	
	long numOfNullHits = 0;
	final Object numOfNullHitsMutex = new Object();
		
	private boolean nullElementStats = false;
	private V nullElement;

	private boolean countElems = true;
	
	public ManagedCache(String type, CacheI<K,V> cache) {
		this(type, cache, null);
	}
	
	public ManagedCache(String type, CacheI<K,V> cache, V nullElement) {
		assert (cache != null);
		this.cache = cache;
		this.nullElement = nullElement;
		if (JMXUtil.registerMBean(this, type, cache.getName()) == null) {
			LOGGER.warn("Unable to register MBean for cache "+type+", "+cache.getName());
		}
	}
	
	public void clear() {
		if (nullElementStats) {
			synchronized (numOfNullsMutex) {
				numOfNulls = 0;
			}
		}
		cache.clear();		
	}

	public String getName() {
		return cache.getName();
	}

	public V get(K key) {
		V result = cache.get(key);
		updateGetStats(key, result);
		return result;
	}

	public void put(K key, V object) {
		if (nullElementStats) {	
			synchronized (this) {
				increaseNullsIfNullObject(object);		
				decreaseNullsIfNullObject(key); // Decrease the number of NULL objects if one is going to be replaced 
				cache.put(key, object);
			} 
		} else {
			cache.put(key, object);
		}
		
	}

	public void remove(K key) {
		if (nullElementStats) {
			synchronized (this) {
				decreaseNullsIfNullObject(key); // Decrease the number of NULL objects if one is going to be removed
				cache.remove(key);
			}			
		} else {
			cache.remove(key);
		}
	}

	public int size() {
		return cache.size();
	}	

	public int getCacheElements() {
		if (countElems ) {
			try {
				return cache.size();
			} catch (UnsupportedOperationException e) {
				countElems = false;
				return -1;			
			}
		}
		return -1;
	}

	public int getCacheNullElements() {
		return nullElementStats ? numOfNulls : -1;
	}

	public long getCacheHits() {
		return numOfHits;
	}

	public long getCacheMisses() {
		return numOfMisses;
	}

	public long getCacheNullHits() {
		return nullElementStats ? numOfNullHits : -1;
	}
	
	public long getCacheQueries() {
		return numOfHits + numOfMisses + numOfNullHits;
	}

	public void clearCache() {
		cache.clear();		
	}

	public synchronized void clearStats() {
		synchronized (numOfHitsMutex) {
			numOfHits = 0;
		}
		synchronized (numOfNullHitsMutex) {
			numOfNullHits = 0;
		}
		synchronized (numOfMissesMutex) {
			numOfMisses = 0;
		}
	}

	public synchronized void startNullElementStats() {
		if (nullElement != null) {
			synchronized (numOfNullsMutex) {
				numOfNulls = 0;
			}
			synchronized (numOfNullHitsMutex) {
				numOfNullHits = 0;
			}
			nullElementStats = true;					
		}
	}

	public synchronized void stopNullElementStats() {
		if (nullElement != null) {
			nullElementStats = false;
		}
	}

	public boolean isNullElementStats() {
		return nullElementStats;
	}

	private void updateGetStats(K key, V result) {
		if (result == null) {
			synchronized (numOfMissesMutex) {
				numOfMisses ++;
			}
		} else if (result == nullElement && nullElementStats) {
			synchronized (numOfNullHitsMutex) {
				numOfNullHits ++;
			}
		} else {
			synchronized (numOfHitsMutex) {
				numOfHits ++;
			}
		}
	}

	private void decreaseNullsIfNullObject(K key) {
		Object o = cache.get(key);
		if (o == nullElement) {
			synchronized (numOfNullsMutex) {
				numOfNulls--;
			}
		}
	}

	private void increaseNullsIfNullObject(V object) {
		if (object == nullElement) {
			synchronized (numOfNullsMutex) {
				numOfNulls++;
			}
		}
	}
}
