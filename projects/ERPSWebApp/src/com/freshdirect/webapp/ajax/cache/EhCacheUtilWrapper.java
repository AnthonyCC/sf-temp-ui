package com.freshdirect.webapp.ajax.cache;

import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.ContentFactory;

public class EhCacheUtilWrapper {

    public static <T> T getObjectFromCache(String cacheName, Object key) {
        T item = null;
        if (ContentFactory.getInstance().isAllowToUseContentCache()) {
            item = EhCacheUtil.getObjectFromCache(cacheName, key);
        }
        return item;
    }

    public static <T> void putObjectToCache(String cacheName, Object key, T item) {
        if (ContentFactory.getInstance().isAllowToUseContentCache()) {
            EhCacheUtil.putObjectToCache(cacheName, key, item);
        }
    }

    public static void removeFromCache(String cacheName, Object key) {
        if (ContentFactory.getInstance().isAllowToUseContentCache()) {
            EhCacheUtil.removeFromCache(cacheName, key);
        }
    }
}
