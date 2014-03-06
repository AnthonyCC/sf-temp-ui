package com.freshdirect.dashboard.model;

import java.util.Date;

public class ProjectedUtilizationVO {

	private String zone;
	
	private Date cutoffTime;
	
	private double plannedCapacity;
	
	private double confirmedOrderCnt;
	
	private double allocatedOrderCnt;
	
	private double expectedOrderCnt;
	
	private int resourceCnt;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Date getCutoffTime() {
		return cutoffTime;
	}
	public void setCutoffTime(Date cutoffTime) {
		this.cutoffTime = cutoffTime;
	}
	public double getPlannedCapacity() {
		return plannedCapacity;
	}
	public void setPlannedCapacity(double plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}
	public double getConfirmedOrderCnt() {
		return confirmedOrderCnt;
	}
	public void setConfirmedOrderCnt(double confirmedOrderCnt) {
		this.confirmedOrderCnt = confirmedOrderCnt;
	}
	public double getAllocatedOrderCnt() {
		return allocatedOrderCnt;
	}
	public void setAllocatedOrderCnt(double allocatedOrderCnt) {
		this.allocatedOrderCnt = allocatedOrderCnt;
	}
	public void setAllocatedOrderCnt(int allocatedOrderCnt) {
		this.allocatedOrderCnt = allocatedOrderCnt;
	}
	public double getExpectedOrderCnt() {
		return expectedOrderCnt;
	}
	public void setExpectedOrderCnt(double expectedOrderCnt) {
		this.expectedOrderCnt = expectedOrderCnt;
	}
	public int getResourceCnt() {
		return resourceCnt;
	}
	public void setResourceCnt(int resourceCnt) {
		this.resourceCnt = resourceCnt;
	}
}
