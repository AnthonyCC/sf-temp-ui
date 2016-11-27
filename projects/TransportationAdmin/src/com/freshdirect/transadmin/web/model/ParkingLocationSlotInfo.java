package com.freshdirect.transadmin.web.model;

import java.io.Serializable;

public class ParkingLocationSlotInfo implements Serializable {
	
	private String location;
	private int usedSlots;
	private int availableSlots;
		
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getUsedSlots() {
		return usedSlots;
	}
	public void setUsedSlots(int usedSlots) {
		this.usedSlots = usedSlots;
	}
	public int getAvailableSlots() {
		return availableSlots;
	}
	public void setAvailableSlots(int availableSlots) {
		this.availableSlots = availableSlots;
	}
}
