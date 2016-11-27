package com.freshdirect.sap.bapi;

import com.freshdirect.customer.ErpRouteStatusInfo;

public interface BapiRouteStatusInfo extends BapiFunctionI {
	
	void addRequest(String requestedDate, String routeNumber);
	
	ErpRouteStatusInfo getRouteStatusInfo();
}
