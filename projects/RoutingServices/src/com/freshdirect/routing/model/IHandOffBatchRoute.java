package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiSendHandOff;

public interface IHandOffBatchRoute extends IRouteModel, BapiSendHandOff.HandOffRouteIn {	

	String getBatchId();
	void setBatchId(String batchId);
	
	String getArea();
	void setArea(String area);
	
	String getSessionName();
	void setSessionName(String sessionName);
		
}
