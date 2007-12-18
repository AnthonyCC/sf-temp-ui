package com.freshdirect.framework.monitor;

public interface WebStatsSensorMBean {

	public void resetStats();
	
	public void clearAllStats();
	
	public String[] getURIs();
	
	public int getMaxStatsSize();
	
	public void setMaxStatsSize(int size);
	
	public void startMBeans();
	
	public void stopMBeans();
	
	public void startMBean(String uri);
	
	public void stopMBean(String uri);

}
