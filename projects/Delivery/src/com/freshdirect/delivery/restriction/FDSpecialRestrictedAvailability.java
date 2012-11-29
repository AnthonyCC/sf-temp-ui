package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.framework.util.DateRange;

/**
 * Restricted availability that always applies if requestedRange overlaps a fixed target range.
 */
public class FDSpecialRestrictedAvailability extends AbstractAvailability {

	private static  final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
			List<RestrictionI> lst = this.restrictions.getRestrictionsContainingExcept(paramRequestedRange,EnumDlvRestrictionCriterion.PURCHASE,EnumDlvRestrictionReason.PLATTER);
			if (!lst.isEmpty()) {
				return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
			}
			
			//Only for Platter Restrictions- OTR should take priority over RRN.
			Set<EnumDlvRestrictionReason> reasons =new HashSet<EnumDlvRestrictionReason>();
			reasons.add(EnumDlvRestrictionReason.PLATTER);
			List<RestrictionI> platterRestrictions =this.restrictions.getRestrictions(EnumDlvRestrictionCriterion.PURCHASE,EnumDlvRestrictionReason.PLATTER,EnumDlvRestrictionType.ONE_TIME_RESTRICTION);
			boolean isOTRRestrictionFound = false;
			if(null !=platterRestrictions && !platterRestrictions.isEmpty()){
				for (Iterator<RestrictionI> iterator = platterRestrictions.iterator(); iterator.hasNext();) {
					OneTimeRestriction restriction = (OneTimeRestriction) iterator.next();
					String startDate =dateFormat.format(restriction.getDateRange().getStartDate());
					if(startDate.equals(dateFormat.format(paramRequestedRange.getStartDate().getTime()))){
						isOTRRestrictionFound = true;					
						if (restriction.contains(paramRequestedRange)) {
							return new FDRestrictedAvailabilityInfo(false, restriction);
						}
						break;
					}					
				}
			}
			if(!isOTRRestrictionFound){
				lst =this.restrictions.getRestrictionsContaining(EnumDlvRestrictionCriterion.PURCHASE,EnumDlvRestrictionReason.PLATTER,EnumDlvRestrictionType.RECURRING_RESTRICTION,paramRequestedRange);
				if (!lst.isEmpty()) {
					return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
				}
			}
			
		}

		return this.availability.availableSomeTime(requestedRange);
	}

	public FDAvailabilityInfo availableCompletely(DateRange requestedRange) {
		if (targetRequestedRange.overlaps(requestedRange)) {
			List<RestrictionI> lst = this.restrictions.getRestrictionsExcept(paramRequestedRange,EnumDlvRestrictionCriterion.PURCHASE,EnumDlvRestrictionReason.PLATTER);
			if (!lst.isEmpty()) {
				return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
			}
			
			//Only for Platter Restrictions- OTR should take priority over RRN.
			Set<EnumDlvRestrictionReason> reasons =new HashSet<EnumDlvRestrictionReason>();
			reasons.add(EnumDlvRestrictionReason.PLATTER);
			List<RestrictionI> platterRestrictions =this.restrictions.getRestrictions(EnumDlvRestrictionCriterion.PURCHASE,EnumDlvRestrictionReason.PLATTER,EnumDlvRestrictionType.ONE_TIME_RESTRICTION);
			boolean isOTRRestrictionFound = false;
			if(null !=platterRestrictions && !platterRestrictions.isEmpty()){
				for (Iterator<RestrictionI> iterator = platterRestrictions.iterator(); iterator.hasNext();) {
					OneTimeRestriction restriction = (OneTimeRestriction) iterator.next();
					String startDate =dateFormat.format(restriction.getDateRange().getStartDate());
					if(startDate.equals(dateFormat.format(paramRequestedRange.getStartDate().getTime()))){
						isOTRRestrictionFound = true;					
						if (restriction.overlaps(paramRequestedRange)) {
							return new FDRestrictedAvailabilityInfo(false, restriction);
						}
						break;
					}					
				}
			}
			if(!isOTRRestrictionFound){
				lst =this.restrictions.getRestrictions(EnumDlvRestrictionCriterion.PURCHASE,EnumDlvRestrictionReason.PLATTER,EnumDlvRestrictionType.RECURRING_RESTRICTION,paramRequestedRange);
				if (!lst.isEmpty()) {
					return new FDRestrictedAvailabilityInfo(false, (RestrictionI) lst.get(0));
				}
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