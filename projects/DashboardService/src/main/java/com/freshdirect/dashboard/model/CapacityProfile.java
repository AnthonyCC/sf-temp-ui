package com.freshdirect.dashboard.model;

public class CapacityProfile {
	
	private double cutoffHourStart;
	
	private double cutoffHourEnd;
	
	private double currentUtilization;
	
	private double projectedUtilization;
	
	private int soldOutCnt;
	
	private int projectedOPTCnt;
	
	private int customerSOWCnt;
	
	private int customerVisitCnt;
	
	private int realTimeBoune;
	
	private String suggestedAction;

	public double getCutoffHourStart() {
		return cutoffHourStart;
	}

	public void setCutoffHourStart(double cutoffHourStart) {
		this.cutoffHourStart = cutoffHourStart;
	}

	public double getCutoffHourEnd() {
		return cutoffHourEnd;
	}

	public void setCutoffHourEnd(double cutoffHourEnd) {
		this.cutoffHourEnd = cutoffHourEnd;
	}

	public double getCurrentUtilization() {
		return currentUtilization;
	}

	public void setCurrentUtilization(double currentUtilization) {
		this.currentUtilization = currentUtilization;
	}

	public double getProjectedUtilization() {
		return projectedUtilization;
	}

	public void setProjectedUtilization(double projectedUtilization) {
		this.projectedUtilization = projectedUtilization;
	}

	public int getSoldOutCnt() {
		return soldOutCnt;
	}

	public void setSoldOutCnt(int soldOutCnt) {
		this.soldOutCnt = soldOutCnt;
	}

	public int getProjectedOPTCnt() {
		return projectedOPTCnt;
	}

	public void setProjectedOPTCnt(int projectedOPTCnt) {
		this.projectedOPTCnt = projectedOPTCnt;
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

	public int getRealTimeBoune() {
		return realTimeBoune;
	}

	public void setRealTimeBoune(int realTimeBoune) {
		this.realTimeBoune = realTimeBoune;
	}

	public String getSuggestedAction() {
		return suggestedAction;
	}

	public void setSuggestedAction(String suggestedAction) {
		this.suggestedAction = suggestedAction;
	}
}
