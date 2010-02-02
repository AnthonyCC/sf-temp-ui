package com.freshdirect.routing.model;

public interface IDeliverySlotCost {
	
	int getAdditionalDistance();
	void setAdditionalDistance(int additionalDistance);
	int getAdditionalRunTime();
	void setAdditionalRunTime(int additionalRunTime);
	int getAdditionalStopCost();
	void setAdditionalStopCost(int additionalStopCost);
	boolean isAvailable();
	void setAvailable(boolean available);
	int getCapacity();
	void setCapacity(int capacity);
	int getCostPerMile();
	void setCostPerMile(int costPerMile);
	boolean isFiltered();
	void setFiltered(boolean filtered);
	int getFixedRouteSetupCost();
	void setFixedRouteSetupCost(int cixedRouteSetupCost);
	int getMaxRunTime();
	void setMaxRunTime(int maxRunTime);
	boolean isMissedTW();
	void setMissedTW(boolean missedTW);
	int getOvertimeHourlyWage();
	void setOvertimeHourlyWage(int overtimeHourlyWage);
	int getPrefRunTime();
	void setPrefRunTime(int prefRunTime);
	int getRegularHourlyWage();
	void setRegularHourlyWage(int regularHourlyWage);
	int getRegularWageDurationSeconds();
	void setRegularWageDurationSeconds(int regularWageDurationSeconds);
	int getRouteId();
	void setRouteId(int routeId);
	int getStopSequence();
	void setStopSequence(int stopSequence);
	int getTotalDistance();
	void setTotalDistance(int totalDistance);
	int getTotalPUQuantity();
	void setTotalPUQuantity(int totalPUQuantity);
	int getTotalQuantity();
	void setTotalQuantity(int totalQuantity);
	int getTotalRouteCost();
	void setTotalRouteCost(int totalRouteCost);
	int getTotalRunTime();
	void setTotalRunTime(int totalRunTime);
	int getTotalServiceTime();
	void setTotalServiceTime(int totalServiceTime);
	int getTotalTravelTime();
	void setTotalTravelTime(int totalTravelTime);
	int getTotalWaitTime();
	void setTotalWaitTime(int totalWaitTime);
	
	double getPercentageAvailable();
}
