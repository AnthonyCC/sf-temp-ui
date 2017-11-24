package com.freshdirect.webapp.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class TomcatBootStartupListener implements ServletContextListener {
	
	private final static Logger LOGGER = LoggerFactory.getInstance(TomcatBootStartupListener.class);
	
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	LOGGER.info("FDTomcat Server Startup Begin.........");    	
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	LOGGER.info("FDTomcat Server Shutdown Complete.........");
    }


}
