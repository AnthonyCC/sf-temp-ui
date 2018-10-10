package com.freshdirect.framework.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public class AsyncDBRefreshEhCache<K, V> {
	
	private static Category LOGGER = LoggerFactory.getInstance(AsyncDBRefreshEhCache.class);
	private final Cache cache;

	public AsyncDBRefreshEhCache(String name, int capacity) {
		CacheManager mgr = CacheManager.getInstance();

		CacheConfiguration config = buildConfiguration(name, capacity);
		this.cache = new Cache(config);
		mgr.addCache(cache);
	}

	private CacheConfiguration buildConfiguration(String name, int capacity) {
		return new CacheConfiguration(name, capacity).memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.eternal(true).timeToLiveSeconds(0).timeToIdleSeconds(0).diskExpiryThreadIntervalSeconds(0);
	}

	public void put(K k, V v) {
		cache.put(new Element(k, v));
	}

	@SuppressWarnings("unchecked")
	public V get(K k) {
		Element e = cache.get(k);
		if (e != null) {
			return (V) e.getObjectValue();
		}
		return null;
	}

	public void update(K k, V v) {
		this.put(k, v);
	}

	public void remove(K k) {
		this.cache.remove(k);
	}

	public void clear() {
		cache.removeAll();
	}

	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		return new HashSet<K>(cache.getKeys());
	}

	public String getName() {
		return this.cache.getName();
	}

	public int size() {
		return this.cache.getSize();
	}

	/**
	 * Asynchronous thread which refreshes the cached content periodically.
	 *
	 * @param <K>
	 * @param <V>
	 */
	public abstract static class RefreshThread<K, V> extends Thread {
		protected final AsyncDBRefreshEhCache<K, V> cache;
		private final long refetchFrequency;
		private long lastRefreshTimeStamp; // in ms

		public RefreshThread(AsyncDBRefreshEhCache<K, V> cache, long refetchFrequency) {
			super(cache.getName() + "-Refresh");
			this.setDaemon(true); // low priority, non-blocking-exit thread

			this.cache = cache;
			this.refetchFrequency = refetchFrequency;
			this.lastRefreshTimeStamp = System.currentTimeMillis();
		}

		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(refetchFrequency);
					long now = System.currentTimeMillis();
					try {
						refresh(lastRefreshTimeStamp);
						lastRefreshTimeStamp = now;
					} catch (Exception e) {
						LOGGER.error(this.getName(),e);
					}
				}
			} catch (InterruptedException iex) {
				LOGGER.error("Interrupted "+this.getName(),iex);
				// should never happen, only if this thread is interrupted
			}
		}

		protected abstract void refresh(long lastUpdatedTimeStamp) throws Exception;
	}

}
