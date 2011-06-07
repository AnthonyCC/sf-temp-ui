package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiSendHandOff;
import com.freshdirect.sap.bapi.BapiSendPhysicalTruckInfo;

public interface IHandOffBatchRoute extends IRouteModel, BapiSendHandOff.HandOffRouteIn, BapiSendPhysicalTruckInfo.HandOffRouteTruckIn {	

	String getBatchId();
	void setBatchId(String batchId);
	
	String getArea();
	void setArea(String area);
	
	String getSessionName();
	void setSessionName(String sessionName);
	
	String getTruckNumber();
	void setTruckNumber(String truckNumber);
		
}
