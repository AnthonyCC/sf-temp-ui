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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.OncePerRequestDateCache;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

/**
 * Class representing inventory information (short term availability).
 * 
 * @version $Revision$
 * @author $Author$
 */
public class FDStockAvailability implements Serializable, FDAvailabilityI {
	private static final long serialVersionUID = -3637574047051551989L;

	private final ErpInventoryModel inventory;

	// Added for APPDEV-991. This will be used while performing ATPCheck
	// for Limited Availability Items to ensure the old stock
	// is nto available to the customer. This is a temporary fix
	// until the ATP check on SAP side is fixed to include only new inventory.
	// after restricted day.
	private ErpInventoryModel inventoryExport;

	private final double reqQty;
	private final double minQty;
	private final double qtyInc;
	// introduced in story appdev 6184 to introduce the concept of another store
	// besides FD for calculaitons.
//	private EnumEStoreId enumStoreId = EnumEStoreId.FD;

	public FDStockAvailability(ErpInventoryModel inventoryModel, double reqQty, 
			double minQty, double qtyInc) {
		if (inventoryModel == null) {
			throw new IllegalArgumentException("Inventory cannot be null");
		}
		this.inventory = inventoryModel;
		this.reqQty = reqQty;
		this.minQty = minQty;
		this.qtyInc = qtyInc;
		//this.enumStoreId = estoreId;

	}

	public FDStockAvailability(ErpInventoryModel inventory, ErpInventoryModel inventoryFromExport, double reqQty,
			double minQty, double qtyInc) {
		if (inventory == null) {
			throw new IllegalArgumentException("Inventory cannot be null");
		}
		this.inventory = inventory;
		this.inventoryExport = inventoryFromExport;
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

		for (ErpInventoryEntryModel entryModel : entries) {
			if (!entryModel.getStartDate().before(requestedRange.getEndDate())) {
				break;
			}
			avQty += entryModel.getQuantity();
			if (roundQuantity(avQty, minQty, qtyInc) >= reqQty) {
				return entryModel.getStartDate();
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

		// Earliest Availability logic
		for (ErpInventoryEntryModel e : entries) {
			if (always ? e.getStartDate().after(requestedRange.getStartDate())
					: !e.getStartDate().before(requestedRange.getEndDate())) {
				break;
			}
			avQty += e.getQuantity();
		}

		avQty = roundQuantity(avQty, minQty, qtyInc);

		return new FDStockAvailabilityInfo(avQty >= reqQty, avQty);
	}

	private DateRange getHorizonDateRange() {

		Date baseDate = OncePerRequestDateCache.getToday();
		if (baseDate == null) {
			baseDate = new Date();
		}

		Calendar startCal = DateUtil.truncate(DateUtil.toCalendar(baseDate));
		Calendar endCal = (Calendar) startCal.clone();

		startCal.add(Calendar.DATE, 1);
		endCal.add(Calendar.DATE, ErpServicesProperties.getHorizonDays());
		return new DateRange(startCal.getTime(), endCal.getTime());
	}

	private static double roundQuantity(double quantity, double minimumQuantity, double quantityIncrement) {
		if (quantity < minimumQuantity) {
			return 0.0;
		}
		return Math.floor((quantity - minimumQuantity) / quantityIncrement) * quantityIncrement + minimumQuantity;
	}

	private boolean isDeliverable(Date input) {
		boolean deliverable = false;
		return deliverable;
	}

	public String toString() {
		return "FDStockAvailability[" + this.reqQty + " " + this.minQty + " " + this.qtyInc + " " + this.inventory
				+ "]";
	}
	
	
	boolean areDatesEqual(java.util.Date date1, java.util.Date date2) {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));

	}

	/**
	 * 
	 */
	public double getAvailabileQtyForDate(java.util.Date targetDate) {
		/*
		 * If the store is Foodkick (FDX) and the available qty is zero for that
		 * day, we are allowed to look ahead ONE following day for inventory.
		 */
		if (! FDStoreProperties.getEnableFDXDistinctAvailability()) {
			return -999;
		}

		double avQty = 0;
		if (null == targetDate)
			targetDate = new Date();// set the date to today if null
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(targetDate);
		tomorrow.add(Calendar.DATE, 1);
		List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
		if (entries.isEmpty()) {
			return avQty;
		}

		for (ErpInventoryEntryModel entryModel : entries) {
			if (areDatesEqual(entryModel.getStartDate(),targetDate)) {
				avQty += entryModel.getQuantity();
				if (/*!this.enumStoreId.equals(EnumEStoreId.FDX) ||*/ avQty > 0)
					break;
				// if we are here, its fdx and the qty is zero

			}

			if (areDatesEqual( entryModel.getStartDate(), tomorrow.getTime()) 
						/*&& this.enumStoreId.equals(EnumEStoreId.FDX)*/ && avQty == 0) {
				avQty += entryModel.getQuantity();
				break;

			}

		}
		return avQty;

	}
	// below methods are for SF 2.0 jason parsers
	public ErpInventoryModel getInventory() {
		return inventory;
	}

	public ErpInventoryModel getInventoryExport() {
		return inventoryExport;
	}

	public double getReqQty() {
		return reqQty;
	}

	public double getMinQty() {
		return minQty;
	}

	public double getQtyInc() {
		return qtyInc;
	}
	
}