package com.freshdirect.transadmin.model;

public class TrnZoneType implements java.io.Serializable, TrnBaseEntityI {
	
	private String zoneTypeId;
	private String name;
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZoneTypeId() {
		return zoneTypeId;
	}

	public void setZoneTypeId(String zoneTypeId) {
		this.zoneTypeId = zoneTypeId;
	}

	public boolean isObsoleteEntity() {
		return false;
	}
	
}
