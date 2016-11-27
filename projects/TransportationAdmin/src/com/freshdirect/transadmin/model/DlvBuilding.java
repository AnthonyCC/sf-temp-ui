package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public class DlvBuilding implements java.io.Serializable, TrnBaseEntityI  {
	
	private String buildingId;
	private String srubbedStreet;
	private String zip;
	private String country;
	private String city;
	private String state;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private String geocodeConfidence;
	private String geocodeQuality;
	
	private DlvServiceTimeType serviceTimeType;
	private BigDecimal serviceTimeOverride;
	private BigDecimal serviceTimeAdjustable;
	private String serviceTimeOperator;
	private String zoneCode;
	private String forceBulk;
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	Set<DeliveryGroup> buildingGroups = new HashSet<DeliveryGroup>(0);
	
	public Set<DeliveryGroup> getBuildingGroups() {		
		return buildingGroups;
	}
	
	public void setBuildingGroups(Set<DeliveryGroup> buildingGroups) {
		this.buildingGroups = buildingGroups;
	}
	
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
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((buildingId == null) ? 0 : buildingId.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DlvBuilding other = (DlvBuilding) obj;
		if (buildingId == null) {
			if (other.buildingId != null)
				return false;
		} else if (!buildingId.equals(other.buildingId))
			return false;
		return true;
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
	
	public DlvServiceTimeType getServiceTimeType() {
		return serviceTimeType;
	}

	public void setServiceTimeType(DlvServiceTimeType serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}

	public BigDecimal getServiceTimeAdjustable() {
		return serviceTimeAdjustable;
	}
	
	public BigDecimal getServiceTimeAdjustableEx() {
		if(serviceTimeAdjustable==null)
			return null;
		if(EnumArithmeticOperator.SUB.toString().equals(serviceTimeOperator))
			return serviceTimeAdjustable.negate();
		return serviceTimeAdjustable;
	}

	public void setServiceTimeAdjustable(BigDecimal serviceTimeAdjustable) {
		if(serviceTimeAdjustable==null)
			this.serviceTimeOperator=null;
		this.serviceTimeAdjustable = serviceTimeAdjustable;
		
	}

	public String getServiceTimeOperator() {
		return serviceTimeOperator;
	}

	public void setServiceTimeOperator(String serviceTimeOperator) {
		this.serviceTimeOperator = serviceTimeOperator;
	}

	public void setServiceTimeOverride(BigDecimal serviceTimeOverride) {
		this.serviceTimeOverride = serviceTimeOverride;
	}
	
	public BigDecimal getServiceTimeOverride() {
		return serviceTimeOverride;
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

	public String getForceBulk() {
		return forceBulk;
	}

	public void setForceBulk(String forceBulk) {
		this.forceBulk = forceBulk;
	}

}
