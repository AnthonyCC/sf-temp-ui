/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * "Least-recently-used" cache.
 *
 * @version $Revision$
 * @author $Author$
 */
public class LruCache<K,V> {

	private final static boolean ASSERTIONS = false;

	private final int capacity;
	private final Map<K,Entry<K,V>> cache;

	private int size = 0;
	private Entry<K,V> head = null;
	private Entry<K,V> tail = null;

	public LruCache(int capacity) {
		if (capacity<2) throw new IllegalArgumentException("Capacity must be at least 2");
		this.capacity = capacity;
		this.cache = new HashMap<K,Entry<K,V>>(capacity);
	}

	public synchronized void put(K key, V value) {
		this.putEntry( new Entry<K,V>(key, value) );
	}

	protected final void putEntry(Entry<K,V> entry) {
		Entry<K,V> oldEntry = this.cache.get(entry.key);
		if (oldEntry!=null) {
			// remove the old entry
			this.removeEntry(oldEntry);
		
		} else {
			if (this.size==this.capacity) {
				// cache full, remove tail
				this.removeEntry( this.tail );
			}
		}

		// add to head
		size++;
		entry.next = this.head;
		entry.prev = null;
  		if (this.head != null) {
			this.head.prev = entry;
		} else {
			this.tail = entry;
		}
		this.head = entry;

		// update cache
		this.cache.put(entry.key, entry);

		if (ASSERTIONS) {
			if (this.size<0) throw new RuntimeException("Assertion failed: size<0");
			if (this.size>capacity) throw new RuntimeException("Assertion failed: size>capacity");
			if (this.cache.size()!=this.size) throw new RuntimeException("Assertion failed: cache.size()!=size");
		}
		
	}


	public synchronized V get(K key) {
		Entry<K,V> e = this.getEntry(key);
		return e==null ? null : e.value;
	}

	public synchronized void remove(Object key) {
		removeEntry(getEntry(key));
	}

	public synchronized void clear() {
		cache.clear();
		size = 0;
		head = null;
		tail = null;
	}
	
	protected final Entry<K,V> getEntry(Object key) {
		Entry<K,V> entry = this.cache.get(key);
		if (entry==null) {
			return null;
		}

		Entry<K,V> prev = entry.prev;

		// move entry to head of LRU list, if it's not already the head (head's prev==null)
		if (prev != null) {
			Entry<K,V> next = entry.next;

			prev.next = next;
			entry.prev = null;
			entry.next = this.head;
			this.head.prev = entry;
			this.head = entry;
	
			if (next != null) {
				next.prev = prev;
			} else {
				this.tail = prev;
			}
		}
		
		return entry;
	}

	protected static class Entry<K,V> {
		Entry<K,V> prev;
		Entry<K,V> next;

		final K key;
		final V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public String toString() {
			return this.key.toString();	
		}

	}

	protected final void removeEntry(Entry<K,V> entry) {
		this.size--;
		Entry<K,V> prev = entry.prev;
		Entry<K,V> next = entry.next;

		if (prev != null) {
	    	prev.next = next;
		} else {
			// it was the head entry
	    	this.head = next;
	    }

		if (next != null) {
	    	next.prev = prev;
	    } else {
	    	// it was the tail entry
			this.tail = prev;
		}

		this.cache.remove( entry.key );
	}
	
	public void debug() {
		System.out.println("head: "+this.head+" tail: "+this.tail);
		Entry<K,V> e = this.head;
		while (e!=null) {
			System.out.println(e.prev + " <-- " + e + " --> " + e.next);
			e = e.next;
		}
	}

	public synchronized int size() {
		return size;
	}

}
