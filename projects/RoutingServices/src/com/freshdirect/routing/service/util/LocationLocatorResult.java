package com.freshdirect.routing.service.util;

import java.io.Serializable;

import com.freshdirect.routing.model.ILocationModel;

public class LocationLocatorResult implements Serializable {
	
	private ILocationModel location;
	
	private boolean isNewBuilding;
	
	private boolean isNewLocation;

	public ILocationModel getLocation() {
		return location;
	}

	public void setLocation(ILocationModel location) {
		this.location = location;
	}

	public boolean isNewBuilding() {
		return isNewBuilding;
	}

	public void setNewBuilding(boolean isNewBuilding) {
		this.isNewBuilding = isNewBuilding;
	}

	public boolean isNewLocation() {
		return isNewLocation;
	}

	public void setNewLocation(boolean isNewLocation) {
		this.isNewLocation = isNewLocation;
	}
	
	
}
