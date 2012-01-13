package com.freshdirect.sap.command;

import com.freshdirect.customer.ErpRouteStatusInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiRouteStatusInfo;
import com.freshdirect.sap.ejb.SapException;

public class SapRouteStatusInfo extends SapCommandSupport {
	
	private String requestedDate;
	private String routeNumber;
	private ErpRouteStatusInfo routeInfo;
	
	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}	

	public SapRouteStatusInfo(String requestedDate, String routeNumber) {
		super();
		this.requestedDate = requestedDate;
		this.routeNumber = routeNumber;
	}

	@Override
	public void execute() throws SapException {
		BapiRouteStatusInfo bapi = BapiFactory.getInstance().getBapiRouteStatusInfoBuilder();
		bapi.addRequest(requestedDate, routeNumber);
		this.invoke(bapi);
		
		routeInfo = bapi.getRouteStatusInfo();
	}
	
	public ErpRouteStatusInfo getRouteStatusInfo(){
		return routeInfo;
	}
	

}
