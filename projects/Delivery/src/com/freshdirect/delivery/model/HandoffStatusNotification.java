package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.util.Date;

public class HandoffStatusNotification implements Serializable {
	
	private String batchId;
	private Date deliveryDate;
	private Date cutoffTime;
	private Date commitTime;
		
	public HandoffStatusNotification() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getCutoffTime() {
		return cutoffTime;
	}
	public void setCutoffTime(Date cutoffTime) {
		this.cutoffTime = cutoffTime;
	}
	public Date getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
}
