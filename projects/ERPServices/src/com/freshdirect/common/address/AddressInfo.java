package com.freshdirect.common.address;

import java.io.Serializable;

public class AddressInfo implements Serializable {

    private static final long serialVersionUID = -2142802941454999638L;

    public AddressInfo() {
		super();
	}

	public AddressInfo(String zoneCode, double longitude,
			double latitude, String scrubbedStreet,
			EnumAddressType addressType, String county, String buildingId,
			String locationId) {
		super();
		this.zoneCode = zoneCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.scrubbedStreet = scrubbedStreet;
		this.addressType = addressType;
		this.county = county;
		this.buildingId = buildingId;
		this.locationId = locationId;
	}

	private String zoneId;
	private String zoneCode;
	private double longitude;
	private double latitude;
	private String scrubbedStreet;
	private EnumAddressType addressType;
	private String county;
	private boolean isGeocodeException;
	private String buildingId;
	private String locationId;
	private String ssScrubbedAddress;
	
	public boolean isGeocodeException() {
		return isGeocodeException;
	}

	public void setGeocodeException(boolean isGeocodeException) {
		this.isGeocodeException = isGeocodeException;
	}

	public String getZoneId(){
		return this.zoneId;
	}
	
	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}
	
	public String getZoneCode(){
		return this.zoneCode;
	}
	
	public void setZoneCode(String zoneCode){
		this.zoneCode = zoneCode;
	}
	public double getLongitude(){
		return this.longitude; 
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public String getScrubbedStreet(){
		return this.scrubbedStreet;
	}
	
	public void setScrubbedStreet(String scrubbedStreet){
		this.scrubbedStreet = scrubbedStreet;
	}

	public EnumAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(EnumAddressType addressType) {
		this.addressType = addressType;
	}
	
	public String getCounty() {
		return county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	public String toString() {
		return "AddressInfo[zoneId " + zoneId
			+ ", scrubbedStreet "
			+ scrubbedStreet
			+ ", longitude/latitude "
			+ longitude
			+ "/"
			+ latitude
			+ ", addressType "
			+ addressType
			+ ", county "
			+ county
			+ "]";
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the ssScrubbedAddress
	 */
	public String getSsScrubbedAddress() {
		return ssScrubbedAddress;
	}

	/**
	 * @param ssScrubbedAddress the ssScrubbedAddress to set
	 */
	public void setSsScrubbedAddress(String ssScrubbedAddress) {
		this.ssScrubbedAddress = ssScrubbedAddress;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((addressType == null) ? 0 : addressType.hashCode());
        result = prime * result + ((buildingId == null) ? 0 : buildingId.hashCode());
        result = prime * result + ((county == null) ? 0 : county.hashCode());
        result = prime * result + ((locationId == null) ? 0 : locationId.hashCode());
        result = prime * result + ((scrubbedStreet == null) ? 0 : scrubbedStreet.hashCode());
        result = prime * result + ((zoneCode == null) ? 0 : zoneCode.hashCode());
        result = prime * result + ((zoneId == null) ? 0 : zoneId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AddressInfo other = (AddressInfo) obj;
        if (addressType == null) {
            if (other.addressType != null)
                return false;
        } else if (!addressType.equals(other.addressType))
            return false;
        if (buildingId == null) {
            if (other.buildingId != null)
                return false;
        } else if (!buildingId.equals(other.buildingId))
            return false;
        if (county == null) {
            if (other.county != null)
                return false;
        } else if (!county.equals(other.county))
            return false;
        if (isGeocodeException != other.isGeocodeException)
            return false;
        if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
            return false;
        if (locationId == null) {
            if (other.locationId != null)
                return false;
        } else if (!locationId.equals(other.locationId))
            return false;
        if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
            return false;
        if (scrubbedStreet == null) {
            if (other.scrubbedStreet != null)
                return false;
        } else if (!scrubbedStreet.equals(other.scrubbedStreet))
            return false;
        if (ssScrubbedAddress == null) {
            if (other.ssScrubbedAddress != null)
                return false;
        } else if (!ssScrubbedAddress.equals(other.ssScrubbedAddress))
            return false;
        if (zoneCode == null) {
            if (other.zoneCode != null)
                return false;
        } else if (!zoneCode.equals(other.zoneCode))
            return false;
        if (zoneId == null) {
            if (other.zoneId != null)
                return false;
        } else if (!zoneId.equals(other.zoneId))
            return false;
        return true;
    }

	
}
