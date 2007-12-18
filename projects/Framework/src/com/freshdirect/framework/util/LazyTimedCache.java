/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.*;

/**
 * Timed LRU cache, with lazy expiration of entries.
 *
 * @version $Revision$
 * @author $Author$
 */
public class LazyTimedCache extends TimedLruCache {

	private List expiredKeys;

	public LazyTimedCache(int capacity, long expire) {
		super(capacity, expire);
		this.expiredKeys = new ArrayList();
	}

	/**
	 * Handle an expired TimedEntry. Renews the lease on the entry, adds it to the list of expired keys,
	 * notifies waiting threads, and returns the value. Does not remove the entry from cache.
	 */
	protected Object getExpired(TimedEntry entry) {
		this.expiredKeys.add( entry.key );
		entry.renewLease(this.expire);
		this.notifyAll();
		return entry.value;
	}

	/**
	 * @return list of expired keys, or null, if it's empty
	 */
	public synchronized List clearExpiredKeys() {
		if (this.expiredKeys.isEmpty()) {
			return null;
		}
		List temp = this.expiredKeys;
		this.expiredKeys = new ArrayList();
		return temp;
	}


	public abstract static class RefreshThread extends Thread {

		protected final LazyTimedCache cache;
		private final long maxDelay;
		private final long refreshFrequency;
		
		public RefreshThread(LazyTimedCache cache, String threadName, long refreshFrequency) {
			this(cache, threadName, refreshFrequency, refreshFrequency/10);
		}

		public RefreshThread(LazyTimedCache cache, String threadName, long refreshFrequency, long maxDelay) {
			super(threadName);
			this.setDaemon(true);
			this.cache = cache;
			this.maxDelay = maxDelay;
			this.refreshFrequency = refreshFrequency;
		}
		
		public void run() {
			try {
				
				while(true) {
					long startTime = System.currentTimeMillis();
					List expiredKeys = null;
					synchronized(this.cache) {
						do {
							this.cache.wait(this.maxDelay);
						} while (System.currentTimeMillis()-startTime < this.refreshFrequency);
						expiredKeys = this.cache.clearExpiredKeys();
					}
					if (expiredKeys!=null) {
						this.refresh(expiredKeys);
					}
				}

			} catch (InterruptedException ex) {}
		}

		protected abstract void refresh(List expiredKeys);
		
	}

}
