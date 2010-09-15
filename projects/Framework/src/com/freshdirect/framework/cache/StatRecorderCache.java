package com.freshdirect.framework.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.framework.util.Counter;
import com.freshdirect.framework.util.JMXUtil;

public class StatRecorderCache<K extends Serializable, V> implements CacheI<K, V>, StatRecorderCacheMBean, CacheStatisticsProvider {
    
    final Counter getHitCounter;
    final Counter getMissCounter;
    final Counter putCounter;
    
    CacheI<K, V> inner;

    public StatRecorderCache(CacheI<K, V> inner) {
        this.inner = inner;
        this.getHitCounter = new Counter(inner.getName() + ".getHit");
        this.getMissCounter = new Counter(inner.getName() + ".getMiss");
        this.putCounter = new Counter(inner.getName() + ".put");
    }

    /**
     * 
     * @see com.freshdirect.framework.cache.CacheI#clear()
     */
    @Override
    public void clear() {
        inner.clear();
    }

    /**
     * @param key
     * @return
     * @see com.freshdirect.framework.cache.CacheI#get(java.io.Serializable)
     */
    @Override
    public V get(K key) {
        try {
            long t = getHitCounter.start();
            V value = inner.get(key);
            if (value != null) {
                getHitCounter.end(t);
            } else {
                getMissCounter.end(t);
            }
            return value;
        } catch (RuntimeException e) {
            getHitCounter.failedCall();
            throw e;
        }
    }

    /**
     * @return
     * @see com.freshdirect.framework.cache.CacheI#getName()
     */
    @Override
    public String getName() {
        return inner.getName();
    }

    /**
     * @param key
     * @param object
     * @see com.freshdirect.framework.cache.CacheI#put(java.io.Serializable,
     *      java.lang.Object)
     */
    @Override
    public void put(K key, V object) {
        long t = putCounter.start();
        inner.put(key, object);
        putCounter.end(t);
    }

    /**
     * @param key
     * @see com.freshdirect.framework.cache.CacheI#remove(java.io.Serializable)
     */
    @Override
    public void remove(K key) {
        inner.remove(key);
    }

    /**
     * @return
     * @see com.freshdirect.framework.cache.CacheI#size()
     */
    @Override
    public int size() {
        return inner.size();
    }

    
    public int getCacheHit() {
        return getHitCounter.getCallCount();
    }
    
    @Override
    public int getCachePut() {
        return putCounter.getCallCount();
    }
    
    @Override
    public int getCacheMiss() {
        return getMissCounter.getCallCount();
    }

    public int getSize() {
        return inner.size();
    }
    
    public void resetCacheHitStat() {
        getHitCounter.reset();
        getMissCounter.reset();
        putCounter.reset();
    }
    
    public CacheI<K, V> getInner() {
        return inner;
    }
    
    @Override
    public String getInternalType() {
        return inner.getClass().getSimpleName();
    }
    
    @Override
    public Map<String, String> getStats() {
        Map<String, String> result;
        if (inner instanceof CacheStatisticsProvider) {
            CacheStatisticsProvider cp = (CacheStatisticsProvider) inner;
            result = cp.getStats();
        } else {
            result = new HashMap<String, String>();            
        }
        result.put("stats.get.callCount", String.valueOf(this.getHitCounter.getCallCount()));
        result.put("stats.get.allTime", String.valueOf(this.getHitCounter.getAllTime()));
        result.put("stats.get.averageTime", String.valueOf(this.getHitCounter.getAverageTime()));
        result.put("stats.get.missedCount", String.valueOf(this.getMissCounter.getCallCount()));
        result.put("stats.put.callCount", String.valueOf(this.putCounter.getCallCount()));
        result.put("stats.put.allTime", String.valueOf(this.putCounter.getAllTime()));
        result.put("stats.put.averageTime", String.valueOf(this.putCounter.getAverageTime()));
        return result;
    }
    

    private void register() {
        JMXUtil.registerToPlatformMBean(this, "com.freshdirect.framework.cache", "CacheI", inner.getName());
        getHitCounter.register();
        getMissCounter.register();
        putCounter.register();
    }

    public static <K extends Serializable, V, T> StatRecorderCache<K, V> wrap(CacheI<K, V> k) {
        StatRecorderCache<K,V> src = new StatRecorderCache<K, V>(k);
        src.register();
        return src;
    }
    
}
