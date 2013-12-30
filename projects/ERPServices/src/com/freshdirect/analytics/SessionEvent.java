package com.freshdirect.analytics;

import java.util.Date;

public class SessionEvent extends EventModel{

	private String orderId;
	private String customerId;
	private Date loginTime;
	private Date logoutTime;
	private Date cutOff;
	private Integer availCount;
	private Integer soldCount;
	private Integer hiddenCount;
	
	private String zone;
	private String lastTimeslot;
	private String isTimeout = "Y";
	private String pageType;
	private String sector;
	private String sameDay;
	private String clientIp;
	private String serverName;
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
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getHiddenCount() {
		return hiddenCount;
	}
	public void setHiddenCount(Integer hiddenCount) {
		this.hiddenCount = hiddenCount;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getSameDay() {
		return sameDay;
	}
	public void setSameDay(String sameDay) {
		this.sameDay = sameDay;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
