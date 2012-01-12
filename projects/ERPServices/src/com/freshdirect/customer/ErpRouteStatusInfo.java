package com.freshdirect.customer;

import java.io.Serializable;

public class ErpRouteStatusInfo implements Serializable {
	
	private String routeNumber;	
	private String status;
	private String statusDesc;
		
	public ErpRouteStatusInfo(String routeNumber, String status, String statusDesc) {
		
		this.routeNumber = routeNumber;
		this.status = status;
		this.statusDesc = statusDesc;
	}
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
