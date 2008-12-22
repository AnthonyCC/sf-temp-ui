package com.freshdirect.transadmin.model;

import java.util.HashSet;
import java.util.Set;

public class TrnZoneType implements java.io.Serializable, TrnBaseEntityI {
	
	private String zoneTypeId;
	private String name;
	private String description;
	private Set zonetypeResources = new HashSet(0);
	

	public TrnZoneType() {
	}

	public TrnZoneType(String zoneTypeId, String name) {
		this.zoneTypeId = zoneTypeId;
		this.name = name;
	}

	public TrnZoneType(String zoneTypeId, String name, String description,
			Set zonetypeResources) {
		this.zoneTypeId = zoneTypeId;
		this.name = name;
		this.description = description;
		this.zonetypeResources = zonetypeResources;
		
	}

	public String getZoneTypeId() {
		return this.zoneTypeId;
	}

	public void setZoneTypeId(String zoneTypeId) {
		this.zoneTypeId = zoneTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getZonetypeResources() {
		return this.zonetypeResources;
	}

	public void setZonetypeResources(Set zonetypeResources) {
		this.zonetypeResources = zonetypeResources;
	}

	

	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

}
