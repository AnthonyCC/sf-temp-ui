package com.freshdirect.storeapi.warmup.jmx.service;

import java.lang.management.ManagementFactory;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.framework.util.JMXUtil;

@Service
public class WarmupJmxRegistratorService {
	
	 public static final String WARMUP_JMX_PUBLISHER_BASE_OBJECTNAME = "com.freshdirect.storeapi:Type=Warmup,Name=WarmupJmx-" + UUID.randomUUID(); 
	 
    public static final String WARMUP_JMX_PUBLISHER_OBJECTNAME = WARMUP_JMX_PUBLISHER_BASE_OBJECTNAME + ",Location=" + System.getProperty("weblogic.Name");

    private static final Logger LOGGER = LoggerFactory.getLogger(WarmupJmxRegistratorService.class);

    @Autowired
    private WarmupJmx warmupJmx;

    @PostConstruct
    public void registerServices() {
    	if("2".equals(JMXUtil.getStorefrontVersion())) {
    		registerWarmupMBeans();
    	} else {
    		registerWeblogicWarmupMBeans();    		
    	}
    }

    public void registerWeblogicWarmupMBeans() {
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
    
    public void registerWarmupMBeans() {
    	LOGGER.info("Start Registering Generic MBean...");
        final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
        	ObjectName  objectName = new ObjectName(WARMUP_JMX_PUBLISHER_BASE_OBJECTNAME);
            server.registerMBean(warmupJmx, objectName);
            LOGGER.info("MBean registered: " + objectName);
        } catch (Exception mone) {
        	LOGGER.info("Genric MBean already registered or error registering Mbean.");
            mone.printStackTrace();
        }
    }
    
}
