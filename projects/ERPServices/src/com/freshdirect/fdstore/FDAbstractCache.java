package com.freshdirect.fdstore;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;

import com.freshdirect.framework.core.ServiceLocator;

import java.util.concurrent.ConcurrentHashMap;

public abstract class FDAbstractCache {
	
	protected final ServiceLocator serviceLocator;
	
	private final long refreshDelay;
	private final Thread refresher;
	
	private Map cache = new ConcurrentHashMap();
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
	
	private  synchronized void refresh() {
		
		Map m = loadData(lastMaxModifiedDate);
		if (!m.isEmpty()) {
			long maxDate = lastMaxModifiedDate.getTime();
			for (Iterator i = m.values().iterator(); i.hasNext(); ) {
				Date d = getModifiedDate(i.next());
				maxDate = Math.max(maxDate, d.getTime());
			}
			this.cache.putAll(m);
			lastMaxModifiedDate = new Date(maxDate);
		}
	}
	
	protected abstract Map loadData(Date since); 
	
	protected abstract Date getModifiedDate(Object item);
	
	protected Object getCachedItem(Object key) {
		return this.cache.get(key);
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
