package com.freshdirect.framework.util;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author csongor
 */
public abstract class BalkingExpiringReference<X> extends ExpiringReference<X> {
	final static Category LOGGER = LoggerFactory.getInstance(BalkingExpiringReference.class);

	private Runnable loader = null;
	
	private class AsyncLoader implements Runnable {
		@Override
        public void run() {
			LOGGER.debug("task is scheduled for execution.");
			try {
				X _new = load();
				loaded(_new);
				LOGGER.debug("task is finished.");
			} catch (Exception e) {
				loaded(referent);
				LOGGER.debug("task failed.", e);
			}
		}	
	}

    /**
     * @param refreshPeriod
     *            in milliseconds
     */
    public BalkingExpiringReference(long refreshPeriod) {
        this(refreshPeriod, false);
    }

	/**
	 * @param refreshPeriod
	 *            in milliseconds
	 * @param executor
	 *            executor, usually a thread pool. cannot be null
	 * @param syncInit
	 *            if true initialize its value synchronously
	 */
    public BalkingExpiringReference(long refreshPeriod, boolean syncInit) {
		super(refreshPeriod);
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
    public BalkingExpiringReference(long refreshPeriod, X initializer) {
		super(refreshPeriod);
		this.referent = initializer;
		set(initializer);
	}
	
	@Override
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

	/**
	 * Initiate asynchronous loading, if it's not loaded already, or it's expired.
	 */
	public synchronized void reload() {
		if (loader == null && (referent == null || isExpired())) {
			loader = new AsyncLoader();
            Thread refreshThread = new Thread(loader);
            refreshThread.setDaemon(false);
            refreshThread.start();
		}
	}
	
	/**
	 * force asynchronous loading.
	 */
	@Override
    public synchronized void forceRefresh() {
		if (loader == null) {
			super.forceRefresh();
			reload();
		}
	}
}
