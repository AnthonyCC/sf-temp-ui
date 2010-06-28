package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class DlvLocation implements java.io.Serializable, TrnBaseEntityI  {
	
	private String locationId;	
	private String apartment;
		
	private DlvServiceTimeType serviceTimeType;
	
	private DlvBuilding building;
		
	private BigDecimal serviceTimeOverride;
	private BigDecimal serviceTimeAdjustable;
	private String serviceTimeOperator;
	
	public String getServiceTimeOperator() {
		return serviceTimeOperator;
	}
	public void setServiceTimeOperator(String serviceTimeOperator) {
		this.serviceTimeOperator = serviceTimeOperator;
	}
	public BigDecimal getServiceTimeOverride() {
		return serviceTimeOverride;
	}
	public void setServiceTimeOverride(BigDecimal serviceTimeOverride) {
		this.serviceTimeOverride = serviceTimeOverride;
	}
	public BigDecimal getServiceTimeAdjustable() {
		return serviceTimeAdjustable;
	}
	public void setServiceTimeAdjustable(BigDecimal serviceTimeAdjustable) {
		this.serviceTimeAdjustable = serviceTimeAdjustable;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
		
		
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((locationId == null) ? 0 : locationId.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DlvLocation other = (DlvLocation) obj;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;
		return true;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getBuildingId() {
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getBuildingId();
	}
	public void setBuildingId(String id) {
		if("null".equals(id)) {
			setServiceTimeType(null);
		} else {
			DlvBuilding trnBuildingType = new DlvBuilding();
			trnBuildingType.setBuildingId(id);
			setBuilding(trnBuildingType);
		}
	}
	
	public BigDecimal getLatitude() {
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getLatitude();
	}
	
	public void setLatitude(BigDecimal latitude) {
		
	}
	public BigDecimal getLongitude() {
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getLongitude();
	}
	public void setLongitude(BigDecimal longitude) {
		
	}
	
	public DlvServiceTimeType getServiceTimeType() {
		return serviceTimeType;
	}
	
	public void setServiceTimeType(DlvServiceTimeType serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
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
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getGeocodeConfidence();
	}
	public void setGeocodeConfidence(String geocodeConfidence) {
		
	}
	public String getGeocodeQuality() {
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getGeocodeQuality();
	}
	public void setGeocodeQuality(String geocodeQuality) {
		
	}
	public DlvBuilding getBuilding() {
		return building;
	}
	public void setBuilding(DlvBuilding building) {
		this.building = building;
	}

}
