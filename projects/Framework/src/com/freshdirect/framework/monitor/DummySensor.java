package com.freshdirect.framework.monitor;

import javax.management.ObjectName;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class DummySensor implements WebRequestSensor {

	private static final Category LOGGER = LoggerFactory.getInstance(DummySensor.class);

	public void doSenseAfterRequest(ServletRequest request,
			ServletResponse response) throws Exception {
			LOGGER.warn("Sensed URL: "+((HttpServletRequest) request).getRequestURL().toString());
	}

	public void doSenseBeforeRequest(ServletRequest request,
			ServletResponse response) throws Exception {
	}

	public void sensorStarted(ObjectName parent) throws Exception {
	}

	public void sensorStopped() throws Exception {
	}

}
