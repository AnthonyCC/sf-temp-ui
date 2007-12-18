package com.freshdirect.framework.monitor;


public interface WebRequestMonitorMBean {
	
	public String[] getSensors();

	public String[] getStartedSensors();

	public String[] getStoppedSensors();
	
	public void startSensor(String sensorName);

	public void startSensors();
	
	public void stopSensors();
	
	public void stopSensor(String sensorName);
	
	public void unloadSensor(String sensorName);
}
