package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class DlvBuilding implements java.io.Serializable, TrnBaseEntityI  {
	
	private String buildingId;
	private String srubbedStreet;
	private String zip;
	private String country;
	private String city;
	private String state;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private DlvServiceTimeType serviceTimeType;
	
	private String geocodeConfidence;
	private String geocodeQuality;
		
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
		
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public DlvServiceTimeType getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(DlvServiceTimeType serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public String getSrubbedStreet() {
		return srubbedStreet;
	}
	public void setSrubbedStreet(String srubbedStreet) {
		this.srubbedStreet = srubbedStreet;
	}
	
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getDlvServiceTimeType() {
		if(getServiceTimeType() == null) {
			return null;
		}
		return getServiceTimeType().getCode();
	}

	public void setDlvServiceTimeType(String serviceTimeType) {
		if("null".equals(serviceTimeType)) {
			setServiceTimeType(null);
		} else {
			DlvServiceTimeType trnServiceTimeType = new DlvServiceTimeType();
			trnServiceTimeType.setCode(serviceTimeType);
			setServiceTimeType(trnServiceTimeType);
		}
	}
	
		
	public boolean isObsoleteEntity() {
		return false;
	}
	public String getGeocodeConfidence() {
		return geocodeConfidence;
	}
	public void setGeocodeConfidence(String geocodeConfidence) {
		this.geocodeConfidence = geocodeConfidence;
	}
	public String getGeocodeQuality() {
		return geocodeQuality;
	}
	public void setGeocodeQuality(String geocodeQuality) {
		this.geocodeQuality = geocodeQuality;
	}
	
	public String toString() {
		return buildingId+","+srubbedStreet+","+zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
