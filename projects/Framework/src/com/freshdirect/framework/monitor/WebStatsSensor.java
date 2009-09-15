package com.freshdirect.framework.monitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.ObjectName;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class WebStatsSensor implements WebStatsSensorMBean, WebRequestSensor {

	private final Category LOGGER = LoggerFactory.getInstance(WebStatsSensor.class);
	
	ThreadLocal startTimeTL;

	private static final int DEFAULT_HASH_SIZE = 10000;
	
	private ConcurrentHashMap monitorData;
	int maxHashSize;
	private boolean registerMBeans;
	ObjectName jmxObjectName;
	private String name;
	
	public WebStatsSensor(String name) {
		this.name = name;
	}
	
	
	public void doSenseBeforeRequest(ServletRequest request,
			ServletResponse response) throws Exception {
		
		startTimeTL.set(new Long(System.currentTimeMillis()));
	}

	public void doSenseAfterRequest(ServletRequest request,
			ServletResponse response) throws Exception {

		long endTime = System.currentTimeMillis();

		long startTime = ((Long) startTimeTL.get()).longValue();

		assert (startTime > 0);
	
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			String uri = req.getRequestURI();
			UriStats data = (UriStats) monitorData.get(uri);
			if (data == null) {
				if (monitorData.size() > maxHashSize) return ;
				
				UriStats newData = new UriStats(uri, jmxObjectName);
				data = (UriStats) monitorData.putIfAbsent(uri, newData);
				if (data == null) {
					data = newData;
					synchronized (this) {
						if (registerMBeans) {
							JMXUtil.registerMBean(data, data.getObjectName());
						}
					}
				}
			}
			data.updateStats(startTime, endTime);
		} catch (Exception e) {
			LOGGER.warn("Exception occurred during Web Request stat computation", e);
			throw e ;
		}
		
	}


	public void sensorStarted(ObjectName parent) throws Exception {
		synchronized (this) {
			monitorData = new ConcurrentHashMap();
			startTimeTL = new ThreadLocal();
			this.maxHashSize = DEFAULT_HASH_SIZE;
			jmxObjectName = JMXUtil.createObjectNameFromParent(parent, "Sensor", this.name);
			JMXUtil.registerMBean(this, jmxObjectName);
			registerMBeans = true;
		}
	}

	public void sensorStopped() throws Exception {
		synchronized (this) {
			registerMBeans = false;
			for (Iterator it = monitorData.values().iterator(); it.hasNext();) {
				UriStats m = (UriStats) it.next();
				JMXUtil.unregisterMBean(m.getObjectName());			
			}
			monitorData = null;
			JMXUtil.unregisterMBean(jmxObjectName);
		}
	}

	public void resetStats() {
		for (Iterator it = monitorData.values().iterator(); it.hasNext();) {
			UriStats m = (UriStats) it.next();
			m.clearStats();
		}
	}
	
	public int getMaxStatsSize() {
		return maxHashSize;
	}

	public String[] getURIs() {
		Set keys = new HashSet(monitorData.keySet());
		return (String[]) keys.toArray(new String[keys.size()]);
	}

	public void setMaxStatsSize(int size) {
		this.maxHashSize = size;
	}


	public void startMBean(String uri) {
		synchronized (this) {
			UriStats us = (UriStats) monitorData.get(uri);
			if (us == null) return ;
			if (JMXUtil.isRegistered(us.getObjectName())) return ;
			try {
				JMXUtil.registerMBean(us, us.getObjectName());
			} catch (Exception e) {
				LOGGER.warn("Unable to start UriStat MBean: "+uri, e);
			}
		}
	}


	public void startMBeans() {
		synchronized (this) {
			registerMBeans = true;
			for (Iterator it = monitorData.values().iterator(); it.hasNext();) {
				UriStats us = (UriStats) it.next();				
				if (JMXUtil.isRegistered(us.getObjectName())) continue ;
				try {
					JMXUtil.registerMBean(us, us.getObjectName());
				} catch (Exception e) {
					LOGGER.warn("Unable to start UriStat MBean: "+us.getObjectName(), e);
				}
			}
		}
	}


	public void stopMBean(String uri) {
		synchronized (this) {
			UriStats us = (UriStats) monitorData.get(uri);
			if (us == null) return ;
			if (!JMXUtil.isRegistered(us.getObjectName())) return ;
			try {
				JMXUtil.unregisterMBean(us.getObjectName());
			} catch (Exception e) {
				LOGGER.warn("Unable to stop UriStat MBean: "+uri, e);
			}
		}
	}


	public void stopMBeans() {
		synchronized (this) {
			registerMBeans = false;
			for (Iterator it = monitorData.values().iterator(); it.hasNext();) {
				UriStats us = (UriStats) it.next();				
				if (!JMXUtil.isRegistered(us.getObjectName())) continue ;
				try {
					JMXUtil.unregisterMBean(us.getObjectName());
				} catch (Exception e) {
					LOGGER.warn("Unable to stop UriStat MBean: "+us.getObjectName(), e);
				}
			}
		}
	}
	
	public void clearAllStats() {
		synchronized (this) {
			registerMBeans = false;
			for (Iterator it = monitorData.values().iterator(); it.hasNext();) {
				UriStats us = (UriStats) it.next();				
				if (!JMXUtil.isRegistered(us.getObjectName())) continue ;
				try {
					JMXUtil.unregisterMBean(us.getObjectName());
				} catch (Exception e) {
					LOGGER.warn("Unable to stop UriStat MBean: "+us.getObjectName(), e);
				}
			}
			monitorData.clear();
			registerMBeans = true;
		}		
	}

}
