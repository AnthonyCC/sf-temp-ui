package com.freshdirect.cms.ui.model;

import java.io.Serializable;

public class AdminProcStatus implements Serializable {
    String current;
    String lastReindexResult;
    boolean running;
    
    long started;
    long elapsedTime;

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getLastReindexResult() {
        return lastReindexResult;
    }

    public void setLastReindexResult(String lastReindexResult) {
        this.lastReindexResult = lastReindexResult;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
    public long getElapsedTime() {
        return elapsedTime;
    }
    
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
    public long getStarted() {
        return started;
    }
    
    public void setStarted(long started) {
        this.started = started;
    }

}
