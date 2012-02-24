package com.freshdirect.analytics.util;

import java.util.Date;

public class DateUtil {

	public final static long SECOND = 1000;
	public final static long MINUTE = 60 * SECOND;
	public final static long HOUR = 60 * MINUTE;
	public final static long DAY = 24 * HOUR;
	
	public static float diffInDays(Date d1, Date d2) {
		
		return Math.abs((float) (d1.getTime() - d2.getTime()) / (float) DAY);
	}
	

} 