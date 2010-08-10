package com.freshdirect.routing.model;


public interface IHandOffBatchSession {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	public String getSessionName();
	void setSessionName(String sessionName);
		
	String getRegion();
	void setRegion(String region);
	
	boolean isDepot();
}
