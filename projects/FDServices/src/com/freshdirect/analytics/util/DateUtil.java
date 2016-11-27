package com.freshdirect.analytics.util;

import java.util.Date;

public class DateUtil {

	public final static long SECOND = 1000;
	public final static long MINUTE = 60 * SECOND;
	public final static long HOUR = 60 * MINUTE;
	public final static long DAY = 24 * HOUR;
	

	/* @return get absolute difference between d1/d2 in days, rounded to nearest */
	public static int getDiffInDays(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (float) DAY)));
	}

} 