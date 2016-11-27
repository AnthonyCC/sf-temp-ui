package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.util.TimeOfDay;

public class CapacityAnalyzerCommand implements Serializable, Comparable<CapacityAnalyzerCommand>{
	
	private String buildingId;
	private String zoneCode;
	private String address;	
	private String description;
	private int soldOutWindow;
	private Map<TimeRange, String> timeslots;

	private boolean hasCapacity;
	
	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	public Map<TimeRange, String> getTimeslots() {
		return timeslots;
	}

	public void setTimeslots(Map<TimeRange, String> timeslots) {
		this.timeslots = timeslots;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public boolean hasCapacity() {
		return hasCapacity;
	}

	public void setHasCapacity(boolean hasCapacity) {
		this.hasCapacity = hasCapacity;
	}



	public int getSoldOutWindow() {
		return soldOutWindow;
	}
	
	public void setSoldOutWindow(int soldOutWindow) {
		this.soldOutWindow = soldOutWindow;
	}
	
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	@Override
	public int compareTo(CapacityAnalyzerCommand o) {
		// TODO Auto-generated method stub
		return this.toString().compareTo(o.toString());
	}

}
