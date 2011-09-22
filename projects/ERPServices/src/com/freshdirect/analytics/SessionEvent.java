package com.freshdirect.analytics;

import java.util.Date;

public class SessionEvent {

	private String customerId;
	private Date loginTime;
	private Date logoutTime;
	private Date cutOff;
	private Integer availCount;
	private Integer soldCount;
	private String zone;
	private String lastTimeslot;
	private String isTimeout = "Y";
	private String orderPlaced;
	private String pageType;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	public Date getCutOff() {
		return cutOff;
	}
	public void setCutOff(Date cutOff) {
		this.cutOff = cutOff;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getLastTimeslot() {
		return lastTimeslot;
	}
	public void setLastTimeslot(String lastTimeslot) {
		this.lastTimeslot = lastTimeslot;
	}
	public String getIsTimeout() {
		return isTimeout;
	}
	public void setIsTimeout(String isTimeout) {
		this.isTimeout = isTimeout;
	}
	public Integer getAvailCount() {
		return availCount;
	}
	public void setAvailCount(Integer availCount) {
		this.availCount = availCount;
	}
	public Integer getSoldCount() {
		return soldCount;
	}
	public void setSoldCount(Integer soldCount) {
		this.soldCount = soldCount;
	}
	public String getOrderPlaced() {
		return orderPlaced;
	}
	public void setOrderPlaced(String orderPlaced) {
		this.orderPlaced = orderPlaced;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
}
