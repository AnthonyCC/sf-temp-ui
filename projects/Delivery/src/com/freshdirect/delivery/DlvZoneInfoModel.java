/*
 * DlvZipCheckResponseModel.java
 *
 * Created on January 22, 2002, 6:46 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.core.ModelSupport;

public class DlvZoneInfoModel extends ModelSupport{
	

	private static final long serialVersionUID = -754268415782542354L;
	
	private final String zoneCode;
	private final String zoneId;
	private final String regionId;
	private final EnumZipCheckResponses response;
	private final boolean unattended;
	private final boolean cosEnabled;
	
	/** Creates new DlvZipCheckResponseModel */
    public DlvZoneInfoModel(String zoneCode, String zoneId, String regionId, EnumZipCheckResponses response, boolean unattended, boolean cosEnabled) {
    	this.zoneCode = zoneCode;
    	this.zoneId = zoneId;
    	this.regionId = regionId;
    	this.response = response;
    	this.unattended = unattended;
    	this.cosEnabled=cosEnabled;
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
	
	public boolean isUnattended() {
		return this.unattended;
	}
	
	public EnumZipCheckResponses getResponse(){
		return this.response;
	}

	public String toString() {
		return "DlvZoneInfoModel["
			+ this.response
			+ ", "
			+this.zoneId
			+", "
			+this.zoneCode
			+", "
			+this.regionId
			+",unattended="
			+this.unattended
			+",cosEnabled="
			+this.cosEnabled
			+ "]";
	}

	public boolean isCosEnabled() {
		return this.cosEnabled;
	}	
}