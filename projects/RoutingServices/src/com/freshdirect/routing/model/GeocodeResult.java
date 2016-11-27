package com.freshdirect.routing.model;

public class GeocodeResult extends BaseModel implements IGeocodeResult {
	
	private String alternateZipcode;
	private IGeographicLocation geographicLocation;
	
	public String getAlternateZipcode() {
		return alternateZipcode;
	}
	public void setAlternateZipcode(String alternateZipcode) {
		this.alternateZipcode = alternateZipcode;
	}
	public IGeographicLocation getGeographicLocation() {
		return geographicLocation;
	}
	public void setGeographicLocation(IGeographicLocation geographicLocation) {
		this.geographicLocation = geographicLocation;
	}
}
