/*
 * $Workfile:FDTimeslot.java$
 *
 * $Date:3/13/03 5:27:00 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Category;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.util.DateUtil;

/**
 *
 *
 * @version $Revision:3$
 * @author $Author:Kashif Nadeem$
 */
public class FDTimeslot implements Serializable {

	private final static Category LOGGER = Category.getInstance(FDTimeslot.class);
	
	private final DlvTimeslotModel dlvTimeslot;

	/** Creates new FDTimeslot */
	public FDTimeslot(DlvTimeslotModel dlvTimeslot) {
		this.dlvTimeslot = dlvTimeslot;
	}

	public Date getBaseDate() {
		return dlvTimeslot.getBaseDate();
	}

	public Date getBegDateTime() {
		return dlvTimeslot.getStartTimeAsDate();
	}

	public Date getEndDateTime() {
		return dlvTimeslot.getEndTimeAsDate();
	}

	public Date getCutoffDateTime() {
		return dlvTimeslot.getCutoffTimeAsDate();
	}

	public String getZoneId() {
		return dlvTimeslot.getZoneId();
	}

	public int getTotalAvailable() {
		return dlvTimeslot.getTotalAvailable();
	}

	public int getBaseAvailable() {
		return dlvTimeslot.getBaseAvailable();
	}

	public int getChefsTableAvailable() {
		return dlvTimeslot.getChefsTableAvailable();
	}

	public int getStatusCode() {
		return dlvTimeslot.getStatus().getValue();
	}

	public String getTimeslotId() {
		return dlvTimeslot.getPK().getId();
	}

	public String getDisplayString(boolean forceAmPm) {
		return format(forceAmPm, DateUtil.toCalendar(this.getBegDateTime()), DateUtil.toCalendar(this.getEndDateTime()));
	}
	
	public String getDisplayString() {
		return getDisplayString(false);
	}

	public static String format(Date startDate, Date endDate) {
		return format(true, DateUtil.toCalendar(startDate), DateUtil.toCalendar(endDate));
	}

	private static String format(boolean forceAmPm, Calendar startCal, Calendar endCal) {
		StringBuffer sb = new StringBuffer();

		boolean showMarker =
			forceAmPm
				|| ((startCal.get(Calendar.HOUR_OF_DAY) < DateUtil.MORNING_END
					&& endCal.get(Calendar.HOUR_OF_DAY) > DateUtil.MORNING_END));

		formatCal(startCal, false, sb);

		sb.append(" - ");

		formatCal(endCal, showMarker, sb);

		return sb.toString();
	}

	private static void formatCal(Calendar cal, boolean showAmPm, StringBuffer sb) {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int marker = cal.get(Calendar.AM_PM);

		if (hour == 0) {
			sb.append("midnight");
		} else if (hour == 12) {
			sb.append("noon");
		} else if (hour > 12) {
			sb.append(hour - 12);
		} else {
			sb.append(hour);
		}

		if (minute != 0) {
			sb.append(":").append(minute);
		}

		if (showAmPm && hour != 12 && hour != 0) {
			if (marker == Calendar.AM) {
				sb.append("am");
			} else {
				sb.append("pm");
			}
		}
	}
	
	public boolean isMatching(Date baseDate, Date startTime, Date endTime){
	    return this.dlvTimeslot.isMatching(baseDate, startTime, endTime);
	}

	public DlvTimeslotModel getDlvTimeslot() {
		return dlvTimeslot;
	}

}
