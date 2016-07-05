package com.freshdirect.webapp.ajax.expresscheckout.location.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpeningHourConstants {

	private static final String SUNDAY = "Sunday";
	private static final String SATURDAY = "Saturday";
	private static final String FRIDAY = "Friday";
	private static final String THURSDAY = "Thursday";
	private static final String WEDNESDAY = "Wednesday";
	private static final String TUESDAY = "Tuesday";
	private static final String MONDAY = "Monday";
	private static final String OPENING_HOUR_ON_WEEK_DAY = "1pm - 9pm";
	private static final String OPENING_HOUR_ON_WEEKEND_DAY = "9am - 5pm";
	private static final String CLOSED = "closed";

	private static final List<OpeningHourData> SHOP_OPEN_TIMES = new ArrayList<OpeningHourData>() {
		private static final long serialVersionUID = 2106406808714312376L;
		{
			add(new OpeningHourData(MONDAY, OPENING_HOUR_ON_WEEK_DAY));
			add(new OpeningHourData(TUESDAY, OPENING_HOUR_ON_WEEK_DAY));
			add(new OpeningHourData(WEDNESDAY, CLOSED));
			add(new OpeningHourData(THURSDAY, CLOSED));
			add(new OpeningHourData(FRIDAY, OPENING_HOUR_ON_WEEK_DAY));
			add(new OpeningHourData(SATURDAY, OPENING_HOUR_ON_WEEKEND_DAY));
			add(new OpeningHourData(SUNDAY, OPENING_HOUR_ON_WEEKEND_DAY));
		}
	};

	private static final List<OpeningHourData> UNMODIFIED_SHOP_OPEN_TIMES = Collections.unmodifiableList(SHOP_OPEN_TIMES);

	public static List<OpeningHourData> getOpeningHours() {
		return UNMODIFIED_SHOP_OPEN_TIMES;
	}

}
