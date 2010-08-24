package com.freshdirect.transadmin.web.model;

import java.util.Date;

public class Capacity implements java.io.Serializable {
		
	private Date deliveryStartTime;
	private Date deliveryEndTime;
	private boolean manuallyClosed;
	private String referenceId;
	private boolean dynamicActive;
	
	int openCount = 0;
	int closedCount = 0;
	int dynamicActiveCount = 0;
	int dynamicInActiveCount = 0;
	
	private boolean isDiscounted;
	
	@Override
	public String toString() {
		return "Capacity [deliveryEndTime=" + deliveryEndTime
				+ ", deliveryStartTime=" + deliveryStartTime + "]";
	}
	
	
	public boolean isDiscounted() {
		return isDiscounted;
	}

	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}

	public int getClosedCount() {
		return closedCount;
	}

	public void setClosedCount(int closedCount) {
		this.closedCount = closedCount;
	}

	public int getDynamicActiveCount() {
		return dynamicActiveCount;
	}

	public void setDynamicActiveCount(int dynamicActiveCount) {
		this.dynamicActiveCount = dynamicActiveCount;
	}

	public int getDynamicInActiveCount() {
		return dynamicInActiveCount;
	}

	public void setDynamicInActiveCount(int dynamicInActiveCount) {
		this.dynamicInActiveCount = dynamicInActiveCount;
	}

	public boolean isDynamicActive() {
		return dynamicActive;
	}
	public void setDynamicActive(boolean dynamicActive) {
		this.dynamicActive = dynamicActive;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public boolean isManuallyClosed() {
		return manuallyClosed;
	}
	public void setManuallyClosed(boolean manuallyClosed) {
		this.manuallyClosed = manuallyClosed;
	}
	public Date getDeliveryStartTime() {
		return deliveryStartTime;
	}
	public void setDeliveryStartTime(Date deliveryStartTime) {
		this.deliveryStartTime = deliveryStartTime;
	}
	public Date getDeliveryEndTime() {
		return deliveryEndTime;
	}
	public void setDeliveryEndTime(Date deliveryEndTime) {
		this.deliveryEndTime = deliveryEndTime;
	}
	private double totalCapacity = 0;
	private double totalConfirmed = 0;
	private double totalAllocated = 0;
	
	public double getTotalCapacity() {
		return totalCapacity;
	}
	public void setTotalCapacity(double totalCapacity) {
		this.totalCapacity = totalCapacity;
	}
	public double getTotalConfirmed() {
		return totalConfirmed;
	}
	public void setTotalConfirmed(double totalConfirmed) {
		this.totalConfirmed = totalConfirmed;
	}
	public double getTotalAllocated() {
		return totalAllocated;
	}
	public void setTotalAllocated(double totalAllocated) {
		this.totalAllocated = totalAllocated;
	}
}
