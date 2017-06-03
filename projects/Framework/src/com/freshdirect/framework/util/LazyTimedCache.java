/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Timed LRU cache, with lazy expiration of entries.
 *
 * @version $Revision$
 * @author $Author$
 */
public class LazyTimedCache<K,V> extends TimedLruCache<K,V> {

	private Set<K> expiredKeys;
	String name;

	public LazyTimedCache(String name, int capacity, long expire) {
		super(capacity, expire);
		this.name = name;
		this.expiredKeys = new HashSet<K>();
	}

	/**
	 * Handle an expired TimedEntry. Renews the lease on the entry, adds it to the list of expired keys,
	 * notifies waiting threads, and returns the value. Does not remove the entry from cache.
	 */
	protected V getExpired(TimedEntry<K,V> entry) {
		this.expiredKeys.add( entry.key );
		entry.renewLease(this.expire);
//		this.notifyAll();
		return entry.value;
	}

	/**
	 * @return list of expired keys, or null, if it's empty
	 */
	public synchronized Set<K> clearExpiredKeys() {
		if (this.expiredKeys.isEmpty()) {
			return null;
		}
		Set<K> temp = this.expiredKeys;
		this.expiredKeys = new HashSet<K>();
		return temp;
	}


	public abstract static class RefreshThread<K,V> extends Thread {

		protected final LazyTimedCache<K,V> cache;
		private final long maxDelay;
		private final long refreshFrequency;
		
		public RefreshThread(LazyTimedCache<K,V> cache, long refreshFrequency) {
			this(cache, refreshFrequency, refreshFrequency/10);
		}

		public RefreshThread(LazyTimedCache<K,V> cache, long refreshFrequency, long maxDelay) {
		        super(cache.name + "Refresh");
			this.setDaemon(true);
			this.cache = cache;
			this.maxDelay = maxDelay;
			this.refreshFrequency = refreshFrequency;
		}
		
		public void run() {
			try {
				
				while(true) {
					//long startTime = System.currentTimeMillis();
					Set<K> expiredKeys = null;
//					synchronized(this.cache) {
//						do {
//							this.cache.wait(this.maxDelay);
//							//System.out.println("Object waiting for $$$$$$$$ "+(System.currentTimeMillis()-startTime)+" secs");
//						} while (System.currentTimeMillis()-startTime < this.refreshFrequency);
//						
//						expiredKeys = this.cache.clearExpiredKeys();
//					}
					
					Thread.sleep( this.refreshFrequency);					
					expiredKeys = this.cache.clearExpiredKeys();
					if (expiredKeys!=null) {
						//System.out.println("Next Refresh started for "+expiredKeys.size());
						this.refresh(expiredKeys);
					}
				}

			} catch (InterruptedException ex) {}
		}

		protected abstract void refresh(Set<K> expiredKeys);
		
	}

}
