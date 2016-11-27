package com.freshdirect.fdstore.cache;

public class CacheEntryIdentifier {
	
	private String cacheName;
	private Object entryKey;

	public CacheEntryIdentifier(String cacheName, Object entryKey) {
		this.cacheName = cacheName;
		this.entryKey = entryKey;
	}

	public Object getEntryKey() {
		return entryKey;
	}

	public String getCacheName() {
		return cacheName;
	}

	public String toString() {
		return getId();
	}

	private String getId() {
		return cacheName + "::" + entryKey;
	}

	public int hashCode() {
		return getId().hashCode();
	}

	public boolean equals(Object o) {
		return (o instanceof CacheEntryIdentifier) && this.getId().equals(((CacheEntryIdentifier) o).getId());
	}
}
