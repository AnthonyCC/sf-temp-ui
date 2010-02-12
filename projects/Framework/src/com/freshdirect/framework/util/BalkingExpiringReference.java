package com.freshdirect.framework.util;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author csongor
 */
public abstract class BalkingExpiringReference<X> extends ExpiringReference<X> {
	final static Category LOGGER = LoggerFactory.getInstance(BalkingExpiringReference.class);

	private Runnable loader = null;
	
	private final Executor executor;
	
	private class AsyncLoader implements Runnable {
		public void run() {
			LOGGER.debug("task is scheduled for execution.");
			try {
				X _new = load();
				loaded(_new);
				LOGGER.debug("task is finished.");
			} catch (Exception e) {
				loaded(referent);
				LOGGER.debug("task failed.");
			}
		}	
	}

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 */
	public BalkingExpiringReference(long refreshPeriod) {
		this(refreshPeriod, new Executor() {
			public void execute(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				t.start();
			}
		});
	}

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 * @param executor
	 *            executor, usually a thread pool. cannot be null
	 */
	public BalkingExpiringReference(long refreshPeriod, Executor executor) {
		this(refreshPeriod, executor, false);
	}

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 * @param executor
	 *            executor, usually a thread pool. cannot be null
	 * @param syncInit
	 *            if true initialize its value synchronously
	 */
	public BalkingExpiringReference(long refreshPeriod, Executor executor, boolean syncInit) {
		super(refreshPeriod);
		this.executor = executor;
		if (syncInit) {
			set(load());
		}
	}
	
	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 * @param executor
	 *            executor, usually a thread pool. cannot be null
	 * @param initializer
	 *            pre-initialize the reference until the new value is generated
	 */
	public BalkingExpiringReference(long refreshPeriod, Executor executor, X initializer) {
		super(refreshPeriod);
		this.referent = initializer;
		this.executor = executor;
		set(initializer);
	}
	
	public synchronized X get() {
		reload();
		return this.referent;
	}
	
	protected synchronized void loaded(X _new) {
		if (referent == _new)
			// failed next time will retry
			lastRefresh = 0;
		else
			lastRefresh = System.currentTimeMillis();
		referent = _new;
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
