package com.freshdirect.dashboard.model;

public class DashboardSummary {
	
	private int totalOrders;
	
	private int projectedOPTCnt;
	
	private int projectedOrderCnt;
	
	private int maxPlantCapacity;
	
	private int totalPlannedCapacity;
	
	private int resourceCnt;

	public int getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}

	public int getProjectedOPTCnt() {
		return resourceCnt > 0 ? (int) projectedOrderCnt / resourceCnt : 0;
	}

	public int getProjectedOrderCnt() {
		return projectedOrderCnt;
	}

	public void setProjectedOrderCnt(int projectedOrderCnt) {
		this.projectedOrderCnt = projectedOrderCnt;
	}

	public int getMaxPlantCapacity() {
		return maxPlantCapacity;
	}

	public void setMaxPlantCapacity(int maxPlantCapacity) {
		this.maxPlantCapacity = maxPlantCapacity;
	}

	public int getTotalPlannedCapacity() {
		return totalPlannedCapacity;
	}

	public void setTotalPlannedCapacity(int totalPlannedCapacity) {
		this.totalPlannedCapacity = totalPlannedCapacity;
	}

	public int getResourceCnt() {
		return resourceCnt;
	}

	public void setResourceCnt(int resourceCnt) {
		this.resourceCnt = resourceCnt;
	}
	
	
	
}
