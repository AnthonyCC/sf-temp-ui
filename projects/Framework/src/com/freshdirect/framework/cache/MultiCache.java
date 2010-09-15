package com.freshdirect.framework.cache;

import java.io.Serializable;

public class MultiCache<K extends Serializable, V> implements CacheI<K, V> {

    CacheI<K, V> primary;
    CacheI<K, V> secondary;
    
    public MultiCache(CacheI<K, V> primary, CacheI<K, V> secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }
    
    /**
     * 
     * @see com.freshdirect.framework.cache.CacheI#clear()
     */
    public void clear() {
        primary.clear();
        secondary.clear();
    }
    /**
     * @param key
     * @return
     * @see com.freshdirect.framework.cache.CacheI#get(java.io.Serializable)
     */
    public V get(K key) {
        V value=  primary.get(key);
        if (value == null) {
            value = secondary.get(key);
            if (value != null) {
                primary.put(key, value);
            }
        }
        return value;
    }
    /**
     * @return
     * @see com.freshdirect.framework.cache.CacheI#getName()
     */
    public String getName() {
        return "Multi[" + primary.getName() + ',' + secondary.getName() + ']';
    }
    /**
     * @param key
     * @param object
     * @see com.freshdirect.framework.cache.CacheI#put(java.io.Serializable, java.lang.Object)
     */
    public void put(K key, V object) {
        primary.put(key, object);
        secondary.put(key, object);
    }
    /**
     * @param key
     * @see com.freshdirect.framework.cache.CacheI#remove(java.io.Serializable)
     */
    public void remove(K key) {
        primary.remove(key);
        secondary.remove(key);
    }
    /**
     * @return
     * @see com.freshdirect.framework.cache.CacheI#size()
     */
    public int size() {
        return primary.size();
    }
    
    
    
}
