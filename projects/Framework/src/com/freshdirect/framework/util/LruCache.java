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
 * "Least-recently-used" cache.
 *
 * @version $Revision$
 * @author $Author$
 */
public class LruCache {

	private final static boolean ASSERTIONS = false;

	private final int capacity;
	private final Map cache;

	private int size = 0;
	private Entry head = null;
	private Entry tail = null;

	public LruCache(int capacity) {
		if (capacity<2) throw new IllegalArgumentException("Capacity must be at least 2");
		this.capacity = capacity;
		this.cache = new HashMap(capacity);
	}

	public synchronized void put(Object key, Object value) {
		this.putEntry( new Entry(key, value) );
	}

	protected final void putEntry(Entry entry) {
		Entry oldEntry = (Entry)this.cache.get(entry.key);
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


	public synchronized Object get(Object key) {
		Entry e = this.getEntry(key);
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
	
	protected final Entry getEntry(Object key) {
		Entry entry = (Entry)this.cache.get(key);
		if (entry==null) {
			return null;
		}

		Entry prev = entry.prev;

		// move entry to head of LRU list, if it's not already the head (head's prev==null)
		if (prev != null) {
			Entry next = entry.next;

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

	protected static class Entry {
		Entry prev;
		Entry next;

		final Object key;
		final Object value;

		public Entry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String toString() {
			return this.key.toString();	
		}

	}

	protected final void removeEntry(Entry entry) {
		this.size--;
		Entry prev = entry.prev;
		Entry next = entry.next;

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
		Entry e = this.head;
		while (e!=null) {
			System.out.println(e.prev + " <-- " + e + " --> " + e.next);
			e = e.next;
		}
	}

	public synchronized int size() {
		return size;
	}

}
