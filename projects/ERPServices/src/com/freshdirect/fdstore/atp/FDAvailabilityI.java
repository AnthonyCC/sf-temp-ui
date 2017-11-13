package com.freshdirect.fdstore.atp;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;

public interface FDAvailabilityI extends Serializable {

	/**
	 * Check if item is available at all within the specified period.
	 */
	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange);

	/**
	 * Check if item is available without any limitations in the specified period.
	 */
	public FDAvailabilityInfo availableCompletely(DateRange requestedRange);
	
	/**
	 * Get the first day the item is fully available on.
	 * 
	 * @param requestedRange never null
	 * @return date, or null if item is not available
	 */
	public Date getFirstAvailableDate(DateRange requestedRange);
	
	/**
	 * Note: should be provision to return zero immediately if property:
	 FDStoreProperties.getEnableFDXDistinctAvailability()) 
			Is set to false or missing.
	 * @param targetDate date to check for inventory qty, if NULL will default to Today.
	 * @return qty avail for targetDate, if FDX and qty 0 for  Target date, includes next date. 
	 * 
	 */
	 public double getAvailabileQtyForDate(java.util.Date targetDate);

}