/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdlogistics.model;

import java.io.Serializable;
import java.util.*;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.framework.core.*;
import com.freshdirect.framework.collection.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDDeliveryDepotModel implements Serializable {
	
	private String name;
	private String registrationCode;
	private String regionId;
	private String depotCode;
    private String custServiceEmail;
    private boolean requireEmployeeId;
    private boolean pickup;
    private boolean corporateDepot;
    private boolean deactivated;
    private List<FDDeliveryDepotLocationModel> locations;
	
	/** 
	 * Creates new DlvDepotModel 
	 */
	public FDDeliveryDepotModel() {
		super();
	}
	
	/**
	 * Constructor with all properties
	 */
	public FDDeliveryDepotModel(String name, String registrationCode, String regionId, List<FDDeliveryDepotLocationModel> locations, String depotCode, String custServEmail, boolean requireEmployeeId, boolean pickup, boolean corporateDepot, boolean deactivated){
		this.name = name;
		this.registrationCode = registrationCode;
		this.regionId = regionId;
		this.locations = locations;
		this.depotCode = depotCode;
		this.custServiceEmail = custServEmail;
        this.requireEmployeeId = requireEmployeeId;
        this.pickup = pickup;
        this.corporateDepot = corporateDepot;
        this.deactivated = deactivated;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getRegistrationCode(){
		return this.registrationCode;
	}
	public void setRegistrationCode(String registrationCode){
		this.registrationCode = registrationCode;
	}
	
	public String getRegionId(){
		return this.regionId;
	}
	public void setRegionId(String regionId){
		this.regionId = regionId;
	}
	
	public String getDepotCode(){
		return this.depotCode;
	}
	public void setDepotCode(String depotCode){
		this.depotCode = depotCode;
	}
    
    public String getCustomerServiceEmail() {
        return this.custServiceEmail;
    }
    
    public void setCustomerServiceEmail(String custServiceEmail) {
        this.custServiceEmail = custServiceEmail;
    }

    public boolean getRequireEmployeeId() {
        return this.requireEmployeeId;
    }
    
    public void setRequireEmployeeId(boolean requireEmployeeId) {
        this.requireEmployeeId = requireEmployeeId;
    }
    
    public boolean isPickup(){
    	return this.pickup;
    }
    
    public void setPickup(boolean pickup){
    	this.pickup = pickup;
    }
    
    public boolean isCorporateDepot() {
    	return this.corporateDepot;
    }
    
    public void setCorporateDepot(boolean corporateDepot) {
    	this.corporateDepot = corporateDepot;
    }
    
    public boolean isDeactivated(){
    	return this.deactivated;
    }
    
    public void setDeactivated(boolean deactivated) {
    	this.deactivated = deactivated;
    }

	
	
	public FDDeliveryDepotLocationModel getLocation(String locationId){
		for(FDDeliveryDepotLocationModel location : this.getLocations()){
			if(location.getPK()!=null && 
					location.getPK().getId().equals(locationId)){
				return location;
			}
		}
		return null;
	}

	public List<FDDeliveryDepotLocationModel> getLocations() {
		return locations;
	}

	public void setLocations(List<FDDeliveryDepotLocationModel> locations) {
		this.locations = locations;
	}
	

		
}
