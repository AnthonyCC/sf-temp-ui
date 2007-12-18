/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.service;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

import java.util.Hashtable;
import weblogic.common.T3StartupDef;
import weblogic.common.T3ServicesDef;

/**
 * Weblogic startup class for launching ServiceI instances. Loads and starts the service class by name,
 * specified by the <CODE>serviceLauncher.class</CODE> parameter. It can optionally perform an asynchronous
 * delayed initialization. The delay is specified in seconds by the <CODE>serviceLauncher.delay</CODE> param.
 * 
 * <P>
 * Example of weblogic.properties file:
 * <PRE>
 * weblogic.system.startupClass.blahService=com.freshdirect.framework.service.T3ServiceLauncher
 * weblogic.system.startupArgs.blahService=\
 *		serviceLauncher.class=com.freshdirect.whatever.BlahService,\
 *		serviceLauncher.delay=10,\
 *		otherParamz=passedToService,\
 *		blahStuff=passedToService
 * </PRE>
 *
 * @version $Revision$
 * @author $Author$
 */
public class T3ServiceLauncher implements T3StartupDef {

	private static Category LOGGER = LoggerFactory.getInstance( T3ServiceLauncher.class );

	private T3ServicesDef services;

	public void setServices(T3ServicesDef services) {
		this.services = services;
	}
	
	public String startup(String name, Hashtable args) throws Exception {
		String serviceClass = (String)args.get("serviceLauncher.class");
		if (serviceClass==null) {
			throw new IllegalArgumentException("T3ServiceLauncher - please specify serviceLauncher.class");
		}
		ServiceI service = (ServiceI) Class.forName(serviceClass).newInstance();
		
		service.configure(args);
		
		String serviceDelay = (String)args.get("serviceLauncher.delay");
		if (serviceDelay==null) {
			// no delay, start immediately
			service.start();
			return "Service " + name + " started";
		} else {
			// asynch delay, start new thread
			int delay = Integer.parseInt(serviceDelay);
			new StartupThread(name, delay, service).start();
			return "Asynch init of service " + name + " in " + delay + " seconds";
		}
	}

	private static class StartupThread extends Thread {
		private String name;
		private int delaySeconds;
		private ServiceI service;
		
		public StartupThread(String name, int delaySeconds, ServiceI service) {
			this.name=name;
			this.delaySeconds=delaySeconds;
			this.service=service;
		}
		
		public void run() {
			try {
				sleep( delaySeconds * 1000 );
				LOGGER.info("Starting service " + name);
				service.start();
				LOGGER.info("Service " + name + " started");
			} catch (ServiceException e) {
				LOGGER.error("Failed to start service "+name, e);
			} catch (InterruptedException e) {}
		}
	}


}