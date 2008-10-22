package com.freshdirect.routing.model;

public class PackagingModel extends BaseModel implements IPackagingModel {
	
	private long noOfCartons;
	
	private long noOfFreezers;
	
	private long noOfCases;
	
	private double totalSize1;
	
	private double totalSize2;
	
	private boolean isDefault; 

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public long getNoOfCartons() {
		return noOfCartons;
	}

	public void setNoOfCartons(long noOfCartons) {
		this.noOfCartons = noOfCartons;
	}

	public long getNoOfCases() {
		return noOfCases;
	}

	public void setNoOfCases(long noOfCases) {
		this.noOfCases = noOfCases;
	}

	public long getNoOfFreezers() {
		return noOfFreezers;
	}

	public void setNoOfFreezers(long noOfFreezers) {
		this.noOfFreezers = noOfFreezers;
	}
	
	public String toString() {
		return noOfCartons+"-"+noOfFreezers+"-"+noOfCases+"\n";
	}

	public double getTotalSize1() {
		return totalSize1;
	}

	public void setTotalSize1(double totalSize1) {
		this.totalSize1 = totalSize1;
	}

	public double getTotalSize2() {
		return totalSize2;
	}

	public void setTotalSize2(double totalSize2) {
		this.totalSize2 = totalSize2;
	}

	
}
