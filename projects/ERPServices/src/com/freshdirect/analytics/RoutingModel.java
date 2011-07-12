package com.freshdirect.analytics;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author tbalumuri
 *
 */
public class RoutingModel implements Serializable {

	public RoutingModel(int additionalDistance, int additionalRunTime,
			int additionalStopCost, int capacity, int costPerMile,
			int fixedRouteSetupCost, int maxRunTime, int overtimeHourlyWage,
			int prefRunTime, int regularHourlyWage,
			int regularWageDurationSeconds, int routeId, int stopSequence,
			int totalDistance, int totalPUQuantity, int totalQuantity,
			int totalRouteCost, int totalRunTime, int totalServiceTime,
			int totalTravelTime, int totalWaitTime, boolean available,
			boolean filtered, boolean missedTW, int waveVehicles, 
			int waveVehiclesInUse, Date waveStartTime,String unavailabilityReason,
			int waveOrdersTaken, double totalQuantities, boolean newRoute,
			double capacities) {
		super();
		this.additionalDistance = additionalDistance;
		this.additionalRunTime = additionalRunTime;
		this.additionalStopCost = additionalStopCost;
		this.capacity = capacity;
		this.costPerMile = costPerMile;
		this.fixedRouteSetupCost = fixedRouteSetupCost;
		this.maxRunTime = maxRunTime;
		this.overtimeHourlyWage = overtimeHourlyWage;
		this.prefRunTime = prefRunTime;
		this.regularHourlyWage = regularHourlyWage;
		this.regularWageDurationSeconds = regularWageDurationSeconds;
		this.routeId = routeId;
		this.stopSequence = stopSequence;
		this.totalDistance = totalDistance;
		this.totalPUQuantity = totalPUQuantity;
		this.totalQuantity = totalQuantity;
		this.totalRouteCost = totalRouteCost;
		this.totalRunTime = totalRunTime;
		this.totalServiceTime = totalServiceTime;
		this.totalTravelTime = totalTravelTime;
		this.totalWaitTime = totalWaitTime;
		this.available = available;
		this.filtered = filtered;
		this.missedTW = missedTW;
		this.unavailabilityReason = unavailabilityReason;
		this.waveVehicles = waveVehicles;
		this.waveVehiclesInUse = waveVehiclesInUse;
		this.waveStartTime = waveStartTime;
		this.waveOrdersTaken = waveOrdersTaken;
		this.totalQuantities = totalQuantities;
		this.newRoute = newRoute;
		this.capacities = capacities;
	}
	/**
	This object encapsulates cost related metrics received from UPS call.
	 */
	
	public RoutingModel(int additionalDistance, int additionalRunTime,
			int additionalStopCost, int capacity, int costPerMile,
			int fixedRouteSetupCost, int maxRunTime, int overtimeHourlyWage,
			int prefRunTime, int regularHourlyWage,
			int regularWageDurationSeconds, int routeId, int stopSequence,
			int totalDistance, int totalPUQuantity, int totalQuantity,
			int totalRouteCost, int totalRunTime, int totalServiceTime,
			int totalTravelTime, int totalWaitTime, boolean available,
			boolean filtered, boolean missedTW) {
		super();
		this.additionalDistance = additionalDistance;
		this.additionalRunTime = additionalRunTime;
		this.additionalStopCost = additionalStopCost;
		this.capacity = capacity;
		this.costPerMile = costPerMile;
		this.fixedRouteSetupCost = fixedRouteSetupCost;
		this.maxRunTime = maxRunTime;
		this.overtimeHourlyWage = overtimeHourlyWage;
		this.prefRunTime = prefRunTime;
		this.regularHourlyWage = regularHourlyWage;
		this.regularWageDurationSeconds = regularWageDurationSeconds;
		this.routeId = routeId;
		this.stopSequence = stopSequence;
		this.totalDistance = totalDistance;
		this.totalPUQuantity = totalPUQuantity;
		this.totalQuantity = totalQuantity;
		this.totalRouteCost = totalRouteCost;
		this.totalRunTime = totalRunTime;
		this.totalServiceTime = totalServiceTime;
		this.totalTravelTime = totalTravelTime;
		this.totalWaitTime = totalWaitTime;
		this.available = available;
		this.filtered = filtered;
		this.missedTW = missedTW;
	}

	private int additionalDistance;
	private int additionalRunTime;
	private int additionalStopCost;
	private int capacity;
	private int costPerMile;
	private int fixedRouteSetupCost;
	private int maxRunTime;
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
	private boolean available;
	private boolean filtered;
	private boolean missedTW;
	private String unavailabilityReason;
	private int waveVehicles;
	private int waveVehiclesInUse;
	private Date waveStartTime;
	private int waveOrdersTaken;
	private double totalQuantities;
	private boolean newRoute;
	private double capacities;

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
	
	public double getPercentageAvailable() {
		/*System.out.println(getTotalRunTime()+"=="
				+ getTotalServiceTime()+" > "
				+ getTotalTravelTime()
				+"-DYNACOST->" +getTotalServiceTime()+getTotalTravelTime());*/
		double result = 0.0;
		if(getTotalRunTime() > 0) {
			return (getTotalServiceTime()+getTotalTravelTime())/(double)getTotalRunTime();
		}
		
		return result;
	}
	public String getUnavailabilityReason() {
		return unavailabilityReason;
	}
	public void setUnavailabilityReason(String unavailabilityReason) {
		this.unavailabilityReason = unavailabilityReason;
	}
	public int getWaveVehicles() {
		return waveVehicles;
	}
	public void setWaveVehicles(int waveVehicles) {
		this.waveVehicles = waveVehicles;
	}
	public int getWaveVehiclesInUse() {
		return waveVehiclesInUse;
	}
	public void setWaveVehiclesInUse(int waveVehiclesInUse) {
		this.waveVehiclesInUse = waveVehiclesInUse;
	}
	public Date getWaveStartTime() {
		return waveStartTime;
	}
	public void setWaveStartTime(Date waveStartTime) {
		this.waveStartTime = waveStartTime;
	}
	public int getWaveOrdersTaken() {
		return waveOrdersTaken;
	}
	public void setWaveOrdersTaken(int waveOrdersTaken) {
		this.waveOrdersTaken = waveOrdersTaken;
	}
	public double getTotalQuantities() {
		return totalQuantities;
	}
	public void setTotalQuantities(double totalQuantities) {
		this.totalQuantities = totalQuantities;
	}
	public boolean isNewRoute() {
		return newRoute;
	}
	public void setNewRoute(boolean newRoute) {
		this.newRoute = newRoute;
	}
	public double getCapacities() {
		return capacities;
	}
	public void setCapacities(double capacities) {
		this.capacities = capacities;
	}
	
	
	
}
