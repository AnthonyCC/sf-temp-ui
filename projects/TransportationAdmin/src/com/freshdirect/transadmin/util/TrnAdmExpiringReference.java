package com.freshdirect.transadmin.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for caching a single object, with timed expiration and appropriate synchronization. 
 */
public abstract class TrnAdmExpiringReference {

	private final long refreshPeriod;
	private long lastRefresh = 0;
	private Map referentHolder=new HashMap();
	private Map referentMap=new HashMap();

	/**
	 * @param refreshPeriod in milliseconds
	 */
	public TrnAdmExpiringReference(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public synchronized Object get(Object key) {
		
		
		//System.out.println("getting the key in expiring reference : "+key);						
		
		Set keySet=referentMap.keySet();
		Iterator iterator=keySet.iterator();
		while(iterator.hasNext()){
			Object time=(Object)iterator.next();
			Long lastRefreshTime=(Long)referentMap.get(time);
			
			if (System.currentTimeMillis() - lastRefreshTime.longValue() > this.refreshPeriod) {
				
				//System.out.println("removing the cache content :"+time);
				
				iterator.remove();
				 referentHolder.remove(time);
			}
			
		}
		
		//System.out.println("Map size : "+referentMap.size());		
		
		Long refreshObj=(Long)referentMap.get(key);
		
		if(refreshObj!=null){
		   lastRefresh=refreshObj.longValue();
		   
			if (System.currentTimeMillis() - lastRefresh > this.refreshPeriod) {			
				Object value = this.load(key);
				referentHolder.put(key,value);

				lastRefresh = System.currentTimeMillis();
				referentMap.put(key, new Long(lastRefresh));
				
				return value;
				
			}else{
				
				return referentHolder.get(key);
			}
		   
		}else{
			
			Object value = this.load(key);
			referentHolder.put(key,value);

			lastRefresh = System.currentTimeMillis();
			referentMap.put(key, new Long(lastRefresh));
			return value;
		}				
	}

	/** Template method that gets invoked whenever the reference has to be loaded */
	protected abstract Object load(Object requestParam);

	public synchronized void forceRefresh() {
		referentHolder=new HashMap();
		referentMap=new HashMap();
	}
	
}

