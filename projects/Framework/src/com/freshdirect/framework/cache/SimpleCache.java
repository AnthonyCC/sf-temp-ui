package com.freshdirect.framework.cache;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic {@link com.freshdirect.framework.cache.CacheI} implementation
 * using a synchronized HashMap. Objects never automatically expire.
 */
public class SimpleCache<K extends Serializable, V> implements CacheI<K, V> {

	private Map<K,V> map;
	private String name;

	
	public SimpleCache() {
		super();
		map = new ConcurrentHashMap<K,V>();
	}

        protected SimpleCache(Map<K, V> map, String name) {
            super();
            this.map = map;
            this.name = name;
        }
	
	public V get(K key) {
		return map.get(key);
	}


	public void put(K key, V object) {		
		map.put(key, object);		
	}

	public void remove(K key) {
		map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	protected String getMBeanName() throws Exception {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int size() {
		return map.size();
	}

}
