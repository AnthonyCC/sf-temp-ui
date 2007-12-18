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
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpMaterialInfo model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpMaterialInfoModel extends ErpModelSupport implements DurableModelI, EntityModelI {
    
    /** SAP unique material number */
	private String sapId;

	/** Material description */
	private String description;
    
    
    /**
	 * Default constructor.
	 */
	public ErpMaterialInfoModel() {
		super();
	}

	/**
	 * Constructor with all properties for an identified object.
	 */
	public ErpMaterialInfoModel(PrimaryKey pk, String sapId,  String description) {
        this.setPK(pk);
		this.setSapId(sapId);
		this.setDescription(description);
	}
    
    /**
	 * Constructor with all properties for an anonymous object.
	 */
	public ErpMaterialInfoModel(String sapId,  String description) {
		this.setSapId(sapId);
		this.setDescription(description);
	}
    
	/**
	 * Get Material SAP unique ID.
	 *
	 * @return SAP identifier
	 */
	public String getSapId() {
		return this.sapId;
	}

	/**
	 * Set Material SAP unique ID.
	 *
	 * @param sapId SAP identifier
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	/**
	 * Get Material Description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get Material Description.
	 *
	 * @return the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
    
    /**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the material's SAP ID.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getSapId();
	}
    
    /**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
	}
    
}
