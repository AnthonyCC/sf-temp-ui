/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpInventory model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpInventoryModel extends ModelSupport {
	private static final long serialVersionUID = -2646485627750337690L;

	/** SAP unique material number */
	private String sapId;

	/** Date of last inventory update */
	private Date lastUpdated;
	
	/**
	 * Inventory entries
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpInventoryEntryModel}>
	 */
	private List<ErpInventoryEntryModel> entries;

	/**
	 * Constructor with all properties.
	 *
	 * @param sapId SAP unique material number
	 * @param entries collection of ErpInventoryEntryModel objects
	 */
	public ErpInventoryModel(String sapId, Date lastUpdated, List<ErpInventoryEntryModel> entries) {
		this.sapId = sapId;
		this.lastUpdated = lastUpdated;
		this.entries = new ArrayList<ErpInventoryEntryModel>( entries );
		Collections.<ErpInventoryEntryModel>sort( this.entries );
		this.entries = Collections.unmodifiableList( this.entries );
	}

	/**
	 * Get SAP Material ID.
	 *
	 * @return SAP ID
	 */
	public String getSapId() {
		return this.sapId;
	}

	/**
	 * Get the date the inventory was last refreshed on.
	 *
	 * @return date of last inventory update
	 */
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	/**
	 * Get all inventory entries.
	 *
	 * @return collection of ErpInventoryEntryModel objects
	 */
	public List<ErpInventoryEntryModel> getEntries() {
		return this.entries;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("ErpInventoryModel[");
		buf.append(sapId);
		for (ErpInventoryEntryModel e : this.entries) {
			buf.append("\n\t").append(e);
		}
		buf.append("\n]");
		return buf.toString();
	}
}
