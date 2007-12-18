/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;

/**
 * ErpCharacteristicValue model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCharacteristicValueModel extends ErpModelSupport implements DurableModelI {

	/** Characteristic value name */
	private String name;

    /** Characteristic value description. */
    private String description;
    
	/**
	 * Default constructor.
	 */
	public ErpCharacteristicValueModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param name characteristic value name
	 * @param description characteristic value description
	 */
	public ErpCharacteristicValueModel(String name, String description) {
		super();
		this.setName(name);
		this.setDescription(description);
	}


	/**
	 * Get characteristic value name.
	 *
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set characteristic value name.
	 *
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

    /** 
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /** 
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

	/**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the characteristic value's name.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getName();
	}

	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
		// no children
	}
    
}
