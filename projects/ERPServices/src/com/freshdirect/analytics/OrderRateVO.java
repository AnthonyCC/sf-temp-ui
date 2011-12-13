package com.freshdirect.analytics;

import java.sql.Timestamp;
import java.util.Date;

public class OrderRateVO {

	private int capacity;
	private String zone;
	private Timestamp cutoffTime;
	private Timestamp startTime;
	private Timestamp endTime;
	private float orderCount;
	private Date baseDate;
	private Timestamp snapshotTime;
	private float projectedRate;
	private Timestamp soldOutTime;
	private float weightedProjectRate;
	private Date expectedSoldOutTime;
	private float ordersExpected;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Date getCutoffTime() {
		return cutoffTime;
	}
	public void setCutoffTime(Timestamp cutoffTime) {
		this.cutoffTime = cutoffTime;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
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
	public Timestamp getSnapshotTime() {
		return snapshotTime;
	}
	public void setSnapshotTime(Timestamp snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	public float getProjectedRate() {
		return projectedRate;
	}
	public void setProjectedRate(float projectedRate) {
		this.projectedRate = projectedRate;
	}
	public Timestamp getSoldOutTime() {
		return soldOutTime;
	}
	public void setSoldOutTime(Timestamp soldOutTime) {
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
	
	
}
