/**
 * 
 */
package com.freshdirect.smartstore.fdstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class OnlineScoreCache {
	private static final Logger LOGGER = LoggerFactory.getInstance(OnlineScoreCache.class);

	private static final int HOUR_IN_MILLIS = 60 * 60 * 1000;
    
	private static Executor cachingStoreLookupThreadPool = new ThreadPoolExecutor(1, 1, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadPoolExecutor.DiscardPolicy());
	
    private Object lock = new Object();

    private class Loader implements Runnable {
		public void run() {
            Set rss = CmsManager.getInstance().getContentKeysByType(ContentType.get("Product"));
            LOGGER.info(OnlineScoreCache.this.getClass().getName()
            		+ ": reloading score cache for " + rss.size() + " products");
            
        	Iterator it = rss.iterator();
        	while (it.hasNext()) {
        		ContentKey key = (ContentKey) it.next();
        		ContentNodeModel contentNode = 
        				(ContentNodeModel) ContentFactory.getInstance().getContentNode(key.getId());
    	        double result = calculateVariable(contentNode);
   	        	cache.put(contentNode.getContentKey().getId(), new Double(result));
        	}
            LOGGER.info(OnlineScoreCache.this.getClass().getName()
            		+ ": reloaded score cache for " + rss.size() + " products");
		}
    }
    
    volatile long nextRuntime = Long.MIN_VALUE; 
    
    Map cache = Collections.synchronizedMap(new HashMap(20000));
    
	boolean cacheEnabled;

	public OnlineScoreCache() {
		cacheEnabled = FDStoreProperties.isSmartstoreOnlineFactorsCached();
	}

    public void reload() {
		if (!(cacheEnabled = FDStoreProperties.isSmartstoreOnlineFactorsCached()))
			return;

		synchronized (lock) {	
			if (System.currentTimeMillis() > nextRuntime) {
				nextRuntime = System.currentTimeMillis() + HOUR_IN_MILLIS;
		    	new Loader().run();
			}
		}
    }
    
	public final double getVariable(ContentNodeModel contentNode) {
		if (!cacheEnabled)
			return calculateVariable(contentNode);
		
		synchronized (lock) {
			if (System.currentTimeMillis() > nextRuntime) {
				nextRuntime = System.currentTimeMillis() + HOUR_IN_MILLIS;
		    	cachingStoreLookupThreadPool.execute(new Loader());
			}
		}
			
        Object number = cache.get(contentNode.getContentKey().getId());
        if (number instanceof Number) {
            return ((Number) number).doubleValue();
        }
        double result = calculateVariable(contentNode);
        cache.put(contentNode.getContentKey().getId(), new Double(result));
        return result;
    }

    public abstract double calculateVariable(ContentNodeModel contentNode);
}