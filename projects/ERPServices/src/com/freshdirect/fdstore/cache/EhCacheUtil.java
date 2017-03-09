package com.freshdirect.fdstore.cache;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.List;

import javax.management.MBeanServer;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.management.ManagementService;

public class EhCacheUtil {

    private static final String EHCACHE_CONFIG_LOCATION = "ehcache.xml";

    // QuickShop caches
    public static final String QS_PAST_ORDERS_CACHE_NAME = "pastOrdersCache";
    public static final String QS_SHOP_FROM_LISTS_CACHE_NAME = "yourListsCache";
    public static final String QS_STARTER_LISTS_CACHE_KEY = "STARTER_LIST";
    public static final String QS_STARTER_LISTS_CACHE_NAME = "fdListsCache";
    public static final String QS_TOP_ITEMS_CACHE_NAME = "topItemsCache";

    // Browse caches
    public static final String BR_CMS_ONLY_PRODUCT_GRABBER_CACHE_NAME = "cmsOnlyProductGrabberCache";
    public static final String BR_ERPS_PRODUCT_GRABBER_CACHE_NAME = "erpsProductGrabberCache";
    public static final String BR_ERPS_ZONE_PRODUCT_GRABBER_CACHE_NAME = "erpsZoneProductGrabberCache";
    public static final String BR_STATIC_PRODUCTS_IN_SUB_TREE_CACHE_NAME = "staticProductsInSubTreeCache";
    public static final String BR_CATEGORY_SUB_TREE_HAS_PRODUCTS_CACHE_NAME = "categorySubTreeHasProductsCache";
    public static final String BR_USER_REFINEMENT_CACHE_NAME = "userRefinementCache";
    public static final String BR_CATEGORY_TOP_ITEM_CACHE_NAME = "categoryTopItemCache";

    // Product Family cache
    public static final String FD_FAMILY_PRODUCT_CACHE_NAME = "familyProductCache";

    private static final Logger LOG = LoggerFactory.getInstance(EhCacheUtil.class);

    private static CacheManager manager;

    static {
        if (FDStoreProperties.isEhCacheEnabled()) {
            LOG.info("Initializing EH cache");
            URL ehcacheConfig = ClassLoader.getSystemResource(EHCACHE_CONFIG_LOCATION);
            if (ehcacheConfig == null) {
                LOG.error("EHCache config file (" + EHCACHE_CONFIG_LOCATION + ") not found !");
            } else {
                manager = CacheManager.create(ehcacheConfig);
                if (FDStoreProperties.isEhCacheManagementEnabled()) {
                    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
                    ManagementService.registerMBeans(manager, mBeanServer, false, false, false, true);
                }
            }
        }
    }

    public static CacheConfiguration createCacheConfiguration(String cacheName, int maxEntries, long ttl) {
        CacheConfiguration cacheConfiguration = new CacheConfiguration(cacheName, maxEntries);
        cacheConfiguration.setTimeToLiveSeconds(ttl);
        return cacheConfiguration;
    }

    public static void createCache(CacheConfiguration cacheConfiguration) {
        Ehcache result = null;
        if (manager != null) {
            result = new Cache(cacheConfiguration);
            manager.addCache(result);
        }
    }

    public static Ehcache getCache(String cacheName) {
        if (manager != null) {
            return manager.getCache(cacheName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectFromCache(String cacheName, Object key) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return null;
        }

        Element element = cache.get(key);
        if (element == null) {
            return null;
        }

        return (T) element.getObjectValue();
    }

    public static <T> void putObjectToCache(String cacheName, Object key, T item) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return;
        }

        cache.put(new Element(key, item));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getListFromCache(String cacheName, Object key) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return null;
        }

        Element element = cache.get(key);
        if (element == null) {
            return null;
        }

        return (List<T>) element.getObjectValue();
    }

    public static <T> void putListToCache(String cacheName, Object key, List<T> items) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return;
        }

        cache.put(new Element(key, items));
    }

    public static void removeFromCache(String cacheName, Object key) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
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

    public static void clearCache(String cacheName) {
        getCache(cacheName).removeAll();
    }

}
