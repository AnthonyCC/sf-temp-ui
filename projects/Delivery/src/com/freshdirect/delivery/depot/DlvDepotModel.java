/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.delivery.depot;

import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.collection.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class DlvDepotModel extends ModelSupport {
	
	private String name;
	private String registrationCode;
	private String regionId;
	private String depotCode;
    private String custServiceEmail;
    private boolean requireEmployeeId;
    private boolean pickup;
    private boolean corporateDepot;
    private boolean deactivated;
	
	/** List of DlvLocationModel objects */
	private LocalObjectList locations = new LocalObjectList();
	
	/** 
	 * Creates new DlvDepotModel 
	 */
	public DlvDepotModel() {
		super();
	}
	
	/**
	 * Constructor with all properties
	 */
	public DlvDepotModel(String name, String registrationCode, String regionId, List locations, String depotCode, String custServEmail, boolean requireEmployeeId, boolean pickup, boolean corporateDepot, boolean deactivated){
		this.name = name;
		this.registrationCode = registrationCode;
		this.regionId = regionId;
		this.setLocations(locations);
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

	public List getLocations() {
		return (List)this.locations.clone();
	}
	
	public DlvLocationModel getLocation(String locationId){
		return (DlvLocationModel) this.locations.get( new PrimaryKey(locationId) );
	}
	
	public void setLocations(List locations) {
		this.locations = new LocalObjectList(locations);
	}
		
}
