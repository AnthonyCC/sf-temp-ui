package com.freshdirect.delivery.restriction;

import java.util.Date;

import com.freshdirect.framework.util.DateRange;

/**
 * A restriction on a specific date range.
 */
public class OneTimeRestriction extends AbstractRestriction {

	private final DateRange range;

	public OneTimeRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,
		Date startDate,
		Date endDate,String path) {
		super(id, criterion, reason, name, message,path);
		this.range = new DateRange(startDate, endDate);
	}

	public boolean contains(Date date) {
		return range.contains(date);
	}

	public boolean overlaps(DateRange r) {
		return range.overlaps(r);
	}

	public boolean contains(DateRange r) {
		return range.contains(r);
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