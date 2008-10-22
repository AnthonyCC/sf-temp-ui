package com.freshdirect.routing.model;

public interface IGeographicLocation {
	
	String getLatitude();
	void setLatitude(String latitude);
	String getLongitude();
	void setLongitude(String longitude);
	String getConfidence();
	void setConfidence(String confidence);
	String getQuality();
	void setQuality(String quality);
}
