package com.freshdirect.framework.monitor;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Category;


import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class WebRequestMonitor implements Filter, WebRequestMonitorMBean {

	private static final Category LOGGER = LoggerFactory.getInstance(WebRequestMonitor.class);

	public static final String JNDI_CONTEXT="freshdirect.monitor.WebRequestMonitor";
	
	FilterConfig config;
	ObjectName jmxObjectName;
	boolean registerMBeans;
	
	ThreadLocal filterApp;
	
	ConcurrentHashMap sensors;
	
	ConcurrentHashMap startedSensors;
	
	ConcurrentHashMap stoppedSensors;

	private boolean noSensorStart;
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (config == null) return;

		boolean invokeFilter = false;
		
		if (filterApp.get() == null) {
			filterApp.set(Boolean.TRUE);
			invokeFilter = true;
		}

		if (invokeFilter) {
			invokeSensorsBefore(request, response);
		}
		
		chain.doFilter(request, response);

		if (invokeFilter) {
			filterApp.set(null);
			invokeSensorsAfter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		try {
			synchronized (this) {
				this.noSensorStart = true; // Disable sensor registrations until the end of setup
				this.config = config;
				this.filterApp = new ThreadLocal();
				sensors = new ConcurrentHashMap();
				startedSensors = new ConcurrentHashMap();
				stoppedSensors = new ConcurrentHashMap();
				jmxObjectName = JMXUtil.registerMBean(this, "WebRequestMonitor", config.getFilterName());
				if (jmxObjectName == null) {
					LOGGER.warn("Unable to register WebRequestMonitor MBean, Sensors disabled.");
					return ;
				}
				registerJNDI(); // Make sure that other components can find us
				startDefaultSensor(); // Start the default sensor
				this.noSensorStart = false; // Enable sensor registrations
			}
		} catch (Exception e) {
			LOGGER.error("Unable to start Filter: "+config.getFilterName(),e);
			throw new ServletException("Unable to start Filter: "+config.getFilterName(),e);
		}
	}

	private void registerJNDI() throws Exception {
		InitialContext ctx = new InitialContext();
		NameParser np = ctx.getNameParser(ctx.getNameInNamespace());
		Name n = np.parse(JNDI_CONTEXT);
		Context subctx = ctx;
		for (Enumeration ne = n.getAll();ne.hasMoreElements();) {
			String c = (String) ne.nextElement();
			subctx = subctx.createSubcontext(c);
		}
		ctx.bind(JNDI_CONTEXT+"."+config.getFilterName(), this);
		ctx.close();
	}
	private void unregisterJNDI() throws Exception {
		InitialContext ctx = new InitialContext();
		ctx.unbind(JNDI_CONTEXT+"."+config.getFilterName());
		ctx.close();
	}

	public void destroy() {
		try {
			synchronized (this) {
				this.noSensorStart = true;
				unregisterJNDI();
				stopSensors();
				JMXUtil.unregisterMBean(jmxObjectName);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while stopping filter: "+config.getFilterName(),e);			
		}
	}

	public String[] getSensors() {
		Set keys = new HashSet(sensors.keySet());
		return (String[]) keys.toArray(new String[keys.size()]);
	}

	public String[] getStartedSensors() {
		Set keys = new HashSet(startedSensors.keySet());
		return (String[]) keys.toArray(new String[keys.size()]);
	}

	public String[] getStoppedSensors() {
		Set keys = new HashSet(stoppedSensors.keySet());
		return (String[]) keys.toArray(new String[keys.size()]);
	}

	public void startSensor(String sensorName) {
		synchronized (this) {
			try {
				if (noSensorStart) return ;
				if (startedSensors.get(sensorName) != null) return ;
				WebRequestSensor s = (WebRequestSensor) sensors.get(sensorName);
				if (s == null) return ; // Unknown sensor
				s.sensorStarted(jmxObjectName);
				startedSensors.put(sensorName, s);
				stoppedSensors.remove(sensorName);
			} catch (Exception e) {
				LOGGER.warn("Could not start sensor: "+sensorName,e);
				// Removing the badly behaving sensor from sensor lists
				sensors.remove(sensorName);
				startedSensors.remove(sensorName);
				stoppedSensors.remove(sensorName);
			}
		}
	}

	public void stopSensor(String sensorName) {
		synchronized (this) {
			try {
				if (startedSensors.get(sensorName) == null) return ;
				WebRequestSensor s = (WebRequestSensor) sensors.get(sensorName);
				if (s == null) return ; // Unknown sensor
				WebRequestSensor r = (WebRequestSensor) startedSensors.remove(sensorName);
				assert (s == r);
				s.sensorStopped();
				stoppedSensors.put(sensorName, s);
			} catch (Exception e) {
				LOGGER.warn("Could not properly stop sensor: "+sensorName,e);
				// Removing the badly behaving sensor from sensor lists
				sensors.remove(sensorName);
				startedSensors.remove(sensorName);
				stoppedSensors.remove(sensorName);
			}
		}
	}

	public void unloadSensor(String sensorName) {
		synchronized (this) {
			try {
				if (startedSensors.get(sensorName) == null) return ;
				WebRequestSensor s = (WebRequestSensor) sensors.get(sensorName);
				if (s == null) return ; // Unknown sensor
				WebRequestSensor r = (WebRequestSensor) startedSensors.remove(sensorName);
				assert (s == r);
				s.sensorStopped();
				sensors.remove(sensorName);
			} catch (Exception e) {
				LOGGER.warn("Could not properly unload sensor: "+sensorName,e);
				// Removing the badly behaving sensor from sensor lists
				sensors.remove(sensorName);
				startedSensors.remove(sensorName);
				stoppedSensors.remove(sensorName);
			} 
		}
	}

	public void startSensors() {
		synchronized (this) {
			for (Iterator it = sensors.keySet().iterator(); it.hasNext();) {
				String name = (String) it.next();
				startSensor(name);
			}
		}		
	}

	public void stopSensors() {
		synchronized (this) {
			for (Iterator it = sensors.keySet().iterator(); it.hasNext();) {
				String name = (String) it.next();
				stopSensor(name);
			}			
		}
	}
	
	public boolean addSensor(String name, WebRequestSensor sensor) throws Exception {
		synchronized (this) {
			if (noSensorStart) return false;
			if (sensors.get(name) != null) return false;
			sensors.put(name, sensor);
			try {
				sensor.sensorStarted(jmxObjectName);
				startedSensors.put(name, sensor);
				return true;
			} catch (Exception e) {
				LOGGER.warn("Unable to add sensor: "+name, e);
				sensors.remove(name);
				throw e;
			}
		}
	}
		
	private void invokeSensorsBefore(ServletRequest request, ServletResponse response) {
		for (Iterator it = startedSensors.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			WebRequestSensor s = (WebRequestSensor) e.getValue();
			try {
				s.doSenseBeforeRequest(request, response);
			} catch (Exception ex) {
				LOGGER.warn("Exception during filter invocation: "+e.getKey(), ex);
			}
		}
	}	

	private void invokeSensorsAfter(ServletRequest request, ServletResponse response) {
		for (Iterator it = startedSensors.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			WebRequestSensor s = (WebRequestSensor) e.getValue();
			try {
				s.doSenseAfterRequest(request, response);
			} catch (Exception ex) {
				LOGGER.warn("Exception during filter invocation: "+e.getKey(), ex);
			}
		}
	}

	private void startDefaultSensor() throws Exception {
		synchronized (this) {
			WebRequestSensor s = new WebStatsSensor("WebStats");
			sensors.put("WebStats", s);
			try {
				s.sensorStarted(jmxObjectName);
				startedSensors.put("WebStats", s);
			} catch (Exception e) {
				LOGGER.error("Unable to start default sensor: WebStats",e);
				sensors.remove("WebStats");
				throw e;
			}			
		}				
	}
	
}
