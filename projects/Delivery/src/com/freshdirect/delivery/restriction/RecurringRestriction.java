package com.freshdirect.delivery.restriction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;

public class RecurringRestriction extends AbstractRestriction {

	private final int dayOfWeek;
	private final TimeOfDayRange timeRange;

	public RecurringRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,
		int dayOfWeek,
		TimeOfDay start,
		TimeOfDay end) {
		super(id, criterion, reason, name, message);
		this.dayOfWeek = dayOfWeek;
		this.timeRange = new TimeOfDayRange(start, end);
	}

	public boolean contains(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (this.dayOfWeek == c.get(Calendar.DAY_OF_WEEK)) {
			return timeRange.contains(new TimeOfDay(date));
		}
		return false;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public TimeOfDayRange getTimeRange() {
		return timeRange;
	}

	public String getDisplayDate() {
		return this.dayOfWeek + " " + timeRange.getStartTime() + " - " + timeRange.getEndTime();
	}

	/**
	 * @return List of DateRange
	 */
	private List toDateRanges(DateRange range) {
		ArrayList l = new ArrayList();
		Calendar endCal = DateUtil.truncate(DateUtil.toCalendar(range.getEndDate()));
		Calendar c = DateUtil.truncate(DateUtil.toCalendar(range.getStartDate()));
		while (!c.after(endCal)) {
			if (c.get(Calendar.DAY_OF_WEEK) == this.dayOfWeek) {
				l.add(timeRange.toDateRange(c.getTime()));
			}
			c.add(Calendar.DATE, 1);
		}
		return l;
	}

	public boolean overlaps(DateRange range) {
		List dateRanges = toDateRanges(range);
		for (Iterator i = dateRanges.iterator(); i.hasNext();) {
			DateRange r = (DateRange) i.next();
			if (r.overlaps(range)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(DateRange range) {
		List dateRanges = toDateRanges(range);
		for (Iterator i = dateRanges.iterator(); i.hasNext();) {
			DateRange r = (DateRange) i.next();
			if (r.contains(range)) {
				return true;
			}
		}
		return false;
	}

	public EnumDlvRestrictionType getType() {
		return EnumDlvRestrictionType.RECURRING_RESTRICTION;
	}

}