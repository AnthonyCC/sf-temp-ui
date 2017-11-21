package com.freshdirect.storeapi.warmup.jmx.service;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.fdstore.FDStoreProperties;

public class RuntimeMBeanConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeMBeanConnector.class);

    public static MBeanServer connectToRuntimeMBeanServer() {
        Context initialContext;
        MBeanServer server = null;
        try {
            String jndiName;
            if (System.getProperty("appNode", "false").equalsIgnoreCase("true")) {
                jndiName = "java:comp/jmx/runtime";
            } else {
                jndiName = "java:comp/env/jmx/runtime";
            }
            initialContext = FDStoreProperties.getInitialContext();
            server = (MBeanServer) initialContext.lookup(jndiName);
        } catch (NamingException e) {
            LOGGER.error("Error while connecting to runtime MBean Server", e);
        }
        return server;
    }
}
