package com.freshdirect.framework.monitor;

import javax.management.ObjectName;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface WebRequestSensor {

	public void doSenseBeforeRequest(ServletRequest request, ServletResponse response) throws Exception;

	public void doSenseAfterRequest(ServletRequest request, ServletResponse response) throws Exception;
	
	public void sensorStarted(ObjectName parent) throws Exception;
	
	public void sensorStopped() throws Exception;
}
