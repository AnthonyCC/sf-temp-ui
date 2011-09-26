package com.freshdirect.analytics;

import java.util.Date;

public class BounceEvent extends EventModel {

	public BounceEvent(String customerId, String status, Date createDate,
			Date cutOff, String zone, String logId, String pageType,
			Date deliveryDate) {
		super();
		this.customerId = customerId;
		this.status = status;
		this.createDate = createDate;
		this.cutOff = cutOff;
		this.zone = zone;
		this.logId = logId;
		this.pageType = pageType;
		this.deliveryDate = deliveryDate;
		System.out.println();
	}
	private String customerId;
	private String status;
	private Date createDate;
	private Date updateDate;
	private Date cutOff;
	private String zone;
	private String logId;
	private String pageType;
	private Date deliveryDate;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
