package com.freshdirect.transadmin.model;


public class CapacitySnapshot implements java.io.Serializable, TrnBaseEntityI{
	
	
	private String buildingId;
	private String servicetype;

	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	@Override
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}
}
