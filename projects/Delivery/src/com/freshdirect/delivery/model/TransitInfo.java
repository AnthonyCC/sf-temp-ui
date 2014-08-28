package com.freshdirect.delivery.model;

import java.util.Date;

public class TransitInfo {
	
	private Date transitDate;
	private String route;
	private int nextStop;
	private Date insertTimeStamp;
	private String enployeeId;
	
	
	public String getEnployeeId() {
		return enployeeId;
	}
	public void setEnployeeId(String enployeeId) {
		this.enployeeId = enployeeId;
	}
	public Date getTransitDate() {
		return transitDate;
	}
	public void setTransitDate(Date transitDate) {
		this.transitDate = transitDate;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public int getNextStop() {
		return nextStop;
	}
	public void setNextStop(int nextStop) {
		this.nextStop = nextStop;
	}
	public Date getInsertTimeStamp() {
		return insertTimeStamp;
	}
	public void setInsertTimeStamp(Date insertTimeStamp) {
		this.insertTimeStamp = insertTimeStamp;
	}
	
	

}
