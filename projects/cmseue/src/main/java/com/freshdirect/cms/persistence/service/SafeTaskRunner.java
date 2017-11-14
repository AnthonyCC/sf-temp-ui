package com.freshdirect.cms.persistence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SafeTaskRunner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SafeTaskRunner.class);

    private final String taskName;

    public SafeTaskRunner(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        LOGGER.info(taskName + " load started");
        try {
            final long t0 = System.currentTimeMillis();

            load();

            final long t1 = System.currentTimeMillis();

            LOGGER.info(taskName + " completed in " + Long.toString(t1 - t0) + " ms");
        } catch (Exception exc) {
            LOGGER.error(taskName + " load failed", exc);
        }
    }

    abstract void load();
}
