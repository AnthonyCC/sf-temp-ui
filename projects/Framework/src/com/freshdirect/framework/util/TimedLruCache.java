/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;


/**
 * LRU cache with expiration of entries.
 *
 * @version $Revision$
 * @author $Author$
 */
public class TimedLruCache extends LruCache {
	protected final long expire;

	/**
	 * 
	 * @param capacity the maximum size of the cache
	 * @param expire the time to expire a living entry in milliseconds
	 */
	public TimedLruCache(int capacity, long expire) {
		super(capacity);
		this.expire = expire;
	}

	public synchronized void put(Object key, Object value) {
		super.putEntry( new TimedEntry(key, value, this.expire) );
	}

	public synchronized Object get(Object key) {
		Entry entry = this.getEntry(key);
		if (entry==null) {
			return null;
		}
		TimedEntry timedEntry = (TimedEntry)entry;
		if ( timedEntry.isExpired() ) {
			return this.getExpired(timedEntry);
		}
		return entry.value;
	}

	/**
	 * Handle an expired TimedEntry. The default implementation removes the entry, and reports a cache miss.
	 * Allows subclasses to implement more sophisticated ways to handle expiration.
	 *
	 * @return entry.value or null
	 */
	protected Object getExpired(TimedEntry entry) {
		super.removeEntry(entry);
		return null;
	}

	protected static class TimedEntry extends LruCache.Entry {
		private long expiration;

		public TimedEntry(Object key, Object value, long expire) {
			super(key, value);
			this.renewLease(expire);
		}

		public void renewLease(long expire) {
			this.expiration = System.currentTimeMillis()+expire;
		}

		public boolean isExpired() {
			return System.currentTimeMillis() > expiration;
		}

	}

}
