package com.freshdirect.fdstore;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;

import com.freshdirect.framework.core.ServiceLocator;

import java.util.concurrent.ConcurrentHashMap;

public abstract class FDAbstractCache<K,V> {
	
	protected final ServiceLocator serviceLocator;
	
	private final long refreshDelay;
	private final Thread refresher;
	
	private Map<K,V> cache = new ConcurrentHashMap<K,V>();
	private Date lastMaxModifiedDate;
	
	public FDAbstractCache(long refreshDelay) {
		this.refreshDelay = refreshDelay;
		this.lastMaxModifiedDate = new Date(0);
		refresher = new RefreshThread(this.getClass().getName());
		refresher.start();
		try{
			this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
			this.refresh();
		} catch (NamingException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	private synchronized void refresh() {
		
		Map<K,V> m = loadData(lastMaxModifiedDate);
		if (!m.isEmpty()) {
			long maxDate = lastMaxModifiedDate.getTime();
			for (Iterator<V> i = m.values().iterator(); i.hasNext(); ) {
				Date d = getModifiedDate(i.next());
				maxDate = Math.max(maxDate, d.getTime());
			}
			this.cache.putAll(m);
			lastMaxModifiedDate = new Date(maxDate);
		}
	}
	
	protected abstract Map<K,V> loadData(Date since); 
	
	protected abstract Date getModifiedDate(V item);
	
	protected V getCachedItem(K key) {
		return this.cache.get(key);
	}
	
	protected Map<K,V> getCache() {
		return this.cache;
	}
	
	private final class RefreshThread extends Thread {
	        public RefreshThread(String name) {
    	            super(name);
                }
	        
		public void run() {
			try {
				
				while(true) {
					Thread.sleep(refreshDelay);
					refresh();
				}

			} catch (InterruptedException ex) {}
		}
	}

}
