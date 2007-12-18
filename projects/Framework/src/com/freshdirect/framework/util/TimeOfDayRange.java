package com.freshdirect.framework.util;

import java.util.Date;

public class TimeOfDayRange extends Range {

	public TimeOfDayRange(TimeOfDay start, TimeOfDay end) {
		super(start, end);
	}

	public TimeOfDay getStartTime() {
		return (TimeOfDay) getStart();
	}

	public TimeOfDay getEndTime() {
		return (TimeOfDay) getEnd();
	}

	public DateRange toDateRange(Date baseDate) {
		Date sd = getStartTime().getAsDate(baseDate);
		Date ed = getEndTime().getAsDate(baseDate);
		return new DateRange(sd, ed);
	}

	public static TimeOfDayRange upTo(TimeOfDay time) {
		return new TimeOfDayRange(TimeOfDay.MIDNIGHT, time);
	}

	public static TimeOfDayRange startingOn(TimeOfDay time) {
		return new TimeOfDayRange(time, TimeOfDay.NEXT_MIDNIGHT);
	}

}