package com.freshdirect.routing.model;

import java.io.Serializable;
import java.util.Date;

public class StandingOrderModel implements Serializable{
	
	private String id;
	private String orderId;
	private String status;
	private String errorHeader;
	private Date altDate;
	private Date startTime;
	private Date endTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorHeader() {
		return errorHeader;
	}
	public void setErrorHeader(String errorHeader) {
		this.errorHeader = errorHeader;
	}
	public Date getAltDate() {
		return altDate;
	}
	public void setAltDate(Date altDate) {
		this.altDate = altDate;
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
