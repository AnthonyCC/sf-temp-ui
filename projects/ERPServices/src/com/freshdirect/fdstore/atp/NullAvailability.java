package com.freshdirect.fdstore.atp;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateRange;

/**
 * Always available or unavailable.
 */

public class NullAvailability implements FDAvailabilityI {
	private static final long serialVersionUID = -4992311712448289847L;

	public final static FDAvailabilityI AVAILABLE = new NullAvailability(FDAvailabilityInfo.AVAILABLE);
	public final static FDAvailabilityI UNAVAILABLE = new NullAvailability(FDAvailabilityInfo.UNAVAILABLE);

	private final FDAvailabilityInfo availabilityInfo;

	
	public NullAvailability(@JsonProperty("available") FDAvailabilityInfo availabilityInfo) {
		this.availabilityInfo = availabilityInfo;
	}

	public FDAvailabilityInfo getAvailabilityInfo() {
		return this.availabilityInfo;
	}
	
	@Override
	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		return this.availabilityInfo;
	}

	@Override
	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		return this.availabilityInfo;
	}

	@Override
	public Date getFirstAvailableDate(DateRange requestedRange) {
		return this.availabilityInfo.isAvailable() ? requestedRange
				.getStartDate() : null;
	}

	@Override
	public double getAvailabileQtyForDate(Date targetDate) {
		// TODO Auto-generated method stub
		//return -7;
		
		if (!FDStoreProperties.getEnableFDXDistinctAvailability()) {
			return -999;
		}
		if(AVAILABLE.equals(this)) {
			return 999;
		} else {
			return 0;
		}
	}
}
