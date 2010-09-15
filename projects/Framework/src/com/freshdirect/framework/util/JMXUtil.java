package com.freshdirect.framework.util;

import java.lang.management.ManagementFactory;
import java.util.Hashtable;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class JMXUtil {

	private static final Logger LOGGER = LoggerFactory.getInstance(JMXUtil.class);
	
	public static final String FRESHDIRECT_DOMAIN = "com.freshdirect";
	
	static MBeanServer server ;
	
	private static MBeanServer getLocalMBeanServer() throws Exception {
	    if (server == null) {
		server = MBeanServerFactory.newMBeanServer();
	    }
	    return server;
	}
	
	public static ObjectName registerMBean(Object mbean, String type, String name) {
		try {
			Hashtable pList = new Hashtable();
			pList.put("Type", type);
			pList.put("Name", name);
			ObjectName objName = new ObjectName(FRESHDIRECT_DOMAIN, pList);
			getLocalMBeanServer().registerMBean(mbean, objName);
			return objName;
		} catch (Exception e) {
			LOGGER.warn("Failed to register MBean "+type+","+name, e);
			return null;
		}
	}

	public static void unregisterMBean(ObjectName objName) {
		try {
			getLocalMBeanServer().unregisterMBean(objName);
		} catch (Exception e) {
			LOGGER.warn("Failed to unregister MBean with ObjectName "+objName, e);
		}		
	}

	public static void registerMBean(Object mbean, ObjectName objName) {
		try {
			getLocalMBeanServer().registerMBean(mbean, objName);
		} catch (Exception e) {
			LOGGER.warn("Failed to register MBean "+objName, e);
			throw new RuntimeException("Failed to register MBean",e);
		}
	}			
	
	public static boolean isRegistered(ObjectName objName) {
		try {
			return getLocalMBeanServer().getMBeanInfo(objName) != null;
		} catch (InstanceNotFoundException e) {
			return false;
		}
		catch (Exception e) {
			LOGGER.warn("Failed to register MBean "+objName, e);
			throw new RuntimeException("Failed to register MBean",e);
		}		
	}

	public static ObjectName createObjectNameFromParent(ObjectName parent, String key, String value) throws Exception {
		Hashtable t = new Hashtable(parent.getKeyPropertyList());
		t.put(key, value);
		return new ObjectName(JMXUtil.FRESHDIRECT_DOMAIN, t);
	}
	
	
	public static boolean registerToPlatformMBean(Object mbean, String type, String name, String value) {
            try {
                ManagementFactory.getPlatformMBeanServer().registerMBean(mbean, new ObjectName(type, name.replace(':', '_').replace(',', '_'), value.replace(':', '_').replace(',', '_')));
                return true;
            } catch (InstanceAlreadyExistsException e) {
                LOGGER.info(e);
            } catch (MBeanRegistrationException e) {
                LOGGER.info(e);
            } catch (NotCompliantMBeanException e) {
                LOGGER.info(e);
            } catch (MalformedObjectNameException e) {
                LOGGER.info(e);
            } catch (NullPointerException e) {
                LOGGER.info(e);
            }
            return false;
	}
	
}
