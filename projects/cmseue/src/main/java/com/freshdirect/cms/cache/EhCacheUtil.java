package com.freshdirect.cms.cache;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.ObjectExistsException;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.CacheDecoratorFactoryConfiguration;
import net.sf.ehcache.config.CacheConfiguration.CacheLoaderFactoryConfiguration;
import net.sf.ehcache.constructs.refreshahead.RefreshAheadCacheFactory;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy.MemoryStoreEvictionPolicyEnum;

@Service
public class EhCacheUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EhCacheUtil.class);

    public static final String QS_STARTER_LISTS_CACHE_KEY = "STARTER_LIST";

    @Autowired
    private CacheManager manager;

    @Value("${ehcache.management.enabled:false}")
    private boolean ehCacheManagementEnabled;

    @PostConstruct
    private void configureCacheManager() {
        CacheConfiguration cmsPageCacheConfiguration = createCmsPageCacheConfiguration();
        createCache(cmsPageCacheConfiguration);

        if (ehCacheManagementEnabled) {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ManagementService.registerMBeans(manager, mBeanServer, false, false, false, true);
        }
    }

    private static CacheConfiguration createCmsPageCacheConfiguration() {
        CacheConfiguration cmsPageCacheConfiguration = new CacheConfiguration("cmsPageCache", 50);
        cmsPageCacheConfiguration.setEternal(true);
        cmsPageCacheConfiguration.setMemoryStoreEvictionPolicy(MemoryStoreEvictionPolicyEnum.LFU.name());
        CacheLoaderFactoryConfiguration fdCacheLoaderFactoryConfiguration = new CacheLoaderFactoryConfiguration();
        fdCacheLoaderFactoryConfiguration.setClass(FDCacheLoaderFactory.class.getCanonicalName());
        fdCacheLoaderFactoryConfiguration.setProperties("some_property=1, some_other_property=2");
        cmsPageCacheConfiguration.addCacheLoaderFactory(fdCacheLoaderFactoryConfiguration);
        CacheDecoratorFactoryConfiguration pageCacheDecoratorFactory = new CacheDecoratorFactoryConfiguration();
        pageCacheDecoratorFactory.setClass(RefreshAheadCacheFactory.class.getCanonicalName());
        pageCacheDecoratorFactory.setProperties("name=myCacheRefresher,timeToRefreshSeconds=200,batchSize=10,numberOfThreads=4,maximumBacklogItems=100,evictOnLoadMiss=true");
        cmsPageCacheConfiguration.addCacheDecoratorFactory(pageCacheDecoratorFactory);
        return cmsPageCacheConfiguration;
    }

    public void createCache(CacheConfiguration cacheConfiguration) {
        if (cacheConfiguration == null) {
            return;
        }

        final String cacheName = cacheConfiguration.getName();
        if (manager == null) {
            LOG.error("Cache manager is not created, yet, cache with name " + cacheName + " is not created");
            return;
        } else if (manager.cacheExists(cacheConfiguration.getName())) {
            LOG.debug("Cache with name " + cacheName + " already exists, skip creating another instance");
            return;
        }

        try {
            Ehcache instance = new Cache(cacheConfiguration);
            manager.addCache(instance);
        } catch (ObjectExistsException exc) {
            LOG.error("Failed to create cache with name " + cacheName + " (possible duplication)", exc);
        }
    }

    public Ehcache getCache(String cacheName) {
        if (manager != null) {
            return manager.getCache(cacheName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObjectFromCache(String cacheName, Object key) {

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

    public <T> void putObjectToCache(String cacheName, Object key, T item) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return;
        }

        cache.put(new Element(key, item));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getListFromCache(String cacheName, Object key) {

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

    public <T> void putListToCache(String cacheName, Object key, List<T> items) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return;
        }

        cache.put(new Element(key, items));
    }

    public void removeFromCache(String cacheName, Object key) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return;
        }

        cache.remove(key);
    }

    public <T> List<T> getListFromCache(CacheEntryIdentifier cacheEntryIdentifier) {
        return getListFromCache(cacheEntryIdentifier.getCacheName(), cacheEntryIdentifier.getEntryKey());
    }

    public <T> void putListToCache(CacheEntryIdentifier cacheEntryIdentifier, List<T> items) {
        putListToCache(cacheEntryIdentifier.getCacheName(), cacheEntryIdentifier.getEntryKey(), items);
    }

    public void removeFromCache(CacheEntryIdentifier cacheEntryIdentifier) {
        removeFromCache(cacheEntryIdentifier.getCacheName(), cacheEntryIdentifier.getEntryKey());
    }

    public void clearCache(String cacheName) {
        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return;
        }
        cache.removeAll();
    }

    public boolean isObjectInCache(String cacheName, Object key) {

        Ehcache cache = getCache(cacheName);
        if (cache == null) {
            LOG.error("No cache found with name: " + cacheName);
            return false;
        }

        Element element = cache.get(key);
        return element != null;

    }
}
