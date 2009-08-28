package com.freshdirect.routing.model;

public class DeliverySlotCost extends BaseModel implements IDeliverySlotCost  {
	
	private int additionalDistance;
	private int additionalRunTime;
	private int additionalStopCost;
	private boolean available;
	private int capacity;
	private int costPerMile;
	private boolean filtered;
	private int fixedRouteSetupCost;
	private int maxRunTime;
	private boolean missedTW;
	private int overtimeHourlyWage;
	private int prefRunTime;
	private int regularHourlyWage;
	private int regularWageDurationSeconds;
	private int routeId;
	private int stopSequence;
	private int totalDistance;
	private int totalPUQuantity;
	private int totalQuantity;
	private int totalRouteCost;
	private int totalRunTime;
	private int totalServiceTime;
	private int totalTravelTime;
	private int totalWaitTime;
	public int getAdditionalDistance() {
		return additionalDistance;
	}
	public void setAdditionalDistance(int additionalDistance) {
		this.additionalDistance = additionalDistance;
	}
	public int getAdditionalRunTime() {
		return additionalRunTime;
	}
	public void setAdditionalRunTime(int additionalRunTime) {
		this.additionalRunTime = additionalRunTime;
	}
	public int getAdditionalStopCost() {
		return additionalStopCost;
	}
	public void setAdditionalStopCost(int additionalStopCost) {
		this.additionalStopCost = additionalStopCost;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getCostPerMile() {
		return costPerMile;
	}
	public void setCostPerMile(int costPerMile) {
		this.costPerMile = costPerMile;
	}
	public boolean isFiltered() {
		return filtered;
	}
	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}
	public int getFixedRouteSetupCost() {
		return fixedRouteSetupCost;
	}
	public void setFixedRouteSetupCost(int fixedRouteSetupCost) {
		this.fixedRouteSetupCost = fixedRouteSetupCost;
	}
	public int getMaxRunTime() {
		return maxRunTime;
	}
	public void setMaxRunTime(int maxRunTime) {
		this.maxRunTime = maxRunTime;
	}
	public boolean isMissedTW() {
		return missedTW;
	}
	public void setMissedTW(boolean missedTW) {
		this.missedTW = missedTW;
	}
	public int getOvertimeHourlyWage() {
		return overtimeHourlyWage;
	}
	public void setOvertimeHourlyWage(int overtimeHourlyWage) {
		this.overtimeHourlyWage = overtimeHourlyWage;
	}
	public int getPrefRunTime() {
		return prefRunTime;
	}
	public void setPrefRunTime(int prefRunTime) {
		this.prefRunTime = prefRunTime;
	}
	public int getRegularHourlyWage() {
		return regularHourlyWage;
	}
	public void setRegularHourlyWage(int regularHourlyWage) {
		this.regularHourlyWage = regularHourlyWage;
	}
	public int getRegularWageDurationSeconds() {
		return regularWageDurationSeconds;
	}
	public void setRegularWageDurationSeconds(int regularWageDurationSeconds) {
		this.regularWageDurationSeconds = regularWageDurationSeconds;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public int getStopSequence() {
		return stopSequence;
	}
	public void setStopSequence(int stopSequence) {
		this.stopSequence = stopSequence;
	}
	public int getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}
	public int getTotalPUQuantity() {
		return totalPUQuantity;
	}
	public void setTotalPUQuantity(int totalPUQuantity) {
		this.totalPUQuantity = totalPUQuantity;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public int getTotalRouteCost() {
		return totalRouteCost;
	}
	public void setTotalRouteCost(int totalRouteCost) {
		this.totalRouteCost = totalRouteCost;
	}
	public int getTotalRunTime() {
		return totalRunTime;
	}
	public void setTotalRunTime(int totalRunTime) {
		this.totalRunTime = totalRunTime;
	}
	public int getTotalServiceTime() {
		return totalServiceTime;
	}
	public void setTotalServiceTime(int totalServiceTime) {
		this.totalServiceTime = totalServiceTime;
	}
	public int getTotalTravelTime() {
		return totalTravelTime;
	}
	public void setTotalTravelTime(int totalTravelTime) {
		this.totalTravelTime = totalTravelTime;
	}
	public int getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(int totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
}
