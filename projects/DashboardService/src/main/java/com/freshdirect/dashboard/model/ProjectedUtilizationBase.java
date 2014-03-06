package com.freshdirect.dashboard.model;


public class ProjectedUtilizationBase {

	private String zone;
	
	private String shift;
	
	private double hoursToCutoff;
	
	private int plannedCapacity;
	
	private int confirmedOrderCnt;
	
	private int confirmedUtilization;
	
	private int allocatedOrderCnt;
	
	private int allocatedUtilization;
	
	private int projectedOrderCnt;
	
	private int projectedUtilization;
	
	private int resourceCnt;
	
	private int projectedOPT;
	
	private int soldOutWindowCnt;
	
	private int customerSOWCnt;
	
	private int customerVisitCnt;
	
	private int realTimeBounce;
	
	private String suggestedAction;
			
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}	
	public int getPlannedCapacity() {
		return plannedCapacity;
	}
	public void setPlannedCapacity(int plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}
	public int getConfirmedOrderCnt() {
		return confirmedOrderCnt;
	}
	public void setConfirmedOrderCnt(int confirmedOrderCnt) {
		this.confirmedOrderCnt = confirmedOrderCnt;
	}
	public int getAllocatedOrderCnt() {
		return allocatedOrderCnt;
	}
	public void setAllocatedOrderCnt(int allocatedOrderCnt) {
		this.allocatedOrderCnt = allocatedOrderCnt;
	}
	public int getResourceCnt() {
		return resourceCnt;
	}
	public void setResourceCnt(int resourceCnt) {
		this.resourceCnt = resourceCnt;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public double getHoursToCutoff() {
		return hoursToCutoff;
	}
	public void setHoursToCutoff(double hoursToCutoff) {
		this.hoursToCutoff = hoursToCutoff;
	}
	public int getConfirmedUtilization() {
		return confirmedUtilization;
	}
	public void setConfirmedUtilization(int confirmedUtilization) {
		this.confirmedUtilization = confirmedUtilization;
	}
	public int getAllocatedUtilization() {
		return allocatedUtilization;
	}
	public void setAllocatedUtilization(int allocatedUtilization) {
		this.allocatedUtilization = allocatedUtilization;
	}
	public int getProjectedOrderCnt() {
		return projectedOrderCnt;
	}
	public void setProjectedOrderCnt(int projectedOrderCnt) {
		this.projectedOrderCnt = projectedOrderCnt;
	}
	public int getProjectedUtilization() {
		return projectedUtilization;
	}
	public void setProjectedUtilization(int projectedUtilization) {
		this.projectedUtilization = projectedUtilization;
	}
	public int getProjectedOPT() {
		return projectedOPT;
	}
	public void setProjectedOPT(int projectedOPT) {
		this.projectedOPT = projectedOPT;
	}
	public int getSoldOutWindowCnt() {
		return soldOutWindowCnt;
	}
	public void setSoldOutWindowCnt(int soldOutWindowCnt) {
		this.soldOutWindowCnt = soldOutWindowCnt;
	}
	public int getCustomerSOWCnt() {
		return customerSOWCnt;
	}
	public void setCustomerSOWCnt(int customerSOWCnt) {
		this.customerSOWCnt = customerSOWCnt;
	}
	public int getCustomerVisitCnt() {
		return customerVisitCnt;
	}
	public void setCustomerVisitCnt(int customerVisitCnt) {
		this.customerVisitCnt = customerVisitCnt;
	}
	public int getRealTimeBounce() {
		return realTimeBounce;
	}
	public void setRealTimeBounce(int realTimeBounce) {
		this.realTimeBounce = realTimeBounce;
	}
	public String getSuggestedAction() {
		return suggestedAction;
	}
	public void setSuggestedAction(String suggestedAction) {
		this.suggestedAction = suggestedAction;
	}
	public String getHourToCutoffEx() {
		if(this.hoursToCutoff != 0.0) {
			return Double.toString(this.hoursToCutoff).replace(".", ":");
		}
		return null;
	}
	@Override
	public String toString() {
		return "ProjectedUtilizationBase [zone=" + zone + ", hoursToCutoff="
				+ hoursToCutoff + ", confirmedUtilization="
				+ confirmedUtilization + ", projectedUtilization="
				+ projectedUtilization + ", suggestedAction=" + suggestedAction
				+ "]";
	}	
	
	
}
