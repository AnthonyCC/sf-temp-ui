package com.freshdirect.fdstore.content;

import java.net.URL;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class QuickShopCacheUtil {
	
	private static final String EHCACHE_CONFIG_LOCATION = "ehcache.xml";
	public static final String PAST_ORDERS_CACHE_NAME = "pastOrdersCache";
	public static final String SHOP_FROM_LISTS_CACHE_NAME = "yourListsCache";
	public static final String STARTER_LISTS_CACHE_KEY = "STARTER_LIST";
	public static final String STARTER_LISTS_CACHE_NAME = "fdListsCache";

	private static final Logger LOG = LoggerFactory.getInstance( QuickShopCacheUtil.class );

	private static CacheManager manager;
	
	private static synchronized void init() {
		
		if ( manager == null ) {
			LOG.info( "Initializing quickshop cache" );
			URL ehcache_config = ClassLoader.getSystemResource( EHCACHE_CONFIG_LOCATION );
			if ( ehcache_config == null ) {
				LOG.error( "EHCache config file (ehcache.xml) not found !" );
				return;
			}

			manager = CacheManager.create( ehcache_config );
		}
	}
	
	private static synchronized void shutdown() {
		if ( manager != null ) {
			LOG.info( "Disabling quickshop cache" );				
			manager.shutdown();
			manager = null;
		}
	}
	
	private static boolean useCache() {
		return FDStoreProperties.isQuickshopCaching();		
	}	
	
	private static synchronized Ehcache getCache( String cacheName ) {
		
		// Using cache
		if ( useCache() ) {
			if ( manager == null ) {
				init();
			}
			if ( manager != null ) {
				return manager.getCache( cacheName );
			}
		} else {
			shutdown();
			// Cache disabled
		}
		
		return null;
	}
	
	public static <T> List<T> getListFromCache(String cacheName, String key) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return null;
		}
		
		Element element = cache.get(key);
		if(element==null){
			return null;
		}
		
		return (List<T>)element.getObjectValue();
	}
	
	public static <T> void putListToCache(String cacheName, String key, List<T> items) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return;
		}
		
		cache.put(new Element(key, items));
	}
	
	public static void removeFromCache(String cacheName, String key) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return;
		}
		
		cache.remove(key);
	}

}
