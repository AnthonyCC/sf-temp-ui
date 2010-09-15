package com.freshdirect.framework.cache;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class ExternalMemCache<K extends Serializable,V> implements CacheI<K, V>, CacheStatisticsProvider {

    final static String NULL = "NULL";
    
    final static Logger LOG = LoggerFactory.getInstance(ExternalMemCache.class);

    String name;
    String prefix;
    int ttl;
    
    public ExternalMemCache(String name, String prefix, int ttl) {
        this.name = name;
        this.prefix = prefix;
        this.ttl = ttl;
    }
    public ExternalMemCache(String name, String prefix) {
        this(name,prefix,3600);
    }
    
    @Override
    public String getName() {
        return "external-"+name+"-prefix-"+prefix;
    }
    
    @Override
    public void remove(K key) {
        MemcachedClient client = MemcacheConfiguration.getClient();
        if (client != null) {
            client.set(prefix + toStringKey(key), 10, NULL);
        }
    }

    @Override
    public V get(K key) {
        final String stringKey = prefix + toStringKey(key);
        try {
            MemcachedClient client = MemcacheConfiguration.getClient();
            if (client != null) {
                Object value = client.get(stringKey);
                if (NULL.equals(value)) {
                    return null;
                }
                
                return (V) value;
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            LOG.error("error with key :"+key + "->\""+stringKey+"\" memcache error:"+e.getMessage(), e);
            return null;
        } catch (OperationTimeoutException e) {
            LOG.error("timeout for get:"+e.getMessage());
            return null;
        }
    }
    
    @Override
    public void put(K key, V object) {
        final String stringKey = prefix + toStringKey(key);
        // LOG.debug("put " + stringKey + "->" + object);
        try {
            MemcachedClient client = MemcacheConfiguration.getClient();
            if (client != null) {
                if (object == null) {
                    client.set(stringKey, ttl, NULL);
                } else {
                    client.set(stringKey, ttl, object);
                }
            } 
        } catch (OperationTimeoutException e) {
            LOG.error("timeout for set:"+e.getMessage());
            return;
        }
    }
    
    protected String toStringKey(K key) {
        return key.toString().replace((char)160, ' ').trim();
    }
    
    @Override
    public void clear() {
        
    }
    
    @Override
    public int size() {
        int count = 0;
        MemcachedClient client = MemcacheConfiguration.getClient();
        if (client != null) {
            for (Map<String, String> values : client.getStats().values()) {
                String string = values.get("curr_items");
                if (string != null) {
                    count += Integer.valueOf(string);
                }
            }
        }
        return count;
    }

    @Override
    public Map<String, String> getStats() {
        MemcachedClient client = MemcacheConfiguration.getClient();
        Map<String, String> result = new HashMap();
        if (client != null) {
            Map<SocketAddress, Map<String, String>> stats = client.getStats();
            for (Map.Entry<SocketAddress, Map<String, String>> k : stats.entrySet()) {
                String prefix = "memcache."+k.getKey().toString();
                
                String[] KEYS = {"get_hits", "get_misses", "curr_items" , "bytes"};
                
                for (String key : KEYS) {
                    result.put(prefix + '.' + key, k.getValue().get(key));
                }
            }
        }
        result.put("prefix", prefix);
        result.put("ttl", String.valueOf(ttl));
        return result;
    }
}
