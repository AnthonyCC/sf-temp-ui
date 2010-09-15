package com.freshdirect.framework.cache;

import java.io.Serializable;
import java.util.Collections;

import org.apache.commons.collections.map.LRUMap;

/**
 * Commons collection bases LRU Cache
 * @author zsombor
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K extends Serializable, V>  extends SimpleCache<K, V> {

    @SuppressWarnings("unchecked")
    public LRUCache(String name, int size) {
        super(Collections.synchronizedMap(new LRUMap(size)), "lru-" + name + '(' + size + ')');
    }

}
