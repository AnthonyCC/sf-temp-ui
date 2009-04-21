package com.freshdirect.framework.util;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.Executor;

/**
 * @author csongor
 */
public abstract class BalkingExpiringReference extends ExpiringReference {
	final static Category LOGGER = LoggerFactory.getInstance(BalkingExpiringReference.class);

	private Runnable loader = null;
	
	private Executor executor = new Executor() {
		public void execute(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.start();
		}
	};
	
	private class AsyncLoader implements Runnable {
		public void run() {
			LOGGER.debug("task is scheduled for execution.");
			Object _new = load();
			
			loaded(_new);
			LOGGER.debug("task is finished.");
		}	
	}

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 */
	public BalkingExpiringReference(long refreshPeriod) {
		super(refreshPeriod);
	}

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 */
	public BalkingExpiringReference(long refreshPeriod, Executor executor) {
		super(refreshPeriod);
		this.executor = executor;
	}

	public BalkingExpiringReference(long refreshPeriod, Executor executor, Object initializer) {
		super(refreshPeriod);
		this.referent = initializer;
		this.executor = executor;
		lastRefresh = System.currentTimeMillis();
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
			executor.execute(loader);
		}
	}
	
	public synchronized void forceRefresh() {
		if (loader == null) {
			super.forceRefresh();
			reload();
		}
	}
}
