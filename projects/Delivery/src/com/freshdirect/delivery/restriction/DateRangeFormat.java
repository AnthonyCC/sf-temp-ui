package com.freshdirect.delivery.restriction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

class DateRangeFormat {

	private static final SimpleDateFormat MDY_FORMATTER = new SimpleDateFormat("MMMM d, yyyy");
	private static final SimpleDateFormat MD_FORMATTER = new SimpleDateFormat("MMMM d");

	public static String format(DateRange range) {
		Calendar startCal = DateUtil.toCalendar(range.getStartDate());
		Calendar endCal = DateUtil.toCalendar(range.getEndDate());
		endCal.add(Calendar.MILLISECOND, -1); // adjust to exclude last msec

		//Simplest case one day.
		if (startCal.get(Calendar.DATE) == endCal.get(Calendar.DATE)) {
			return MDY_FORMATTER.format(range.getStartDate());
		}

		StringBuffer sb = new StringBuffer();
		sb.append(MD_FORMATTER.format(range.getStartDate()));
		sb.append("-");
		if (startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH)) {
			sb.append(endCal.get(Calendar.DATE));
		} else {
			sb.append(MD_FORMATTER.format(range.getEndDate()));
		}

		sb.append(", ");
		sb.append(startCal.get(Calendar.YEAR));
		return sb.toString();
	}

}