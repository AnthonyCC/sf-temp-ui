package com.freshdirect.fdstore.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDVersion;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.CacheStatisticsProvider;
import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * This is the base class for an automaticly updating cache, with bulk loading.
 * 
 * @author zsombor
 * 
 * @param <K>
 * @param <V>
 */
public abstract class FDAbstractCache<K extends Serializable, T extends Comparable<T>, V extends FDVersion<T>> implements FDAbstractCacheMBean, CacheI<K, V>, CacheStatisticsProvider {
    
    
    
	private final Thread refresher;

	private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<K, V>();
	T lastVersion = null;
	int hitCount = 0;
	boolean running = true;
	boolean registerMBean = true;
	boolean mock = false;

	public FDAbstractCache() {
		ERPServiceLocator.getInstance();
		if (registerMBean) {
		    registerSelf();
		}
		refresher = new RefreshThread(this.getClass().getName());
	}


    /**
     * 
     */
    protected void startRefresher() {
        this.refresh();
        refresher.start();
    }

	public FDAbstractCache(boolean mock) {
	    this.mock = mock;
		refresher = null;
	}

	private void registerSelf() {
		JMXUtil.registerToPlatformMBean(this, "com.freshdirect.framework.cache.FDCache", "Cache", this.getClass().getSimpleName());
	}

        private synchronized void refresh() {
            Map<K, V> m = loadData(lastVersion);
            if (!m.isEmpty()) {
                T max = null;
                for (V value : m.values()) {
                    T curr = value.getVersion();
                    if (max == null || (curr != null && max.compareTo(curr) < 0)) {
                        max = curr;
                    }
                }
                lastVersion = max;
                this.cache.putAll(m);
            }
        }

	protected abstract Map<K, V> loadData(T lastVersion);

	protected V getCachedItem(K key) {
		hitCount++;
		return this.cache.get(key);
	}

	private final class RefreshThread extends Thread {
		public RefreshThread(String name) {
			super(name);
		}

		public void run() {
			try {

				while (running) {
				    try {
					refresh();
				    } catch (EJBException e) {
				        getLog().error("Error during refreshing "+this.getClass().getName()+" : "+e.getMessage(), e);
                                    } catch (FDRuntimeException e) {
                                        getLog().error("Error during refreshing "+this.getClass().getName()+" : "+e.getMessage(), e);
				    } catch (RuntimeException e) {
				        getLog().error("Error during refreshing "+this.getClass().getName()+" : "+e.getMessage(), e);
				    }
                                    
				    long time = getRefreshDelay();
				    getLog().info("sleep " + time / 1000 + " sec till the next refresh");
				    Thread.sleep(time);
				}

			} catch (InterruptedException ex) {
			} 
		}
	}

	@Override
	public int getHitCount() {
		return hitCount;
	}

	@Override
	public void resetHitCount() {
		this.hitCount = 0;
	}

	@Override
	public int size() {
		return cache.size();
	}
	
	@Override
	public int getSize() {
	    return cache.size();
	}

	@Override
	public int refreshCache() {
		refresh();
		return cache.size();
	}
	
	@Override
	public V get(K key)  {
	    return getCachedItem(key);
	}
	
	@Override
	public void put(K key, V object) {
	    cache.put(key, object);
	}
	
	@Override
	public String getName() {
	    return this.getClass().getSimpleName();
	}

	@Override
	public void remove(K key) {
	    this.cache.remove(key);
	}
	
	@Override
	public void clear() {
	    
	}
	
	protected Set<Entry<K, V>> keySet() {
	    return this.cache.entrySet();
	}

	@Override
	public Map<String, String> getStats() {
	    return fillStatMap(new HashMap<String, String>());
	}

        /**
         * @param result
         * @return 
         */
        protected Map<String, String> fillStatMap(Map<String, String> result) {
            result.put("internal.hitcount", String.valueOf(hitCount));
            result.put("internal.size", String.valueOf(getSize()));
            result.put("internal.lastVersion", lastVersion != null ? lastVersion.toString() : "[null]");
            result.put("internal.refreshDelay", String.valueOf(getRefreshDelay()));
            return result;
        }
        
        protected abstract Logger getLog();


        /**
         * @return
         */
        public abstract long getRefreshDelay();

}
