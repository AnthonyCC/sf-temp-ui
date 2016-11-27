package com.freshdirect.delivery.restriction;

import java.util.List;

import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.framework.util.DateRange;

/**
 * Restricted availability that checks against restrictions in requestedRange.
 */
public class FDRestrictedAvailability extends AbstractAvailability {
	private static final long serialVersionUID = 1762949850169722695L;

	private final FDAvailabilityI availability;
	private final DlvRestrictionsList restrictions;

	public FDRestrictedAvailability(FDAvailabilityI availability, DlvRestrictionsList restrictions) {
		this.availability = availability;
		this.restrictions = restrictions;
	}

	public FDAvailabilityInfo availableSomeTime(DateRange requestedRange) {
		List<RestrictionI> lst = this.restrictions.getRestrictionsContaining(requestedRange);

		if (lst.isEmpty()) {
			return this.availability.availableSomeTime(requestedRange);
		}

		return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		List<RestrictionI> lst = this.restrictions.getRestrictions(requestedRange);

		if (lst.isEmpty()) {
			return this.availability.availableCompletely(requestedRange);
		}

		return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
	}
	
	public String toString() {
		return "FDRestrictedAvailability[restrictions = " + restrictions + ", availability = " + availability + "]";
	}

}