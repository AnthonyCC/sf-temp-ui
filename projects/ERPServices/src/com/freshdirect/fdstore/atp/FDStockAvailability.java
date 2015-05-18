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
	
	//Added for APPDEV-991. This will be used while performing ATPCheck 
	//for Limited Availability Items to ensure the old stock
	//is nto available to the customer. This is a temporary fix
	//until the ATP check on SAP side is fixed to include only new inventory.
	//after restricted day.
	private ErpInventoryModel inventoryExport;
	
	private final Date[] availableDates;

	private final double reqQty;
	private final double minQty;
	private final double qtyInc;

	public FDStockAvailability(ErpInventoryModel inventory, double reqQty, double minQty, double qtyInc, Date[] availableDates) {
		if (inventory == null) {
			throw new IllegalArgumentException("Inventory cannot be null");
		}
		this.inventory = inventory;
		this.reqQty = reqQty;
		this.minQty = minQty;
		this.qtyInc = qtyInc;
		this.availableDates = availableDates;
	}

	public FDStockAvailability(ErpInventoryModel inventory, ErpInventoryModel inventoryFromExport, double reqQty, double minQty, double qtyInc, Date[] availableDates) {
		if (inventory == null) {
			throw new IllegalArgumentException("Inventory cannot be null");
		}
		this.inventory = inventory;
		this.inventoryExport = inventoryFromExport;
		this.reqQty = reqQty;
		this.minQty = minQty;
		this.qtyInc = qtyInc;
		this.availableDates = availableDates;
	}
	
	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		return checkAvailability(false, requestedRange);
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		return checkAvailability(true, requestedRange);
	}
	
	public Date getFirstAvailableDate(DateRange requestedRange) {
		List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
		if(entries.isEmpty()) {
			return null;
		}
		double avQty = 0;
		if(FDStoreProperties.isLimitedAvailabilityEnabled() && this.availableDates != null && this.availableDates.length > 0){
			//If material falls under limited availability.
			int count = 0;
			Date lastUnavailDate = requestedRange.getStartDate();
			//Find last Unavailable date.
			while(isDeliverable(lastUnavailDate) && count < FDStoreProperties.getAvailDaysInPastToLookup()){
				//Check previous day.
				lastUnavailDate = DateUtil.addDays(lastUnavailDate, -1);
				count++;
			}
			//Remove the time part.
			lastUnavailDate = DateUtil.truncate(lastUnavailDate);
			for (ErpInventoryEntryModel e : entries) {
				if (!e.getStartDate().before(requestedRange.getEndDate())) {
					break;
				}

				//Inventory start date cannot be before last unavailability date.
				if(lastUnavailDate == null || !e.getStartDate().before(lastUnavailDate))
					avQty += e.getQuantity();
				if (roundQuantity(avQty, minQty, qtyInc) >= reqQty) {
					return e.getStartDate();
				}				
			}
		}else {
			
			for (ErpInventoryEntryModel e : entries) {
				if (!e.getStartDate().before(requestedRange.getEndDate())) {
					break;
				}
				avQty += e.getQuantity();
				if (roundQuantity(avQty, minQty, qtyInc) >= reqQty) {
					return e.getStartDate();
				}
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

		if(FDStoreProperties.isLimitedAvailabilityEnabled() && this.availableDates != null && this.availableDates.length > 0){
			//If material falls under limited availability.

			if(!isDeliverable(requestedRange.getStartDate())){
				avQty = 0.0;
			} else {
				//The request date is deliverable.
				int count = 0;
				Date lastUnavailDate = requestedRange.getStartDate();
				//Find last Unavailable date.
				while(isDeliverable(lastUnavailDate) && count < FDStoreProperties.getAvailDaysInPastToLookup()){
					//Check previuos day.
					lastUnavailDate = DateUtil.addDays(lastUnavailDate, -1);
					count++;
				}
				//Remove the time part.
				lastUnavailDate = DateUtil.truncate(lastUnavailDate);
				double newInvQty = 0.0; //Remaining inventory from the last unavailable date.
				if(this.inventoryExport != null) {
					List<ErpInventoryEntryModel> exportEntries = this.inventoryExport.getEntries();
					for (ErpInventoryEntryModel e : exportEntries) {
						if (!e.getStartDate().before(requestedRange.getEndDate())) {
							break;
						}
	
						//Inventory start date cannot be before last unavailability date.
						if(lastUnavailDate == null || !e.getStartDate().before(lastUnavailDate))
							newInvQty += e.getQuantity();
					}
				}
				//Inventory received from ATP check.
				for (ErpInventoryEntryModel e : entries) {
					if (always ? e.getStartDate().after(requestedRange.getStartDate()) : !e.getStartDate().before(
							requestedRange.getEndDate())) {
							break;
						}

					//Inventory start date cannot be before last unavailability date.
					if(lastUnavailDate == null || !e.getStartDate().before(lastUnavailDate))
						avQty += e.getQuantity();
				}			
				
				if(this.inventoryExport != null && newInvQty <= 0.0){
					//When there are no new inventory after last restricted date from inventory export
					//then reset the quantity from ATP check to 0 as ATP can return old stock as per
					//current ATP check.
					avQty = newInvQty;
				}

			}
		} else {
			//Earliest Availability logic
			for (ErpInventoryEntryModel e : entries) {
				if (always ? e.getStartDate().after(requestedRange.getStartDate()) : !e.getStartDate().before(
					requestedRange.getEndDate())) {
					break;
				}
				avQty += e.getQuantity();
			}
		}
		avQty = roundQuantity(avQty, minQty, qtyInc);

		return new FDStockAvailabilityInfo(avQty >= reqQty, avQty);
	}

	public List<FDLimitedAvailabilityInfo> getLimitedAvailabilityInfo() {
		if(this.availableDates == null || this.availableDates.length == 0){
			List<FDLimitedAvailabilityInfo> emptyList = Collections.emptyList();
			return emptyList;
		}
		List<FDLimitedAvailabilityInfo>  limitedAvList = new ArrayList<FDLimitedAvailabilityInfo>();
		DateRange horizonDateRange = getHorizonDateRange();
		Date startDate = horizonDateRange.getStartDate();
		Date endDate = horizonDateRange.getEndDate();
		/*
		Date availDate = null;
		if(startDate.after(inventory.getInventoryStartDate()))
			availDate = inventory.getInventoryStartDate();
		else
			availDate = startDate;
			*/
		Date nextDate = startDate;
		
		
		double totalAvailable = 0.0;
		int count = 0;
		Date lastUnavailDate = new Date();
		//Initialize last Unavailable date.
		while(isDeliverable(lastUnavailDate) && count < FDStoreProperties.getAvailDaysInPastToLookup()){
			//Check previuos day.
			lastUnavailDate = DateUtil.addDays(lastUnavailDate, -1);
			count++;
		}
		//Remove the time part.
		lastUnavailDate = DateUtil.truncate(lastUnavailDate);
		while(!nextDate.after(endDate)) {
			FDLimitedAvailabilityInfo limitedAvInfo = null;
			if(!isDeliverable(nextDate)){
				limitedAvInfo =  new FDLimitedAvailabilityInfo(nextDate, 0.0, false);
				lastUnavailDate = nextDate;
				totalAvailable = 0.0;
			} else {
				List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
				for (ErpInventoryEntryModel e : entries) {
					if (!e.getStartDate().before(nextDate)) {
						break;
					}
					//Inventory start date cannot be before last unavailability date.
					if(lastUnavailDate == null || !e.getStartDate().before(lastUnavailDate))
						totalAvailable += e.getQuantity();
				}				
				limitedAvInfo =  new FDLimitedAvailabilityInfo(nextDate, totalAvailable, (roundQuantity(totalAvailable, minQty, qtyInc) >= reqQty) );
			}
			if(!nextDate.before(startDate))
				limitedAvList.add(limitedAvInfo);
			nextDate = DateUtil.addDays(nextDate, 1);
		}
		return limitedAvList;
		
	}


	private DateRange getHorizonDateRange() {
		
		Date baseDate = OncePerRequestDateCache.getToday();
		if(baseDate == null){
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
		if(input != null && this.availableDates != null){
			for(int i = 0; i < this.availableDates.length; i++) {
				Date availableDate = this.availableDates[i];
				if(DateUtil.isSameDay(input, availableDate))//Date match
					deliverable = true;
			}
		}
		return deliverable;
	}
	
	public String toString() {
		return "FDStockAvailability[" + this.reqQty + " " + this.minQty + " " + this.qtyInc + " " + this.inventory + "]";
	}


}