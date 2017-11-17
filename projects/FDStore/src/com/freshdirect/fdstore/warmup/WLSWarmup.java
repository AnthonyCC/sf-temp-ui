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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.freshdirect.cms.configuration.RootConfiguration;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.storeapi.configuration.StoreAPIConfig;

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
            initializeSpringContext();
        } else {
            logger.info("This is not an app node. Spring context initialization is going to happen automatically");
        }
    }

    @Override
    public void postStart(ApplicationLifecycleEvent evt) {
        Logger logger = LoggingHelper.getServerLogger();
        /**
         * Test code
         */

        if (FDStoreProperties.performStorePreLoad()) {
            Class warmupClass = Warmup.class;
            String className = FDStoreProperties.getWarmupClass();
            if (className != null) {
                try {
                    warmupClass = Class.forName(className);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not find Warmup class " + className + " fallback to default.", e);
                }
            }
            try {
                logger.info("initiating warmup using class '" + warmupClass.getName() + "'");
                Warmup warmup = (Warmup) warmupClass.newInstance();
                Warmup.WARMUP_STATE.set(WarmupState.IN_PROGRESS);
                warmup.warmup();
                Warmup.WARMUP_STATE.set(WarmupState.FINISHED);
                logger.info("warmup completed");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "error during warmup", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void initializeSpringContext() {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfiguration.class);
        rootContext.register(StoreAPIConfig.class);
        rootContext.refresh();
    }

}
