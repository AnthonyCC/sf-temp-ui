/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdlogistics.model;

import java.util.Date;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.framework.core.*;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDDeliveryDepotLocationModel extends ModelSupport {

	private Date startDate;
	private Date endDate;
	
	private String facility;
	private AddressModel address;
	private String instructions;
	private String zoneCode;
    private boolean deliveryChargeWaived;


	public FDDeliveryDepotLocationModel() {
		super();
	}
	
	public FDDeliveryDepotLocationModel(PrimaryKey pk, Date startDate, Date endDate, String facility, AddressModel address, String instructions, String zoneCode, boolean deliveryChargeWaived) {
		this();
		this.setPK(pk);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setFacility(facility);
		this.setAddress(address);
		this.setInstructions(instructions);
		this.setZoneCode(zoneCode);	
        this.setDeliveryChargeWaived(deliveryChargeWaived);
	}

	public Date getStartDate(){
		return this.startDate;
	}
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}
	
	public String getFacility(){
		return this.facility;
	}
	public void setFacility(String facility){
		this.facility = facility;	
	}
	
	public String getInstructions(){
		return this.instructions;
	}
	public void setInstructions(String instructions){
		this.instructions = instructions;
	}
	
	public String getZoneCode(){
		return this.zoneCode;
	}
	public void setZoneCode(String zoneName){
		this.zoneCode = zoneName;
	}
	
	public AddressModel getAddress(){
		return this.address;
	}
	public void setAddress(AddressModel address){
		this.address = address;
	}
	
    public boolean getDeliveryChargeWaived() {
        return this.deliveryChargeWaived;
    }
    
    public void setDeliveryChargeWaived(boolean deliveryChargeWaived) {
        this.deliveryChargeWaived = deliveryChargeWaived;
    }

}
