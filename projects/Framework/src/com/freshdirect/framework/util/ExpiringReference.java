package com.freshdirect.framework.util;

/**
 * Wrapper for caching a single object, with timed expiration and appropriate synchronization. 
 */
public abstract class ExpiringReference {

	protected final long refreshPeriod;

	protected long lastRefresh = 0;

	protected Object referent;

	/**
	 * @param refreshPeriod in milliseconds
	 */
	public ExpiringReference(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public synchronized Object get() {
		if (System.currentTimeMillis() - lastRefresh > this.refreshPeriod) {
			this.referent = this.load();

			lastRefresh = System.currentTimeMillis();
		}
		return this.referent;
	}

	/** Template method that gets invoked whenever the reference has to be loaded */
	protected abstract Object load();

	public synchronized void forceRefresh() {
		lastRefresh = 0;
	}
	
}
