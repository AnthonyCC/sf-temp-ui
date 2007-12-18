/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapOrderLineI;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.bapi.BapiMaterialAvailability;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class MaterialAvailabilityHelper {
	private final BapiMaterialAvailability bapi;
	private final SapOrderI order;
	private final SapOrderLineI orderLine;

	public MaterialAvailabilityHelper(BapiMaterialAvailability bapi, SapOrderI order, SapOrderLineI orderLine) {
		this.order = order;
		this.orderLine = orderLine;
		this.bapi = bapi;
	}
	
	public SapOrderLineI getOrderLine() {
		return this.orderLine;	
	}

	protected void build() {
		this.bapi.setPlant( SapProperties.getPlant() );
		this.bapi.setStgeLoc( SapProperties.getStgeLoc() );

		this.bapi.setCustomerNumber( this.order.getCustomer().getSapCustomerNumber() );
		this.bapi.setMaterialNumber( this.orderLine.getMaterialNumber() );
		this.bapi.setSalesUnit( this.orderLine.getSalesUnit() );
		
		this.bapi.addRequest(order.getRequestedDate(), orderLine.getQuantity());
	}

	/**
	 * Update availability on orderline.
	 */
	protected void process(BapiMaterialAvailability bapi) {
		
		final int size = bapi.getCommitedLength();

		// fill inventory entries
		List inventoryEntries = new ArrayList( size );
		for (int i=0; i<size; i++) {
			Date confDate = bapi.getCommitedDate(i);
			double confQty = bapi.getCommitedQty(i);
			inventoryEntries.add(
				new ErpInventoryEntryModel( new java.sql.Date( confDate.getTime() ), confQty )
			);
		}
		
		List inventories = new ArrayList(1);
		inventories.add(new ErpInventoryModel(this.orderLine.getMaterialNumber(), new Date(), inventoryEntries));

		this.orderLine.setInventories(inventories);
	}

	
	
}