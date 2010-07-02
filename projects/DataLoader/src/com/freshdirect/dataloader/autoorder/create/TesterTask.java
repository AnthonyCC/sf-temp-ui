package com.freshdirect.dataloader.autoorder.create;

import java.util.Date;

import com.freshdirect.dataloader.autoorder.create.command.ITesterCommand;
import com.freshdirect.dataloader.autoorder.create.command.TesterCommand;


/** Uses a SwingWorker to perform geocoding of addresses(time comsu. */

public class TesterTask {
    private ITesterCommand testerCommand;
    

    public TesterTask(Date filename, String skuPath, String customerNo, String customerPrefix, String type) {
    	testerCommand = new TesterCommand();
    	testerCommand.init(filename, skuPath, customerNo, customerPrefix, type);
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
        return testerCommand.getTotal();
    }

    /**
     * find out how much has been done.
     */
    public int getCurrent() {
        return testerCommand.getCount();
    }

    /**
     * find out if the task has completed.
     */
    public boolean isDone() {
        return testerCommand.isDone();
    }

    /**
     * Returns the most recent status message, or null
     * if there is no current status message.
     */
    public String getMessage() {
        return testerCommand.getStatusMessage();
    }
    
    
    
    public long getElapsedTime(){
    	return testerCommand.getElapsedTime();
    }

    /**
     * The actual long running task.  This runs in a SwingWorker thread.
     */
    class ActualTask {
        ActualTask() {
        	testerCommand.load();
        }
    }
}
