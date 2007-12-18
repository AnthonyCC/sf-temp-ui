package com.freshdirect.delivery.restriction;

import java.util.Date;

import com.freshdirect.framework.util.DateRange;

public class OneTimeReverseRestriction extends AbstractRestriction {

	private final DateRange range;
	private final DateRange preRange;
	private final DateRange postRange;

	public OneTimeReverseRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,
		Date startDate,
		Date endDate) {
		super(id, criterion, reason, name, message);
		this.range = new DateRange(startDate, endDate);
		this.preRange = DateRange.upTo(startDate);
		this.postRange = DateRange.startingOn(endDate);
	}

	public boolean contains(Date date) {
		return !range.contains(date);
	}

	public boolean overlaps(DateRange r) {
		return preRange.overlaps(r) || postRange.overlaps(r);
	}

	public boolean contains(DateRange r) {
		return preRange.contains(r) || postRange.contains(r);
	}

	public String getDisplayDate() {
		return DateRangeFormat.format(range);
	}

	public DateRange getDateRange() {
		return range;
	}

	public EnumDlvRestrictionType getType() {
		return EnumDlvRestrictionType.ONE_TIME_RESTRICTION;
	}

}