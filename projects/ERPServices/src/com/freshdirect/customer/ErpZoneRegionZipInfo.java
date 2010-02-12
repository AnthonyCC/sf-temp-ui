package com.freshdirect.customer;

import java.io.Serializable;

import com.freshdirect.framework.core.ModelSupport;

public class ErpZoneRegionZipInfo extends ModelSupport implements Serializable {

	public ErpZoneRegionZipInfo(ErpZoneRegionInfo regionInfo,String zipCode) 
	{
	   this.zipCode=zipCode;
	   this.region=regionInfo;
	}
	
	public ErpZoneRegionInfo getRegion() {
		return region;
	}

	public void setRegion(ErpZoneRegionInfo region) {
		this.region = region;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int number) {
		this.version = number;
	}

	private ErpZoneRegionInfo region;	
	private String zipCode;	
	private int version;
	
	
	
}
