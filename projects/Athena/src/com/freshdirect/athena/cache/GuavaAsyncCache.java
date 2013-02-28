package com.freshdirect.athena.cache;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.freshdirect.athena.common.SystemMessageManager;
import com.freshdirect.athena.exception.AsyncCacheException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;


public abstract class GuavaAsyncCache<K, V> {
	
	private LoadingCache<K, V> cache;
	private long lastRefresh = 0;
	private ExecutorService executor;
	private String cacheIdentifier;
	
	private static final Logger LOGGER = Logger.getLogger(GuavaAsyncCache.class);
	
	public GuavaAsyncCache(int refreshFrequencyMilliSecond, String cacheIdentifier) {
		this(cacheIdentifier, 100000, refreshFrequencyMilliSecond, 1);
	}
	
	public GuavaAsyncCache(String cacheIdentifier, int maxSize
								, int refreshFrequencyMilliSecond
								, int reloadThreadPoolSize) {
		super();
		this.cacheIdentifier = cacheIdentifier;
		executor = Executors.newFixedThreadPool(reloadThreadPoolSize);
		initCache(maxSize, refreshFrequencyMilliSecond);
	}
	
	private void initCache(int maxSize, int refreshFrequencyMilliSecond) {
		
		cache = CacheBuilder.newBuilder()
			.weakKeys()
	       .maximumSize(maxSize)
	       .refreshAfterWrite(refreshFrequencyMilliSecond, TimeUnit.MILLISECONDS)
	       .build(
	    		   new CacheLoader<K, V>() {
	    			   // Method for Initial Load
	    			   public V load(K key) {
	    				   V result = null;  // Never return null to Guava
	    				   try {
	    					   LOGGER.info("START TO LOAD "+getCacheIdentifier()+ " "+key);
	    					   lastRefresh = System.currentTimeMillis();
	    					   SystemMessageManager.getInstance().addMessage("Start Cache load for "+getCacheIdentifier());
	    					   result = doLoad(key);	    					   
						   } catch (AsyncCacheException e) {
							   e.printStackTrace();								
						  }
						   if(result == null) {
							   LOGGER.info("LOAD DEFAULT "+getCacheIdentifier()+ " "+key);
							   result = loadDefault(key);
						   }
						   return result;
	    			   }
	    			   
	    			   private V doLoad(K key) throws AsyncCacheException {
	    				   long startTime = System.currentTimeMillis();
    					   V result = loadData(key);
    					   long endTime = System.currentTimeMillis();
    					   LOGGER.debug("END LOAD :" + getCacheIdentifier()+ " "+key + " completed in " + (endTime - startTime) + " milliseconds");
    					   SystemMessageManager.getInstance().addMessage("Cache load for "+getCacheIdentifier() 
    							   						+ " took " + (endTime - startTime) + " milliseconds");
    					   return result;
	    			   }
	    			   
	    			   // Method for Asynchronous Load
	    			   public ListenableFuture<V> reload(final K key, final V prevGraph) {
	    				   
	    				   ListenableFutureTask<V> task = ListenableFutureTask
	    				   .create(new Callable<V>() {
	    					// Method for Async Re-Load
	    					   public V call() {
	    						   V result = null; // Never return null to Guava
	    						   try {
	    							  
	    							   LOGGER.info("START TO RE-LOAD "+getCacheIdentifier()+ " "+key);
	    							   SystemMessageManager.getInstance().addMessage("Start Cache reload for "+getCacheIdentifier()+ " "+key);
	    							   result = doLoad(key);
	    							   lastRefresh = System.currentTimeMillis();
	    							   
	    						   } catch (AsyncCacheException e) {
	    							   e.printStackTrace();
	    							   result =  prevGraph;
	    						   }
	    						   if(result == null) {
	    							   LOGGER.info("LOAD DEFAULT "+getCacheIdentifier()+ " "+key);
	    							   result = loadDefault(key);
	    						   }
	    						   
	    						   return result;
	    					   }
	    				   });
	    				   executor.execute(task);
	    				   return task;
	    			   }
	    		   });
	}
	
	
	protected abstract V loadData(K key) throws AsyncCacheException;
	protected abstract V loadDefault(K key);
	
	public V get(K key) {
		try {
			return cache.get(key);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public void forceRefresh() {
		Set<K> cacheKeys = cache.asMap().keySet();
		if(cacheKeys != null) {
			for(K key : cacheKeys) {
				cache.refresh(key);
			}
		}
		this.cache.invalidateAll();		
	}
	
	public void invalidateAll() {
		this.cache.invalidateAll();	
	}

	public String getCacheIdentifier() {
		return cacheIdentifier;
	}
	
	public long getLastRefresh() {
		return System.currentTimeMillis() - lastRefresh;
	}
		
}
