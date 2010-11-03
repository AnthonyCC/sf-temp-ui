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

import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpUnavailabilityEntry model class. 
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpInventoryEntryModel extends ModelSupport implements Comparable {
	private static final long serialVersionUID = 3557841522645345674L;

	/** Inventory entry start date */
	private Date startDate;

	/** Inventory level quantity in base UOM */
	private double quantity;

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
		super();
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

	public int compareTo(Object o) {
		ErpInventoryEntryModel other = (ErpInventoryEntryModel)o;
		return this.startDate.compareTo( other.startDate );
	}
	
	public String toString() {
		return "ErpInventoryEntryModel[" + this.startDate + " " + this.quantity + "]";
	}

}
