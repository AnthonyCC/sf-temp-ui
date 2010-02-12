package com.freshdirect.framework.util;

/**
 * Wrapper for caching a single object, with timed expiration and appropriate synchronization. 
 */
public abstract class ExpiringReference<X> {

	protected final long refreshPeriod;

	protected long lastRefresh = 0;

	protected X referent;

	/**
	 * @param refreshPeriod in milliseconds
	 */
	public ExpiringReference(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public synchronized X get() {
		if (System.currentTimeMillis() - lastRefresh > this.refreshPeriod) {
			this.referent = this.load();

			lastRefresh = System.currentTimeMillis();
		}
		return this.referent;
	}

	/** Template method that gets invoked whenever the reference has to be loaded */
	protected abstract X load();

	public synchronized void forceRefresh() {
		lastRefresh = 0;
	}

	public synchronized void set(X value) {
		referent = value;
		lastRefresh = System.currentTimeMillis();
	}

	protected boolean isExpired() {
		return System.currentTimeMillis() - lastRefresh > this.refreshPeriod;
	}
}
