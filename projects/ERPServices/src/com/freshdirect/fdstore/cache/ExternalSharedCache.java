package com.freshdirect.fdstore.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.fdstore.FDVersion;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.CacheStatisticsProvider;
import com.freshdirect.framework.cache.ExternalMemCache;
import com.freshdirect.framework.cache.StatRecorderCache;
import com.freshdirect.framework.cache.StatRecorderCacheMBean;
import com.freshdirect.framework.util.ProgressReporter;

public abstract class ExternalSharedCache<K extends Serializable, T extends Comparable<T>, V extends FDVersion<T>> extends FDAbstractCache<K, T, V> implements CacheStatisticsProvider, ExternalSharedCacheMBean {

    CacheI<String, V> memCache;

    public ExternalSharedCache(boolean mock) {
        super(mock);
        memCache = null;
    }

    public ExternalSharedCache(String prefix, int ttl) {
        super();
        getLog().info("configuring with memcached, refresh period : " + getRefreshDelay() + " ms, ttl : "
                + (ttl <= 0 ? "unlimited" : " " + ttl + " sec"));
        memCache = StatRecorderCache.wrap(new ExternalMemCache<String, V>(this.getClass().getSimpleName() + "-memcache", prefix, ttl));
    }


    protected V getCachedItem(K key) {
        V item = super.getCachedItem(key);
        if ((item == null) && (memCache != null)) {
            item = memCache.get(getStringKey(key));
            if (item != null) {
                put(key, item);
            }
        }
        return item;       
    }
    
    protected String getStringKey(K key) {
        return (String) key;
    }
    
    @Override
    protected void startRefresher() {
        long time = System.currentTimeMillis();
        super.startRefresher();
        long elapsed = System.currentTimeMillis() - time;
        final StatRecorderCacheMBean stats = (StatRecorderCacheMBean) this.memCache;
        getLog().info("cache initialized in " + elapsed + " sec, memcache hits:" + stats.getCacheHit() + ", memcache cache misses:"
                + stats.getCacheMiss() + ", cache puts:" + stats.getCachePut());
    }
    
    
    /**
     * This is only public for testing purposes.
     * @param key
     * @return
     */
    public V getFromExternalCache(K key) {
        if (memCache != null) {
            return memCache.get(getStringKey(key));
        }
        return null;
    }
    
    protected void putToExternalCache(K key, V value) {
        if (memCache != null) {
            memCache.put(getStringKey(key), value);
        }
        return;
    }
    
    @Override
    public Map<String, String> getStats() {
        Map<String, String> result;
        if (memCache instanceof CacheStatisticsProvider) {
            CacheStatisticsProvider cp = (CacheStatisticsProvider) memCache;
            result = cp.getStats();
        } else {
            result = new HashMap<String, String>();            
        }
        fillStatMap(result);
        return result;
    }
    
    @Override
    public int storeDataToExternalCache() {
        if (memCache != null) {
            ProgressReporter p = new ProgressReporter();
            int count = 0;
            int i = 0;
            for (Entry<K,V> e : keySet()) {
                if (e.getValue() != null) {
                    memCache.put(getStringKey(e.getKey()), e.getValue());
                    count++;
                }
                i++;
                if (p.shouldLogMessage(i)) {
                    getLog().info("puting datas from " + getName() + ", at " + i + ", stored :" + count);
                }
            }
            return count;
        } else {
            return -1;
        }
    }
    
    @Override
    public int getAlreadyStoredItemCount() {
        if (memCache != null) {
            ProgressReporter p = new ProgressReporter();
            int count = 0;
            int i = 0;
            for (Entry<K,V> e : keySet()) {
                V externalValue = memCache.get(getStringKey(e.getKey()));
                if (e.getValue() != null && externalValue != null) {
                    count++;
                }
                i++;
                if (p.shouldLogMessage(i)) {
                    getLog().info("calculating for " + getName() + ", at " + i + ", already found:" + count);
                }
            }
            return count;
        } else {
            return -1;
        }
    }


}
