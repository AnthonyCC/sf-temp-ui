/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2004 FreshDirect
 *
 */

package com.freshdirect.fdstore.warmup;

import java.util.Properties;
import java.util.logging.Logger;

import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.logging.LoggingHelper;

/**
 * WLSWarmup
 *
 * @version $Revision:$
 * @author $Author:$
 */
public class WLSWarmup extends ApplicationLifecycleListener {

    @Override
    public void preStart(ApplicationLifecycleEvent arg0) throws ApplicationException {
        Logger logger = LoggingHelper.getServerLogger();
        Properties systemProperties = System.getProperties();
        if (systemProperties.containsKey("appNode") && Boolean.parseBoolean(systemProperties.getProperty("appNode"))) {
            logger.info("This is an app node. Initializing spring context manually.");
            WarmupInitiator.initializeSpringContext();
        } else {
            logger.info("This is not an app node. Spring context initialization is going to happen automatically");
        }
    }

    @Override
    public void postStart(ApplicationLifecycleEvent evt) {
    	WarmupInitiator.initWarmup();
    }


}
