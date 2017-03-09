package com.freshdirect.cms.publish.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * A very simple task timer implementation
 * It measures the time a task took to run.
 * 
 * Ticking starts at the time instance is created
 * and ends when logTimeSpent method is invoked.
 * 
 * @author segabor
 *
 */
public class TaskTimer {
    private final long startTime = System.currentTimeMillis();
    
    private String taskName = null;
    
    private Priority logPriority = Priority.DEBUG;

    public TaskTimer(String taskName, Priority logPriority) {
        this.taskName = taskName;
        this.logPriority = logPriority;
    }

    public TaskTimer(String taskName) {
        this(taskName, Priority.DEBUG);
    }

    public void logTimeSpent(Logger logger, String msg) {
        final long dt = System.currentTimeMillis() - startTime;

        logger.log(logPriority, "** Task <" + taskName + "> completed in " +(dt/1000)+" sec) **");
        if (msg != null) {
            logger.log(logPriority, "** " + msg + " **");
        }
    }
}
