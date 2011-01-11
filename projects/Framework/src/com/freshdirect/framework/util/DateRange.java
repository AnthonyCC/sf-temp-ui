package com.freshdirect.framework.util;

import java.util.Date;

public class DateRange extends Range<Date> {

	private static final long	serialVersionUID	= -9202120468747575170L;
	
	private final static Date PAST = new Date(Long.MIN_VALUE);
	private final static Date FUTURE = new Date(Long.MAX_VALUE);

	public DateRange(Date start, Date end) {
		super(start, end);
	}

	public Date getStartDate() {
		return getStart();
	}

	public Date getEndDate() {
		return getEnd();
	}

	public static DateRange upTo(Date date) {
		return new DateRange(PAST, date);
	}

	public static DateRange startingOn(Date date) {
		return new DateRange(date, FUTURE);
	}

}