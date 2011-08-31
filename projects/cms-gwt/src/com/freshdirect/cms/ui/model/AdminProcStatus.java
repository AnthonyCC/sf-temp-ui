package com.freshdirect.cms.ui.model;

import java.io.Serializable;

public class AdminProcStatus implements Serializable {
    String current;
    String lastReindexResult;
    boolean running;

    long started;
    long elapsedTime;

    public AdminProcStatus() {
    }

    public AdminProcStatus(String current, String lastReindexResult, boolean running, long started, long elapsedTime) {
        super();
        this.current = current;
        this.lastReindexResult = lastReindexResult;
        this.running = running;
        this.started = started;
        this.elapsedTime = elapsedTime;
    }

    public String getCurrent() {
        return current;
    }

    public String getLastReindexResult() {
        return lastReindexResult;
    }

    public boolean isRunning() {
        return running;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public long getStarted() {
        return started;
    }

}
