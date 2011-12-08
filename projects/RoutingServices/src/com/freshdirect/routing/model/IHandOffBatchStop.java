package com.freshdirect.routing.model;

import com.freshdirect.sap.bapi.BapiSendHandOff;


public interface IHandOffBatchStop extends IRoutingStopModel, BapiSendHandOff.HandOffStopIn {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	String getSessionName();
	void setSessionName(String sessionName);
	
	String getRouteId();
	void setRouteId(String routeId);
	
	String getRoutingRouteId();
	void setRoutingRouteId(String routingRouteId);
	
	boolean isException();
	void setException(boolean isException);
	
	
	void setServiceAddress2(String serviceAddress2);
}
