package com.freshdirect.framework.cache;

import java.io.Serializable;

/**
 * A cache which calls the given ObjectAccessor if the object is not found in the cache.
 * 
 * @author zsombor
 *
 * @param <K>
 * @param <V>
 */
public class ActiveCacheModul<K extends Serializable, V> implements CacheI<K, V> {

    CacheI<K, V> cache;
    ObjectAccessor<K, V> accessor;

    public ActiveCacheModul(ObjectAccessor<K, V> accessor, CacheI<K, V> cache) {
        this.cache = cache;
        this.accessor = accessor;
    }

    /**
     * 
     * @see com.freshdirect.framework.cache.CacheI#clear()
     */
    public void clear() {
        cache.clear();
    }

    /**
     * @param key
     * @return
     * @see com.freshdirect.framework.cache.CacheI#get(java.io.Serializable)
     */
    public V get(K key) {
        V value = cache.get(key);
        if (value == null) {
            value = accessor.get(key);
            cache.put(key, value);
        }
        return value;
    }

    /**
     * @return
     * @see com.freshdirect.framework.cache.CacheI#getName()
     */
    public String getName() {
        return "ActiveCache[" + cache.getName() + ", accessor:" + accessor + ']';
    }

    /**
     * @param key
     * @param object
     * @see com.freshdirect.framework.cache.CacheI#put(java.io.Serializable,
     *      java.lang.Object)
     */
    public void put(K key, V object) {
        cache.put(key, object);
    }

    /**
     * @param key
     * @see com.freshdirect.framework.cache.CacheI#remove(java.io.Serializable)
     */
    public void remove(K key) {
        cache.remove(key);
    }

    /**
     * @return
     * @see com.freshdirect.framework.cache.CacheI#size()
     */
    public int size() {
        return cache.size();
    }

}
