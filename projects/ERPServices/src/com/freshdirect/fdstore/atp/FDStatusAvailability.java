package com.freshdirect.fdstore.atp;

import java.util.Date;

import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.framework.util.DateRange;

public class FDStatusAvailability implements FDAvailabilityI {

	private final EnumAvailabilityStatus status;

	private final FDAvailabilityI availability;

	public FDStatusAvailability(EnumAvailabilityStatus status,
			FDAvailabilityI availability) {
		this.status = status;
		this.availability = availability;
	}

	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		if (status == null || EnumAvailabilityStatus.AVAILABLE.equals(status)) {
			return availability.availableSomeTime(requestedRange);
		}
		return new FDStatusAvailabilityInfo(false, status);
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		if (status == null || EnumAvailabilityStatus.AVAILABLE.equals(status)) {
			return availability.availableCompletely(requestedRange);
		}
		return new FDStatusAvailabilityInfo(false, status);
	}

	public Date getFirstAvailableDate(DateRange requestedRange) {
		if (status == null || EnumAvailabilityStatus.AVAILABLE.equals(status)) {
			return availability.getFirstAvailableDate(requestedRange);
		}
		return null;
	}

}