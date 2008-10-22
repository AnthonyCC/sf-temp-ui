package com.freshdirect.routing.model;

public interface IGeocodeResult {
	
	
	String getAlternateZipcode();
	void setAlternateZipcode(String alternateZipcode);
	IGeographicLocation getGeographicLocation();
	void setGeographicLocation(IGeographicLocation geographicLocation);
}
