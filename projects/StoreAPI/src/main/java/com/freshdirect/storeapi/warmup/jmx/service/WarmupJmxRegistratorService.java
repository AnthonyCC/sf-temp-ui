package com.freshdirect.storeapi.warmup.jmx.service;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarmupJmxRegistratorService {

    public static final String WARMUP_JMX_PUBLISHER_OBJECTNAME = "com.freshdirect.storeapi:Type=Warmup,Name=WarmupJmx,Location=" + System.getProperty("weblogic.Name");

    private static final Logger LOGGER = LoggerFactory.getLogger(WarmupJmxRegistratorService.class);

    @Autowired
    private WarmupJmx warmupJmx;

    @PostConstruct
    public void registerServices() {
        registerWarmupMBeans();
    }

    public void registerWarmupMBeans() {
        LOGGER.debug("Registering WarmupJmxPublisher");
        try {

            MBeanServer connection = RuntimeMBeanConnector.connectToRuntimeMBeanServer();
            if (connection != null) {
                ObjectName warmupJmxPublisherMbeanName = new ObjectName(WARMUP_JMX_PUBLISHER_OBJECTNAME);

                connection.registerMBean(warmupJmx, warmupJmxPublisherMbeanName);
            } else {
                LOGGER.error("Couldn't establish connection to Runtime MBean Server");
            }
        } catch (Exception e) {
            LOGGER.error("Error while registering JMX Warmup MBeans", e);
        }
    }

}
