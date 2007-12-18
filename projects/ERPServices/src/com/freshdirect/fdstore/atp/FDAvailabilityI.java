package com.freshdirect.fdstore.atp;

import java.io.Serializable;
import java.util.Date;

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

}