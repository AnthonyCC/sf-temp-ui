package com.freshdirect.framework.cache;

public interface ManagedCacheMBean {
	
	public int getCacheElements();
	
	public int getCacheNullElements();
	
	public long getCacheQueries();
	
	public long getCacheHits();
	
	public long getCacheNullHits();
	
	public long getCacheMisses();
	
	public boolean isNullElementStats();
	
	public void startNullElementStats();
	
	public void stopNullElementStats();
	
	public void clearStats();
	
	public void clearCache();
	
}
