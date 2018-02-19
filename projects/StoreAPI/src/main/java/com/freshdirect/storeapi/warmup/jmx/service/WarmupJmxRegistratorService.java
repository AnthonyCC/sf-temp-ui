package com.freshdirect.storeapi.warmup.jmx.service;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarmupJmxRegistratorService {

    public static final String WARMUP_JMX_PUBLISHER_OBJECTNAME = "com.freshdirect.storeapi:Type=Warmup,Name=WarmupJmx" + UUID.randomUUID() + ",Location=" + System.getProperty("weblogic.Name");

    private static final Logger LOGGER = LoggerFactory.getLogger(WarmupJmxRegistratorService.class);

    @Autowired
    private WarmupJmx warmupJmx;

    @PostConstruct
    public void registerServices() {
        registerWarmupMBeans();
    }

    public void registerWarmupMBeans() {
        LOGGER.info("Registering WarmupJmxPublisher");
        try {

            MBeanServer connection = RuntimeMBeanConnector.connectToRuntimeMBeanServer();
            if (connection != null) {
                ObjectName warmupJmxPublisherMbeanName = new ObjectName(WARMUP_JMX_PUBLISHER_OBJECTNAME);

                connection.registerMBean(warmupJmx, warmupJmxPublisherMbeanName);
                
                LOGGER.info("Registered WarmupJmxPublisher:"+warmupJmxPublisherMbeanName);
            } else {
                LOGGER.error("WarmupJmxPublisher Couldn't establish connection to Runtime MBean Server");
            }
        } catch (Exception e) {
            LOGGER.error("WarmupJmxPublisher Error while registering JMXWarmupMBeans", e);
        }
    }

}
