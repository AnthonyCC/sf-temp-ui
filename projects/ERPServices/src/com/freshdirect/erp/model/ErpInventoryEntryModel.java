/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.common.date.SimpleDateDeserializer;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.sap.SapProperties;

/**
 * ErpUnavailabilityEntry model class. 
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpInventoryEntryModel extends ModelSupport implements Comparable<Object> {
	private static final long serialVersionUID = 3557841522645345674L;

	/** Inventory entry start date */
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date startDate;

	/** Inventory level quantity in base UOM */
	private double quantity;
	
	private String plantId;

	/**
	 * Default constructor.
	 */
	public ErpInventoryEntryModel() {
		super();
	}
	
	/**
	 * Constructor with all properties.
	 *
	 * @param startDate start date of inventory entry
	 * @param quantity Inventory level quantity in base UOM
	 */
	public ErpInventoryEntryModel(Date startDate, double quantity) {
		this.plantId=SapProperties.getPlant();
		this.startDate = startDate;
		this.quantity = quantity;
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param startDate start date of inventory entry
	 * @param quantity Inventory level quantity in base UOM
	 */
	public ErpInventoryEntryModel(Date startDate, double quantity, String plantId) {
		super();
		this.plantId = plantId;
		this.startDate = startDate;
		this.quantity = quantity;
	}
	
	/**
	 * Get inventory entry start date.
	 *
	 * @return date
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Get inventory level quantity.
	 *
	 * @return quantity in base unit of measure
	 */
	public double getQuantity(){
		return this.quantity;
	}
	
	/**
	 * @return the plantId
	 */
	public String getPlantId()
	{
		return plantId;
	}
	
	public String toString() {
		return "ErpInventoryEntryModel[" + this.plantId + " " + this.startDate + " " + this.quantity + "]";
	}
	
	@Override
	public void setId( String id ) {
		if (id != null)
			super.setId(id);
	}
	
	@Override
	public int compareTo(Object o)
	{
		ErpInventoryEntryModel other = (ErpInventoryEntryModel) o;
		int i = this.plantId.compareTo(other.plantId);
	   if (i != 0)
	   {
	   	return i;
	   }

	   return  this.startDate.compareTo(other.startDate);
	}
}
