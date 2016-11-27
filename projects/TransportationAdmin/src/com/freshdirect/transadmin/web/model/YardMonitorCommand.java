package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.List;

public class YardMonitorCommand implements Serializable {

	private List<ParkingLocationSlotInfo> locationSlotSummary;
	private List<ParkingLocationTruckInfo> locationTruckSummary;
	
	public List<ParkingLocationSlotInfo> getLocationSlotSummary() {
		return locationSlotSummary;
	}
	public void setLocationSlotSummary(List<ParkingLocationSlotInfo> locationSlotSummary) {
		this.locationSlotSummary = locationSlotSummary;
	}
	public List<ParkingLocationTruckInfo> getLocationTruckSummary() {
		return locationTruckSummary;
	}
	public void setLocationTruckSummary(List<ParkingLocationTruckInfo> locationTruckSummary) {
		this.locationTruckSummary = locationTruckSummary;
	}
}
