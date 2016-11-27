package com.freshdirect.transadmin.cache;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;


public abstract class BaseAsyncCache<K, V> {
	
	private LoadingCache<K, V> cache;
	private long lastRefresh = 0;
	private ExecutorService executor;
	private String cacheIdentifier;
	private final static Category LOGGER = LoggerFactory.getInstance(BaseAsyncCache.class);
	
	public BaseAsyncCache(int refreshFrequency, String cacheIdentifier) {
		this(cacheIdentifier, 100000, refreshFrequency, 1);
	}
	
	public BaseAsyncCache(String cacheIdentifier, int maxSize
								, int refreshFrequency
								, int reloadThreadPoolSize) {
		super();
		this.cacheIdentifier = cacheIdentifier;
		executor = Executors.newFixedThreadPool(reloadThreadPoolSize);
		initCache(maxSize, refreshFrequency);
	}
	
	private void initCache(int maxSize, int refreshFrequency) {
		
		cache = CacheBuilder.newBuilder()
	       .maximumSize(maxSize)
	       .refreshAfterWrite(refreshFrequency, TimeUnit.MILLISECONDS)
	       .build(
	    		   new CacheLoader<K, V>() {
	    			   // Method for Initial Load
	    			   public V load(K key) {
	    				   V result = null;  // Never return null to Guava
	    				   try {
	    					   LOGGER.info("START TO LOAD CACHE "+getCacheIdentifier()+" for key "+key);
	    					   lastRefresh = System.currentTimeMillis();
	    					   //throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
	    					   result = loadData(key);
						   } catch (AsyncCacheException e) {
								Map<K, V> fileStoreBackup = readFromStore();
								if(fileStoreBackup != null) {
									result = fileStoreBackup.get(key);
								} 
						  }
						   if(result == null) {
							   result = loadDefault(key);
						   }
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
	    							   writeToStore(); // Let backup data before reload
	    							   LOGGER.info("START TO RE-LOAD CACHE "+getCacheIdentifier());
	    							   lastRefresh = System.currentTimeMillis();
	    							   result = loadData(key);
	    						   } catch (AsyncCacheException e) {
	    							   result =  prevGraph;
	    						   }
	    						   if(result == null) {
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
			return null;
		}
	}
	
	//Used to write file backup of cache data
	private void writeToStore() {
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(getCacheIdentifier()));
			HashMap<K, V> offlineData = new HashMap<K, V>();
			offlineData.putAll(cache.asMap());			
			oos.writeObject(offlineData);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if(oos != null) {
				try {
					oos.close();
				} catch (Exception e) {
					LOGGER.error("Unable to close writeToStore Stream for:"+getCacheIdentifier());
				}
			}
		}
	}
	
	//Used to read from file backup of cache data
	private Map<K, V> readFromStore() {
		
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(getCacheIdentifier()));
			return (Map<K, V>) ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(ois != null) {
				try {
					ois.close();
				} catch (Exception e) {
					LOGGER.error("Unable to close readFromStore Stream for:"+getCacheIdentifier());
				}
			}
		}
		return null;
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

	public String getCacheIdentifier() {
		return cacheIdentifier;
	}
	
	public long getLastRefresh() {
		return System.currentTimeMillis() - lastRefresh;
	}
		
}
