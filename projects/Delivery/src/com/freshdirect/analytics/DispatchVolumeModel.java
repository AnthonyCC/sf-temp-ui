package com.freshdirect.analytics;

import java.util.Date;

import com.freshdirect.routing.util.RoutingTimeOfDay;

public class DispatchVolumeModel {
	private RoutingTimeOfDay dispatchTime;
	private Date snapshotTime;
	private Date dispatchDate;
	private double plannedCapacity;
	private int plantCapacity;
	private int orderCount;
	private int noOftrucks;
	public RoutingTimeOfDay getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(RoutingTimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public double getPlannedCapacity() {
		return plannedCapacity;
	}
	public void setPlannedCapacity(double plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}
	public int getPlantCapacity() {
		return plantCapacity;
	}
	public void setPlantCapacity(int plantCapacity) {
		this.plantCapacity = plantCapacity;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public int getNoOftrucks() {
		return noOftrucks;
	}
	public void setNoOftrucks(int noOftrucks) {
		this.noOftrucks = noOftrucks;
	}
	
	public Date getDispatchDate() {
		return dispatchDate;
	}
	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
	public Date getSnapshotTime() {
		return snapshotTime;
	}
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	@Override
	public String toString() {
		return "DispatchVolumeModel [dispatchTime=" + dispatchTime
				+ ", snapshotTime=" + snapshotTime + ", dispatchDate="
				+ dispatchDate + ", plannedCapacity=" + plannedCapacity
				+ ", plantCapacity=" + plantCapacity + ", orderCount="
				+ orderCount + ", noOftrucks=" + noOftrucks + "]";
	}
	
}
