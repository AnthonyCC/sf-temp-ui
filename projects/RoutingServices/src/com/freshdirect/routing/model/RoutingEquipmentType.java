package com.freshdirect.routing.model;

public class RoutingEquipmentType extends BaseModel implements IRoutingEquipmentType {
	
	private String equipmentTypeID;
	private String regionID;
	
	public String getEquipmentTypeID() {
		return equipmentTypeID;
	}
	public String getRegionID() {
		return regionID;
	}
	public void setEquipmentTypeID(String equipmentTypeID) {
		this.equipmentTypeID = equipmentTypeID;
	}
	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}
	
}
