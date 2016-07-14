package com.freshdirect.cms.search;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class BackgroundStatus implements Cloneable {
    private final static Logger LOG = LoggerFactory.getInstance(BackgroundStatus.class);
    
    private String current;
    private String lastReindexResult;
    private boolean running;
    private long started;
    private long elapsedTime;

    public String getCurrent() {
        return current;
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
    
    @Override
    public BackgroundStatus clone() {
        try {
            return (BackgroundStatus) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void notifyStart() {
        LOG.info("start");
        setRunning(true);
        setStarted(System.currentTimeMillis());
    }

    public void setStatus(String message) {
        LOG.info("status:" + message);
        this.current = message;
    }

    public void notifiyFinished() {
        setElapsedTime(System.currentTimeMillis() - getStarted());
        LOG.info("finished in :"+(getElapsedTime()/1000)+" sec");
        setRunning(false);
    }

    public void notifiyError(Throwable e) {
        setRunning(false);
        setElapsedTime(System.currentTimeMillis() - getStarted());
        this.current = "failure: " + e.getMessage();
        this.lastReindexResult = "see logs for details";
        LOG.error("error during task execution:" + e.getMessage(), e);
    }

}
