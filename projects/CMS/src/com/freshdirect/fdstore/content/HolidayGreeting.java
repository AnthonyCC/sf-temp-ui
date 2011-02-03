package com.freshdirect.fdstore.content;

import java.util.Calendar;
import java.util.Date;

import com.freshdirect.cms.ContentKey;

public class HolidayGreeting extends ContentNodeModelImpl {
	public HolidayGreeting(ContentKey key) {
		super(key);
	}

	public String getDescription() {
		return getFullName();
	}

	public String getCode() {
		return getAttribute("CODE", null);
	}

	public Date getStartDate() {
		return (Date) getCmsAttributeValue("startDate");
	}

	public Date getEndDate() {
		return (Date) getCmsAttributeValue("endDate");
	}

	public String getGreetingText() {
		return getAttribute("GREETING_TEXT", "");
	}
	
	public Date getIntervalStartDate() {
		Date date = getStartDate();
		if (date != null)
			return alignGreetingDate(date, false);
		else
			return null;
	}

	public Date getIntervalEndDate() {
		Date date = getEndDate();
		if (date == null)
			date = getStartDate();
		if (date != null)
			return alignGreetingDate(date, true);
		else
			return null;
	}

	private Date alignGreetingDate(Date date, boolean end) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar now = Calendar.getInstance();
		cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (end)
			cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}
}
