package com.freshdirect.fdstore.iplocator;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IpLocatorRecord implements Serializable {
	
	@JsonProperty("RecordID")
	private String recordID;
	
	@JsonProperty("IPAddress")
	private String iPAddress;
	
	@JsonProperty("Latitude")
	private String latitude;
	
	@JsonProperty("Longitude")
	private String longitude;
	
	@JsonProperty("PostalCode")
	private String postalCode;
	
	@JsonProperty("Region")
	private String region;
	
	@JsonProperty("ISPName")
	private String iSPName;
	
	@JsonProperty("DomainName")
	private String domainName;
	
	@JsonProperty("City")
	private String city;
	
	@JsonProperty("CountryName")
	private String countryName;
	
	@JsonProperty("CountryAbbreviation")
	private String countryAbbreviation;
	
	@JsonProperty("ConnectionSpeed")
	private String connectionSpeed;
	
	@JsonProperty("ConnectionType")
	private String connectionType;
	
	@JsonProperty("UTC")
	private String uTC;
	
	@JsonProperty("Continent")
	private String continent;
	
	@JsonProperty("Result")
	private String result;
	
	public String getRecordID() {
		return recordID;
	}
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
	public String getiPAddress() {
		return iPAddress;
	}
	public void setiPAddress(String iPAddress) {
		this.iPAddress = iPAddress;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getiSPName() {
		return iSPName;
	}
	public void setiSPName(String iSPName) {
		this.iSPName = iSPName;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryAbbreviation() {
		return countryAbbreviation;
	}
	public void setCountryAbbreviation(String countryAbbreviation) {
		this.countryAbbreviation = countryAbbreviation;
	}
	public String getConnectionSpeed() {
		return connectionSpeed;
	}
	public void setConnectionSpeed(String connectionSpeed) {
		this.connectionSpeed = connectionSpeed;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	public String getuTC() {
		return uTC;
	}
	public void setuTC(String uTC) {
		this.uTC = uTC;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "IpLocatorRecord [recordID=" + recordID + ", iPAddress="
				+ iPAddress + ", latitude=" + latitude + ", longitude="
				+ longitude + ", postalCode=" + postalCode + ", region="
				+ region + ", iSPName=" + iSPName + ", domainName="
				+ domainName + ", city=" + city + ", countryName="
				+ countryName + ", countryAbbreviation=" + countryAbbreviation
				+ ", connectionSpeed=" + connectionSpeed + ", connectionType="
				+ connectionType + ", uTC=" + uTC + ", continent=" + continent
				+ ", result=" + result + "]";
	}
	
	

}
