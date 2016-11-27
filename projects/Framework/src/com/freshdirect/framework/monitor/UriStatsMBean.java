package com.freshdirect.framework.monitor;

public interface UriStatsMBean {

	public String getURI();
			
	public int getHitCount();

	public long getMaxExecTime();

	public long getMinExecTime();

	public double getAvgExecTime();
	
	public void clearStats();

}
