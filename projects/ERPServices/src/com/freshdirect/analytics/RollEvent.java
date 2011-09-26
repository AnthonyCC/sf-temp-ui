package com.freshdirect.analytics;

import java.util.Date;

public class RollEvent extends EventModel {

	public RollEvent(String customerId, Date createDate, float unavailablePct,
			String zone, Date cutOff, String logId, Date deliveryDate) {
		super();
		this.customerId = customerId;
		this.createDate = createDate;
		this.unavailablePct = unavailablePct;
		this.zone = zone;
		this.cutOff = cutOff;
		this.logId = logId;
		this.deliveryDate = deliveryDate;
	}
	private String customerId;
	private Date createDate;
	private float unavailablePct;
	private String zone;
	private Date cutOff;
	private String logId;
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
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public float getUnavailablePct() {
		return unavailablePct;
	}
	public void setUnavailablePct(float unavailablePct) {
		this.unavailablePct = unavailablePct;
	}
	public Date getCutOff() {
		return cutOff;
	}
	public void setCutOff(Date cutOff) {
		this.cutOff = cutOff;
	}
	
}
