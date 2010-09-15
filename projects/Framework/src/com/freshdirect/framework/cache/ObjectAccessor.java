package com.freshdirect.framework.cache;

/**
 * Encapsulates the object retrieval/creator functionality, used by {@link ActiveCacheModul}
 *  
 * @author zsombor
 *
 * @param <K>
 * @param <V>
 */
public interface ObjectAccessor<K, V> {
    public V get(K key);
}
