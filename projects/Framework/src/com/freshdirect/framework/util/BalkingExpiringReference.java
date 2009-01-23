package com.freshdirect.framework.util;

/**
 * @author csongor
 */
public abstract class BalkingExpiringReference extends ExpiringReference {
	private Thread loader = null;
	
	private class AsyncLoader extends Thread {
		public void run() {
			Object _new = load();
			
			loaded(_new);
		}	
	}

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 */
	public BalkingExpiringReference(long refreshPeriod) {
		super(refreshPeriod);
	}

	protected boolean isExpired() {
		return System.currentTimeMillis() - lastRefresh > this.refreshPeriod;
	}
	
	public synchronized Object get() {
		reload();
		return this.referent;
	}
	
	protected synchronized void loaded(Object _new) {
		referent = _new;
		lastRefresh = System.currentTimeMillis();
		loader = null;		
	}

	public synchronized void reload() {
		if (loader == null && (referent == null || isExpired())) {
			loader = new AsyncLoader();
			loader.setDaemon(true);
			loader.start();
		}
	}
	
	public synchronized void forceRefresh() {
		if (loader == null) {
			super.forceRefresh();
			reload();
		}
	}
}
