package com.freshdirect.fdstore.atp;

import java.util.Date;

import com.freshdirect.framework.util.DateRange;

/**
 * Always available or unavailble.
 */
public class NullAvailability implements FDAvailabilityI {

	public final static FDAvailabilityI AVAILABLE = new NullAvailability(FDAvailabilityInfo.AVAILABLE);
	public final static FDAvailabilityI UNAVAILABLE = new NullAvailability(FDAvailabilityInfo.UNAVAILABLE);

	private final FDAvailabilityInfo availabilityInfo;

	private NullAvailability(FDAvailabilityInfo availabilityInfo) {
		this.availabilityInfo = availabilityInfo;
	}

	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		return this.availabilityInfo;
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		return this.availabilityInfo;
	}

	public Date getFirstAvailableDate(DateRange requestedRange) {
		return this.availabilityInfo.isAvailable() ? requestedRange
				.getStartDate() : null;
	}

}