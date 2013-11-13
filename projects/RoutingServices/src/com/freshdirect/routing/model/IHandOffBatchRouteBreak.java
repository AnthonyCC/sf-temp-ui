package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiSendHandOff;


public interface IHandOffBatchRouteBreak extends BapiSendHandOff.HandOffRouteBreakIn {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	String getSessionName();
	void setSessionName(String sessionName);
	
	void setRouteId(String routeId);
	void setBreakId(String breakId);
	void setStartTime(Date startTime);
	void setEndTime(Date endTime);
	
}
