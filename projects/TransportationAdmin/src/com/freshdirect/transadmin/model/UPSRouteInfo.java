package com.freshdirect.transadmin.model;

import java.util.Date;

public class UPSRouteInfo 
{
	private long routeKey;
	private String routeNumber;
	private Date routeDate;
	
	private Date startTime;
	private Date lastStop;
	private Date endTime;
	
	public long getRouteKey() {
		return routeKey;
	}
	public void setRouteKey(long routeKey) {
		this.routeKey = routeKey;
	}
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	public Date getRouteDate() {
		return routeDate;
	}
	public void setRouteDate(Date routeDate) {
		this.routeDate = routeDate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getLastStop() {
		return lastStop;
	}
	public void setLastStop(Date lastStop) {
		this.lastStop = lastStop;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	
}
