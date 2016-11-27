package com.freshdirect.routing.model;

public class FacilityModel extends BaseModel implements IFacilityModel  {

	private String facilityCode;
	private String routingCode;
	private int leadFromTime;
	private int leadToTime;
    private String prefix;    
    private String facilityTypeModel;
    
    private String latitude;
    private String longitude;
    
	public String getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}

	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}

	public int getLeadFromTime() {
		return leadFromTime;
	}

	public void setLeadFromTime(int leadFromTime) {
		this.leadFromTime = leadFromTime;
	}

	public int getLeadToTime() {
		return leadToTime;
	}

	public void setLeadToTime(int leadToTime) {
		this.leadToTime = leadToTime;
	}

	public String getFacilityTypeModel() {
		return facilityTypeModel;
	}

	public void setFacilityTypeModel(String facilityTypeModel) {
		this.facilityTypeModel = facilityTypeModel;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((facilityCode == null) ? 0 : facilityCode.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FacilityModel other = (FacilityModel) obj;
		if (facilityCode == null) {
			if (other.facilityCode != null)
				return false;
		} else if (!facilityCode.equals(other.facilityCode))
			return false;
		return true;
	}

	public String toString() {
		return facilityCode+"|"+facilityTypeModel;
	}
}
