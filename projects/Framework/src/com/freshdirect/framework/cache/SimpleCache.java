package com.freshdirect.framework.cache;

import java.io.Serializable;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

/**
 * Basic {@link com.freshdirect.framework.cache.CacheI} implementation
 * using a synchronized HashMap. Objects never automatically expire.
 */
public class SimpleCache implements CacheI {

	private ConcurrentHashMap map;
	private String name;

	
	public SimpleCache() {
		super();
		map = new ConcurrentHashMap();
	}
	
	public Object get(Serializable key) {
		return map.get(key);
	}


	public void put(Serializable key, Object object) {		
		map.put(key, object);		
	}

	public void remove(Serializable key) {
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
