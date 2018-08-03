package com.freshdirect.storeapi.warmup.jmx.service;

import java.lang.management.ManagementFactory;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
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
    	LOGGER.info("Registering MBean...");
        final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
        	ObjectName  objectName = new ObjectName(WARMUP_JMX_PUBLISHER_BASE_OBJECTNAME);
            server.registerMBean(warmupJmx, objectName);
            System.out.println("MBean registered: " + objectName);
        } catch (MalformedObjectNameException mone) {
            mone.printStackTrace();
        } catch (InstanceAlreadyExistsException iaee) {
            iaee.printStackTrace();
        } catch (MBeanRegistrationException mbre) {
            mbre.printStackTrace();
        } catch (NotCompliantMBeanException ncmbe) {
            ncmbe.printStackTrace();
        }
    }
    
}
