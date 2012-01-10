package com.freshdirect.customer;

import java.io.Serializable;

public class ErpRouteStatusInfo implements Serializable {
	
	private String routeNumber;	
	private String routeStatus;
	
	public ErpRouteStatusInfo(String routeNumber, String routeStatus) {
		super();
		this.routeNumber = routeNumber;
		this.routeStatus = routeStatus;
	}
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	public String getRouteStatus() {
		return routeStatus;
	}
	public void setRouteStatus(String routeStatus) {
		this.routeStatus = routeStatus;
	}
}
