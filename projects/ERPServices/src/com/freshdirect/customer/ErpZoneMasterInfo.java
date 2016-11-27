package com.freshdirect.customer;

import java.io.Serializable;
import java.util.*;

import com.freshdirect.framework.core.ModelSupport;

public class ErpZoneMasterInfo extends ModelSupport implements Serializable {

	
	private ErpZoneRegionInfo region;
	
	private String sapId;
	private EnumZoneServiceType serviceType;
	private ErpZoneMasterInfo parentZone;
	private boolean isDefault;
	private int version;
	private String description;
	private String webDescription;
	
	public ErpZoneMasterInfo(
		String sapId,
		ErpZoneRegionInfo region,
		EnumZoneServiceType serviceType,	
		String description		
		) {
				
		this.sapId=sapId;
		this.region = region;
		this.serviceType = serviceType;
		this.description=description;		
	}
	
	
	

	public void setDetails(List list) {
		if(list == null)
			list = new ArrayList();
		else
			details = list;
	}
	
	public List getDetails() {
		return details;
	}
	
	public String toString() {
		return "ErpZoneInfo[routeId: "
			+ this.getId()
			+ " regionId: "
			+ this.region
			+ " serviceType: "
			+ this.description
			+ " desc: "
			+ details.toString();
	}

	
	public ErpZoneRegionInfo getRegion() {
		return region;
	}

	public void setRegion(ErpZoneRegionInfo region) {
		this.region = region;
	}

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public EnumZoneServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(EnumZoneServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public ErpZoneMasterInfo getParentZone() {
		return parentZone;
	}

	public void setParentZone(ErpZoneMasterInfo parentZone) {
		this.parentZone = parentZone;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebDescription() {
		return webDescription;
	}

	public void setWebDescription(String webDescription) {
		this.webDescription = webDescription;
	}
		
	private List details = new ArrayList();

//	public boolean equals(Object o){
//		if(o instanceof ErpZoneMasterInfo){
//			ErpZoneMasterInfo masterInfo=(ErpZoneMasterInfo)o;
//			if(masterInfo.get)
//		}
//		return false;
//	}
	

}
