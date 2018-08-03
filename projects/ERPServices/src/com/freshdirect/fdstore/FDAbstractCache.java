package com.freshdirect.fdstore;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.ServiceLocatorI;
import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class FDAbstractCache<K,V> {
	private static Category LOGGER = LoggerFactory.getInstance(FDAbstractCache.class);
	protected ServiceLocatorI serviceLocator;
	
	private final long refreshDelay;
	private final Thread refresher;
	
	private Map<K,V> cache = new ConcurrentHashMap<K,V>();
	protected Date lastMaxModifiedDate;

	public FDAbstractCache(long refreshDelay) {
		if(FDStoreProperties.isLocalDeployment()) {
			this.lastMaxModifiedDate = new Date();			
			refreshDelay = FDStoreProperties.TEN_DAYS_IN_MILLIS; //ten days
		} else {
			this.lastMaxModifiedDate = new Date(0);
		}
		this.refreshDelay = refreshDelay;
		
		refresher = new RefreshThread(this.getClass().getName());
		refresher.start();
		
		try{
			try{
				this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
			}catch (Exception e) {
				if("2".equals(JMXUtil.getStorefrontVersion())) {
					LOGGER.info("SKIP EJB SERVICELOCATER");
				}else{
					throw new FDRuntimeException(e);
				}
			}
			this.refresh();
		} catch (Exception e) {
			throw new FDRuntimeException(e);
		}		
	}
	
	protected synchronized void refresh() {
		
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
    	            this.setDaemon(true);
                }
	        
		@Override
		public void run() {
			try {
				
				while(true) {
					Thread.sleep(refreshDelay);
					try {
						refresh();
					} catch (Exception e) {
						LOGGER.warn("Failed to refresh due to " + e);
						//do nothing - keep the thread running
					}
				}

			} catch (InterruptedException ex) {
				LOGGER.warn("RefreshThread is stopped, as it failed to refresh due to " + ex);
				// do nothing
			} catch (Exception e){
				LOGGER.warn("RefreshThread is stopped, as it failed to refresh due to " + e);
				//do nothing				
			}
		}
	}

}
