package com.freshdirect.delivery.restriction;

import java.util.List;

import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.framework.util.DateRange;

/**
 * Restricted availability that always applies if requestedRange overlaps a fixed target range.
 */
public class FDSpecialRestrictedAvailability extends AbstractAvailability {

	private final FDAvailabilityI availability;
	private final DlvRestrictionsList restrictions;
	private final DateRange targetRequestedRange;
	private final DateRange paramRequestedRange;

	public FDSpecialRestrictedAvailability(
		FDAvailabilityI availability,
		DlvRestrictionsList restrictions,
		DateRange targetRequestedRange,
		DateRange paramRequestedRange) {

		this.availability = availability;
		this.restrictions = restrictions;
		this.targetRequestedRange = targetRequestedRange;
		this.paramRequestedRange = paramRequestedRange;
	}

	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		if (targetRequestedRange.overlaps(requestedRange)) {
			List lst = this.restrictions.getRestrictionsContaining(paramRequestedRange);
			if (!lst.isEmpty()) {
				return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
			}
		}

		return this.availability.availableSomeTime(requestedRange);
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		if (targetRequestedRange.overlaps(requestedRange)) {
			List lst = this.restrictions.getRestrictions(paramRequestedRange);
			if (!lst.isEmpty()) {
				return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
			}
		}

		return this.availability.availableCompletely(requestedRange);
	}

	public String toString() {
		return "FDSpecialRestrictedAvailability[targetRequestedRange = "
			+ targetRequestedRange
			+ ", restrictions = "
			+ restrictions
			+ ", availability = "
			+ availability
			+ "]";
	}

}