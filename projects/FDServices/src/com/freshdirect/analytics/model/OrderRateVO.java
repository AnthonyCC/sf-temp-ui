package com.freshdirect.analytics.model;

import java.util.Date;

public class OrderRateVO {

	private int capacity;
	private String zone;
	private Date cutoffTime;
	private Date startTime;
	private Date endTime;
	private float orderCount;
	private Date baseDate;
	private Date snapshotTime;
	private String snapshotTimeFmt;
	private float projectedRate;
	private Date soldOutTime;
	private float weightedProjectRate;
	private Date expectedSoldOutTime;
	private float ordersExpected;
	private String cutoffTimeFormatted;
	private String startTimeFormatted;
	
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
	public float getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(float orderCount) {
		this.orderCount = orderCount;
	}
	public Date getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}
	public Date getSnapshotTime() {
		return snapshotTime;
	}
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	public String getSnapshotTimeFmt() {
		return snapshotTimeFmt;
	}
	public void setSnapshotTimeFmt(String snapshotTimeFmt) {
		this.snapshotTimeFmt = snapshotTimeFmt;
	}
	public float getProjectedRate() {
		return projectedRate;
	}
	public void setProjectedRate(float projectedRate) {
		this.projectedRate = projectedRate;
	}
	public Date getSoldOutTime() {
		return soldOutTime;
	}
	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}
	public float getWeightedProjectRate() {
		return weightedProjectRate;
	}
	public void setWeightedProjectRate(float weightedProjectRate) {
		this.weightedProjectRate = weightedProjectRate;
	}
	public Date getExpectedSoldOutTime() {
		return expectedSoldOutTime;
	}
	public void setExpectedSoldOutTime(Date expectedSoldOutTime) {
		this.expectedSoldOutTime = expectedSoldOutTime;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public float getOrdersExpected() {
		return ordersExpected;
	}
	public void setOrdersExpected(float ordersExpected) {
		this.ordersExpected = ordersExpected;
	}
	public String getCutoffTimeFormatted() {
		return cutoffTimeFormatted;
	}
	public void setCutoffTimeFormatted(String cutoffTimeFormatted) {
		this.cutoffTimeFormatted = cutoffTimeFormatted;
	}
	public String getStartTimeFormatted() {
		return startTimeFormatted;
	}
	public void setStartTimeFormatted(String startTimeFormatted) {
		this.startTimeFormatted = startTimeFormatted;
	}
	
	
}
