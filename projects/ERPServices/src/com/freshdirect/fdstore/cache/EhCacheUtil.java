package com.freshdirect.fdstore.cache;

import java.net.URL;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class EhCacheUtil {
	
	private static final String EHCACHE_CONFIG_LOCATION = "ehcache.xml";
	
	//QuickShop caches
	public static final String QS_PAST_ORDERS_CACHE_NAME = "pastOrdersCache";
	public static final String QS_SHOP_FROM_LISTS_CACHE_NAME = "yourListsCache";
	public static final String QS_STARTER_LISTS_CACHE_KEY = "STARTER_LIST";
	public static final String QS_STARTER_LISTS_CACHE_NAME = "fdListsCache";

	//Browse caches
	public static final String BR_CMS_ONLY_PRODUCT_GRABBER_CACHE_NAME = "cmsOnlyProductGrabberCache";
	public static final String BR_ERPS_PRODUCT_GRABBER_CACHE_NAME = "erpsProductGrabberCache";
	public static final String BR_ERPS_ZONE_PRODUCT_GRABBER_CACHE_NAME = "erpsZoneProductGrabberCache";
	public static final String BR_STATIC_PRODUCTS_IN_SUB_TREE_CACHE_NAME = "staticProductsInSubTreeCache";
	public static final String BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE_NAME = "categorySubTreeHasProductsCache";
	public static final String BR_USER_REFINEMENT_CACHE_NAME = "userRefinementCache";
	
	private static final Logger LOG = LoggerFactory.getInstance( EhCacheUtil.class );

	private static CacheManager manager;
	
	static {
		if (FDStoreProperties.isEhCacheEnabled()) {
			LOG.info( "Initializing EH cache" );
			URL ehcacheConfig = ClassLoader.getSystemResource( EHCACHE_CONFIG_LOCATION );
			if ( ehcacheConfig == null ) {
				LOG.error( "EHCache config file ("+EHCACHE_CONFIG_LOCATION+") not found !" );

			} else {
				manager = CacheManager.create( ehcacheConfig );
			}
		}
	}
	
	private static Ehcache getCache( String cacheName ) {
		if ( manager != null ) {
			return manager.getCache( cacheName );
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromCache(String cacheName, Object key) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return null;
		}
		
		Element element = cache.get(key);
		if(element==null){
			return null;
		}
		
		return (T)element.getObjectValue();
	}
	
	public static <T> void putObjectToCache(String cacheName, Object key, T item) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return;
		}
		
		cache.put(new Element(key, item));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getListFromCache(String cacheName, Object key) {
		
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
	
	public static <T> void putListToCache(String cacheName, Object key, List<T> items) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return;
		}
		
		cache.put(new Element(key, items));
	}
	
	public static void removeFromCache(String cacheName, Object key) {
		
		Ehcache cache = getCache(cacheName);
		if(cache==null){
			LOG.error("No cache found with name: " + cacheName);
			return;
		}
		
		cache.remove(key);
	}

	
	public static <T> List<T> getListFromCache(CacheEntryIdentifier cacheEntryIdentifier) {
		return getListFromCache(cacheEntryIdentifier.getCacheName(), cacheEntryIdentifier.getEntryKey());
	}

	public static <T> void putListToCache(CacheEntryIdentifier cacheEntryIdentifier, List<T> items) {
		putListToCache(cacheEntryIdentifier.getCacheName(), cacheEntryIdentifier.getEntryKey(), items);
	}
	
	public static void removeFromCache(CacheEntryIdentifier cacheEntryIdentifier) {
		removeFromCache(cacheEntryIdentifier.getCacheName(), cacheEntryIdentifier.getEntryKey());
	}
}
