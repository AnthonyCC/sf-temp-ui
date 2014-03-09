package com.freshdirect.dashboard.model;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.dashboard.util.DateUtil;

public class PlantDispatchData {

	private Date dispatchTime;
	private int plantCapacity;
	private int plannedCapacity;
	private int trucks;
	private int orders;
	private int allocatedOrders;
	private String shift;
		
	private int cumlPlannedCapacity;
	private int cumlOrders;
	private int cumlAllocatedOrders;
	private int cumlTrucks;
	private int projectedOrders;
	private int productionCapacity;
	
	private int currentUtilization;
	private int liveSpareCapacity;
	
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
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
	public int getCumlPlannedCapacity() {
		return cumlPlannedCapacity;
	}
	public void setCumlPlannedCapacity(int cumlPlannedCapacity) {
		this.cumlPlannedCapacity = cumlPlannedCapacity;
	}
	public int getAllocatedOrders() {
		return allocatedOrders;
	}
	public void setAllocatedOrders(int allocatedOrders) {
		this.allocatedOrders = allocatedOrders;
	}
	public int getCumlOrders() {
		return cumlOrders;
	}
	public void setCumlOrders(int cumlOrders) {
		this.cumlOrders = cumlOrders;
	}
	public int getCumlAllocatedOrders() {
		return cumlAllocatedOrders;
	}
	public void setCumlAllocatedOrders(int cumlAllocatedOrders) {
		this.cumlAllocatedOrders = cumlAllocatedOrders;
	}
	public int getCumlTrucks() {
		return cumlTrucks;
	}
	public void setCumlTrucks(int cumlTrucks) {
		this.cumlTrucks = cumlTrucks;
	}
	public int getProjectedOrders() {
		return projectedOrders;
	}
	public void setProjectedOrders(int projectedOrders) {
		this.projectedOrders = projectedOrders;
	}
	public int getProductionCapacity() {
		return productionCapacity;
	}
	public void setProductionCapacity(int productionCapacity) {
		this.productionCapacity = productionCapacity;
	}	
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public int getCurrentUtilization() {
		return currentUtilization;
	}
	public void setCurrentUtilization(int currentUtilization) {
		this.currentUtilization = currentUtilization;
	}
	public int getLiveSpareCapacity() {
		return liveSpareCapacity;
	}
	public void setLiveSpareCapacity(int liveSpareCapacity) {
		this.liveSpareCapacity = liveSpareCapacity;
	}
	public String getDispatchTimeEx() {
		if(this.getDispatchTime() != null) {
			try {
				return DateUtil.getServerTime(this.dispatchTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
