package com.freshdirect.analytics.model;


public class PlantDispatchData {

	private String dispatchTime;
	private int plantCapacity;
	private int plannedCapacity;
	private int cumlplannedCapacity;
	private int totalOrders;
	private int trucks;
	private int orders;
	public String getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public int getPlantCapacity() {
		return plantCapacity;
	}
	public void setPlantCapacity(int plantCapacity) {
		this.plantCapacity = plantCapacity;
	}
	public int getPlannedCapacity() {
		return plannedCapacity;
	}
	public void setPlannedCapacity(int plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}
	public int getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}
	public int getTrucks() {
		return trucks;
	}
	public void setTrucks(int trucks) {
		this.trucks = trucks;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public int getCumlplannedCapacity() {
		return cumlplannedCapacity;
	}
	public void setCumlplannedCapacity(int cumlplannedCapacity) {
		this.cumlplannedCapacity = cumlplannedCapacity;
	}

	
}
