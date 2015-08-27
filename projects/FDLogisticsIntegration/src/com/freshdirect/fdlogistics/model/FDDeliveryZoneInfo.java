package com.freshdirect.fdlogistics.model;

import java.io.Serializable;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.logistics.delivery.model.FulfillmentInfo;

public class FDDeliveryZoneInfo implements Serializable  {

	private final String zoneCode;
	private final String zoneId;
	private final EnumZipCheckResponses response;
	private final boolean unattended;
	private final boolean cosEnabled;
	private final boolean ctActive;
	private final String regionId;
	private final EnumRegionServiceType serviceType;
	private final FulfillmentInfo fulfillmentInfo;
	
    public FDDeliveryZoneInfo(String zoneCode, String zoneId, String regionId, EnumZipCheckResponses response) {
    	this(zoneCode, zoneId, regionId, response, false, false, false, EnumRegionServiceType.HOME, null);
    }
  
    public FDDeliveryZoneInfo(String zoneCode, String zoneId, String regionId, EnumZipCheckResponses response, boolean unattended, boolean cosEnabled, 
    		boolean ctActive, EnumRegionServiceType serviceType, FulfillmentInfo fulfillmentInfo) {

    	this.zoneCode = StringUtil.isEmpty(zoneCode)?"":zoneCode;
    	this.zoneId = zoneId;
    	this.regionId = regionId;
    	this.response = response;
    	this.unattended = unattended;
    	this.cosEnabled=cosEnabled;
    	this.ctActive = ctActive;
    	this.serviceType = serviceType;
    	this.fulfillmentInfo = fulfillmentInfo;
    
	}

	public FDDeliveryZoneInfo(String zoneCode, String zoneId, String regionId, EnumZipCheckResponses response, EnumRegionServiceType serviceType) {
    	this(zoneCode, zoneId, regionId, response, false, false, false, serviceType, null);
	}

	public String getZoneCode(){
		return this.zoneCode;
	}
	
	public String getZoneId(){
		return this.zoneId;
	}
	
	public String getRegionId(){
		return this.regionId;
	}
	
	public EnumRegionServiceType getRegionSvcType(){
		return this.serviceType;
	}
	
	public boolean isUnattended() {
		return this.unattended;
	}
	
	public EnumZipCheckResponses getResponse(){
		return this.response;
	}

	public boolean isCosEnabled() {
		return this.cosEnabled;
	}
	
	public boolean isCtActive() {
		return this.ctActive;
	}
	
	public boolean isBulkZone(){
		return (this.serviceType !=null && EnumRegionServiceType.isHybrid(this.serviceType));
	}

	public EnumRegionServiceType getServiceType() {
		return serviceType;
	}

	public FulfillmentInfo getFulfillmentInfo() {
		return fulfillmentInfo;
	}
	
}