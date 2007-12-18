/**
 * @author ekracoff
 * Created on May 2, 2005*/

package com.freshdirect.ocf;

import org.apache.log4j.Category;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.framework.util.log.LoggerFactory;


public class QuartzListener extends ApplicationLifecycleListener{
	private static Category LOGGER = LoggerFactory.getInstance(QuartzListener.class);

	public void postStart(ApplicationLifecycleEvent evt) {
		try {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();
		
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		
		LOGGER.info("Started quartz scheduler");
		
	}
}
