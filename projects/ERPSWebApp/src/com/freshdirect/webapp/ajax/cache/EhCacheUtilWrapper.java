package com.freshdirect.webapp.ajax.cache;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.storeapi.content.ContentFactory;

public class EhCacheUtilWrapper {

    public static <T> T getObjectFromCache(String cacheName, Object key) {
        T item = null;
        if (ContentFactory.getInstance().isAllowToUseContentCache()) {
            item = CmsServiceLocator.ehCacheUtil().getObjectFromCache(cacheName, key);
        }
        return item;
    }

    public static <T> void putObjectToCache(String cacheName, Object key, T item) {
        if (ContentFactory.getInstance().isAllowToUseContentCache()) {
            CmsServiceLocator.ehCacheUtil().putObjectToCache(cacheName, key, item);
        }
    }

    public static void removeFromCache(String cacheName, Object key) {
        if (ContentFactory.getInstance().isAllowToUseContentCache()) {
            CmsServiceLocator.ehCacheUtil().removeFromCache(cacheName, key);
        }
    }
}
