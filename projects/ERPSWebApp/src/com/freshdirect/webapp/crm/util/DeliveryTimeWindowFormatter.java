package com.freshdirect.webapp.crm.util;

import java.util.Calendar;
import java.util.Date;

public class DeliveryTimeWindowFormatter {
	public static final String SEP	= " - ";
	
	public static String formatTime(Date s, Date e) {
		int sh = dateToCalendar(s).get(Calendar.HOUR_OF_DAY);
		int eh = dateToCalendar(e).get(Calendar.HOUR_OF_DAY);
		
		if (sh < 12) {
			if (eh < 12) {
				return sh+SEP+eh+"am";
			} else if (eh == 12) {
				return sh+SEP+"noon";
			} else {
				return sh+SEP+(eh-12)+"pm";
			}
		} else if (sh == 12) {
			return "noon"+SEP+(eh-12);
		} else {
			return (sh-12)+SEP+(eh-12);
		}
	}
	
	public static Calendar dateToCalendar(final Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}
}
