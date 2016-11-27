package com.freshdirect.dataloader.geocodefilter.ui;

import com.freshdirect.dataloader.geocodefilter.GFLoader;

/** Uses a SwingWorker to perform geocoding of addresses(time comsu. */

public class GFTask {
    private GFLoader loader;
    

    public GFTask(String filename, String destination, boolean filterRestricted){
    	loader = new GFLoader(filename, destination, filterRestricted);
    }
    /**
     * start the task.
     */
    public void go() {
        final SwingWorker worker = new SwingWorker() {
            @Override
            public Object construct() {
                return new ActualTask();
            }
        };
        worker.start();
    }

    /**
     * find out how much work needs
     * to be done.
     */
    public int getLengthOfTask() {
        return loader.getTotal();
    }

    /**
     * find out how much has been done.
     */
    public int getCurrent() {
        return loader.getCount();
    }

    /**
     * find out if the task has completed.
     */
    public boolean isDone() {
        return loader.isDone();
    }

    /**
     * Returns the most recent status message, or null
     * if there is no current status message.
     */
    public String getMessage() {
        return loader.getStatusMessage();
    }
    
    /** returns number of GEOCODE_OK address */
    public int getValids(){
    	return loader.getValids();
    }
    
    /** returns number of GEOCODE_BAD address */
    public int getInvalids(){
    	return loader.getInvalids();
    }
    
    /** returns number of address that through an InvalidAddressException */
    public int getInvalidExceptions(){
    	return loader.getInvalidExceptions();
    }
    
    public long getElapsedTime(){
    	return loader.getElapsedTime();
    }

    /**
     * The actual long running task.  This runs in a SwingWorker thread.
     */
    class ActualTask {
        ActualTask() {
        	loader.load();
        }
    }
}
