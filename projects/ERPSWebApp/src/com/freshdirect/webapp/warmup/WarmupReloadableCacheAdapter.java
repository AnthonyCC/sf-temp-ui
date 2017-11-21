package com.freshdirect.webapp.warmup;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CacheWarmupAction;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.fdstore.sitemap.SitemapDataFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.wcms.CMSContentFactory;
import com.freshdirect.webapp.taglib.fdstore.WineHomenavTag;

public class WarmupReloadableCacheAdapter {

    private static final WarmupReloadableCacheAdapter INSTANCE = new WarmupReloadableCacheAdapter();
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(WarmupReloadableCacheAdapter.class);
    
    private final List<String> EHCACHE_REGISTRY = new ArrayList<String>();

    private WarmupReloadableCacheAdapter() {
        initializeCaches();
    }

    private void initializeCaches() {
        populateEhCaches();
    }

    private void populateEhCaches() {
        EHCACHE_REGISTRY.addAll(CmsCaches.findAllCacheNamesByWarmupAction(CacheWarmupAction.RELOADABLE));
    }

    public void evictCaches() {
        for (String cacheName : EHCACHE_REGISTRY) {
            LOGGER.info("Evicting cache: " + cacheName);
            CmsServiceLocator.ehCacheUtil().getCache(cacheName).removeAll();
        }

        ContentFactory.getInstance().evictNodesByCaches();
        WineHomenavTag.evictWarmupRelatedCache();
        SitemapDataFactory.evictSitemapData();
        CMSContentFactory.evictPageCache();
    }

    public static WarmupReloadableCacheAdapter defaultService() {
        return INSTANCE;
    }
}
