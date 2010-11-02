package com.freshdirect.fdstore.atp;

import java.util.Date;

import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.framework.util.DateRange;

public class FDStatusAvailability implements FDAvailabilityI {
	private static final long serialVersionUID = 3877126382889070925L;

	private final EnumAvailabilityStatus status;

	private final FDAvailabilityI availability;

	public FDStatusAvailability(EnumAvailabilityStatus status,
			FDAvailabilityI availability) {
		this.status = status;
		this.availability = availability;
	}

	@Override
	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		if (status == null || EnumAvailabilityStatus.AVAILABLE.equals(status)) {
			return availability.availableSomeTime(requestedRange);
		}
		return new FDStatusAvailabilityInfo(false, status);
	}

	@Override
	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		if (status == null || EnumAvailabilityStatus.AVAILABLE.equals(status)) {
			return availability.availableCompletely(requestedRange);
		}
		return new FDStatusAvailabilityInfo(false, status);
	}

	@Override
	public Date getFirstAvailableDate(DateRange requestedRange) {
		if (status == null || EnumAvailabilityStatus.AVAILABLE.equals(status)) {
			return availability.getFirstAvailableDate(requestedRange);
		}
		return null;
	}
}
