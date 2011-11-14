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
		
		List<FDLimitedAvailabilityInfo> limitedAvInfo = getLimitedAvailabilityInfo();
		if(!limitedAvInfo.isEmpty()){
			//If material falls under restricted availability.
			double avQty = 0;
			for(FDLimitedAvailabilityInfo l: limitedAvInfo) {
				if(l.getRequestedDate().after(requestedRange.getEndDate())){
					break;
				}
				if(!l.isAvailable())
					//If material unavailable for that day then reset availability.
					avQty = 0;
				else
					avQty = l.getQuantity();
				if (roundQuantity(avQty, minQty, qtyInc) >= reqQty) {
					return l.getRequestedDate();
				}
			}
		} else {
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
		}
		return null;
	}


	private FDAvailabilityInfo checkAvailability(boolean always, DateRange requestedRange) {

		List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
		if (entries.isEmpty()) {
			return FDAvailabilityInfo.AVAILABLE;
		}
		double avQty = 0;

		List<FDLimitedAvailabilityInfo> limitedAvInfo = getLimitedAvailabilityInfo();
		if(!limitedAvInfo.isEmpty()){
			//If material falls under restricted availability.
			for(FDLimitedAvailabilityInfo l: limitedAvInfo) {
				if (always ? l.getRequestedDate().after(requestedRange.getStartDate()) : l.getRequestedDate().after(
					requestedRange.getEndDate())) {
					break;
				}

				avQty = l.getQuantity();
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
		while(isDeliverable(lastUnavailDate) || count < FDStoreProperties.getAvailDaysInPastToLookup()){
			//Check previuos day.
			lastUnavailDate = DateUtil.addDays(lastUnavailDate, -1);
			count++;
		}

		while(!nextDate.after(endDate)) {
			FDLimitedAvailabilityInfo limitedAvInfo = null;
			if(!isDeliverable(nextDate)){
				limitedAvInfo =  new FDLimitedAvailabilityInfo(nextDate, 0.0, false);
				lastUnavailDate = nextDate;
			} else {
				List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
				for (ErpInventoryEntryModel e : entries) {
					if (e.getStartDate().after(nextDate)) {
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

	private ErpInventoryEntryModel getInventoryEntry(Date input){
		if(input == null)
			throw new IllegalArgumentException("ErpInventoryEntryModel getInventoryEntry(Date input) : Input Date cannot be Null");
		List<ErpInventoryEntryModel> entries = this.inventory.getEntries();
		if(entries.isEmpty())
			return null;
		for (ErpInventoryEntryModel e : entries) {
			if(input.compareTo(e.getStartDate()) == 0)
				return e;
		}
		return null;
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