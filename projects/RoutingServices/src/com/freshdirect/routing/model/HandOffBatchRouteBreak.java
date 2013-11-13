package com.freshdirect.routing.model;

import java.util.Date;


public class HandOffBatchRouteBreak implements IHandOffBatchRouteBreak  {
	
	private String batchId;
	
	private String sessionName;
	
	private String routeId;
	
	private String breakId;
	private Date startTime;
	private Date endTime;
	
	public HandOffBatchRouteBreak(String batchId, String sessionName,String routeId, String breakId, Date startTime, Date endTime) {
		super();
		this.batchId = batchId;		
		this.sessionName = sessionName;
		this.routeId = routeId;		
		this.breakId = breakId;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public HandOffBatchRouteBreak() {
		super();
	}
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getBreakId() {
		return breakId;
	}

	public void setBreakId(String breakId) {
		this.breakId = breakId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	
}
