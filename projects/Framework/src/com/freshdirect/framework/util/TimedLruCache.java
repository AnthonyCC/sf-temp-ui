/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.io.Serializable;


/**
 * LRU cache with expiration of entries.
 * 
 * TODO: it might have better performance if it used ConcurrentHashMap instead of synchronized methods
 *
 * @version $Revision$
 * @author $Author$
 */
public class TimedLruCache<K extends Serializable,V> extends LruCache<K,V> {
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

	public synchronized void put(K key, V value) {
		super.putEntry( new TimedEntry<K,V>(key, value, this.expire) );
	}

	public synchronized V get(K key) {
		Entry<K,V> entry = this.getEntry(key);
		if (entry==null) {
			return null;
		}
		TimedEntry<K,V> timedEntry = (TimedEntry<K,V>)entry;
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
	protected V getExpired(TimedEntry<K,V> entry) {
		super.removeEntry(entry);
		return null;
	}

	protected static class TimedEntry<K,V> extends LruCache.Entry<K,V> {
		private long expiration;

		public TimedEntry(K key, V value, long expire) {
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
