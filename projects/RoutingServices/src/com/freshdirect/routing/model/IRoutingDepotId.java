package com.freshdirect.routing.model;

public interface IRoutingDepotId {
	
	String getLocationId();
	String getLocationType();
	
	String getRegionID();
	void setLocationId(String locationId);
	void setLocationType(String locationType);
	
	void setRegionID(String regionID);
}
