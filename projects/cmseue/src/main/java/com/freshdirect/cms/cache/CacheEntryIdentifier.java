package com.freshdirect.cms.cache;

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

    @Override
    public String toString() {
        return getId();
    }

    private String getId() {
        return cacheName + "::" + entryKey;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CacheEntryIdentifier) && this.getId().equals(((CacheEntryIdentifier) o).getId());
    }
}
