package com.freshdirect.routing.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.framework.util.DateUtil;

public class RoutingDateUtil {
	
	private static final DateFormat MIN_HOUR_SEC_FORMATTER = new SimpleDateFormat("HH:mm:ss");
	
	private static final DateFormat MIN_HOUR_SEC_PLAINFORMATTER = new SimpleDateFormat("HHmmss");
	
	private static final DateFormat DATE_PLAIN = new SimpleDateFormat("yyyyMMdd");
	
	private static final DateFormat DATETIME_PLAIN = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private static final Calendar BASE_CALENDAR = Calendar.getInstance();
	
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static String formatTime(Date dateValue) throws ParseException{
		return MIN_HOUR_SEC_FORMATTER.format(dateValue);
	}
	
	public static String formatDateTime(Date dateValue) throws ParseException{
		return DATETIME_PLAIN.format(dateValue);
	}
	
	public static String getCurrentTime() throws ParseException {		
		BASE_CALENDAR.setTimeInMillis(System.currentTimeMillis());
		return MIN_HOUR_SEC_PLAINFORMATTER.format(BASE_CALENDAR.getTime());
	}
	
	public static String formatPlain(Date dateValue) throws ParseException{
		return DATE_PLAIN.format(dateValue);
	}
	
	public static Date addSeconds(Date date, int seconds) {		
		BASE_CALENDAR.setTime(date);
		BASE_CALENDAR.add(Calendar.SECOND, seconds);
		return BASE_CALENDAR.getTime();
	}
	
	public static Date reduceTimeByPercent(Date dateStart, Date dateEnd, double percentage) {
		int minutesBetween = DateUtil.getDiffInMinutes(dateStart, dateEnd);
		int percentageMinutes = (int)(minutesBetween*percentage);
		
		BASE_CALENDAR.setTime(dateStart);
		BASE_CALENDAR.add(Calendar.MINUTE, percentageMinutes);
		return BASE_CALENDAR.getTime();
	}
	
	public static Date getDate(String dateString) throws ParseException {		
        return (Date)dateFormat.parse(dateString);
	}
	
	
}
