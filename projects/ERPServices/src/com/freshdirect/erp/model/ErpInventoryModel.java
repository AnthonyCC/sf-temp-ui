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
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.FDVersion;
import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpInventory model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpInventoryModel extends ModelSupport implements FDVersion<Date> {

	/** SAP unique material number */
	private String sapId;

	/** Date of last inventory update */
	private Date lastUpdated;
	
	/**
	 * Inventory entries
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpInventoryEntryModel}>
	 */
	private List entries;

	/**
	 * Constructor with all properties.
	 *
	 * @param sapId SAP unique material number
	 * @param entries collection of ErpInventoryEntryModel objects
	 */
	public ErpInventoryModel(String sapId, Date lastUpdated, List entries) {
		this.sapId = sapId;
		this.lastUpdated = lastUpdated;
		this.entries = new ArrayList( entries );
		Collections.sort( this.entries );
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
	public List getEntries() {
		return this.entries;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("ErpInventoryModel[");
		buf.append(sapId);
		for (Iterator i=this.entries.iterator(); i.hasNext(); ) {
			ErpInventoryEntryModel e = (ErpInventoryEntryModel) i.next();
			buf.append("\n\t").append(e);
		}
		buf.append("\n]");
		return buf.toString();
	}

	@Override
	public Date getVersion() {
		return lastUpdated;
	}
}
