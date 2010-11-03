/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.atp;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateRange;

/**
 * Class representing inventory information (short term availability).
 * 
 * @version $Revision$
 * @author $Author$
 */
public class FDStockAvailability implements Serializable, FDAvailabilityI {
	private static final long serialVersionUID = -3637574047051551989L;

	private final ErpInventoryModel inventory;

	private final double reqQty;
	private final double minQty;
	private final double qtyInc;

	public FDStockAvailability(ErpInventoryModel inventory, double reqQty, double minQty, double qtyInc) {
		if (inventory == null) {
			throw new IllegalArgumentException("Inventory cannot be null");
		}
		this.inventory = inventory;
		this.reqQty = reqQty;
		this.minQty = minQty;
		this.qtyInc = qtyInc;
	}

	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		return checkAvailability(false, requestedRange);
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		return checkAvailability(true, requestedRange);
	}
	
	public Date getFirstAvailableDate(DateRange requestedRange) {
		List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
		if (entries.isEmpty()) {
			return null;
		}

		double avQty = 0;
		for (ErpInventoryEntryModel e : entries) {
			if (!e.getStartDate().before(requestedRange.getEndDate())) {
				break;
			}
			avQty += e.getQuantity();
			if (roundQuantity(avQty, minQty, qtyInc) >= reqQty) {
				return e.getStartDate();
			}
		}
			
		return null;
	}


	private FDAvailabilityInfo checkAvailability(boolean always, DateRange requestedRange) {

		List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
		if (entries.isEmpty()) {
			return FDAvailabilityInfo.AVAILABLE;
		}

		double avQty = 0;
		for (ErpInventoryEntryModel e : entries) {
			if (always ? e.getStartDate().after(requestedRange.getStartDate()) : !e.getStartDate().before(
				requestedRange.getEndDate())) {
				break;
			}
			avQty += e.getQuantity();
		}

		avQty = roundQuantity(avQty, minQty, qtyInc);

		return new FDStockAvailabilityInfo(avQty >= reqQty, avQty);
	}

	private static double roundQuantity(double quantity, double minimumQuantity, double quantityIncrement) {
		if (quantity < minimumQuantity) {
			return 0.0;
		}
		return Math.floor((quantity - minimumQuantity) / quantityIncrement) * quantityIncrement + minimumQuantity;
	}

	public String toString() {
		return "FDStockAvailability[" + this.reqQty + " " + this.minQty + " " + this.qtyInc + " " + this.inventory + "]";
	}


}