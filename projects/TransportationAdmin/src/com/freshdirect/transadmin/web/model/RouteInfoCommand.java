package com.freshdirect.transadmin.web.model;

import java.io.Serializable;

public class RouteInfoCommand implements Serializable {
	
	private String loadStatus;
	private String serviceStatus;
	private String currentLocation;
	private String fueled;
	
	public String getLoadStatus() {
		return loadStatus;
	}
	public void setLoadStatus(String loadStatus) {
		this.loadStatus = loadStatus;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	public String getFueled() {
		return fueled;
	}
	public void setFueled(String fueled) {
		this.fueled = fueled;
	}
}
