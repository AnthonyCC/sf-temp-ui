package com.freshdirect.analytics;

import java.util.Date;

public class OrderData {

	private double orderCount;
	private double projectedCount;
	private Date projectedSoldOut;
	private String zone;
	private String startTime;
	private String endime;
	private int capacity;
	private double ordersExpected;
	private double utilization;
	public double getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(double orderCount) {
		this.orderCount = orderCount;
	}
	public double getProjectedCount() {
		return projectedCount;
	}
	public void setProjectedCount(double projectedCount) {
		this.projectedCount = projectedCount;
	}
	public Date getProjectedSoldOut() {
		return projectedSoldOut;
	}
	public void setProjectedSoldOut(Date projectedSoldOut) {
		this.projectedSoldOut = projectedSoldOut;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndime() {
		return endime;
	}
	public void setEndime(String endime) {
		this.endime = endime;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public double getOrdersExpected() {
		return ordersExpected;
	}
	public void setOrdersExpected(double ordersExpected) {
		this.ordersExpected = ordersExpected;
	}
	public double getUtilization() {
		return utilization;
	}
	public void setUtilization(double utilization) {
		this.utilization = utilization;
	}
}
