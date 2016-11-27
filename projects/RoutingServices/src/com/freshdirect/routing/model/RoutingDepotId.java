package com.freshdirect.routing.model;

public class RoutingDepotId extends BaseModel implements IRoutingDepotId {
	
	private String locationId;
	private String locationType;
	private String regionID;
	
	public String getLocationId() {
		return locationId;
	}
	public String getLocationType() {
		return locationType;
	}
	public String getRegionID() {
		return regionID;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}
	
	
}
