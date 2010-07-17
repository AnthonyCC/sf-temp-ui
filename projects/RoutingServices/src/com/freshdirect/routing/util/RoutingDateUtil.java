package com.freshdirect.routing.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.framework.util.DateUtil;

public class RoutingDateUtil {
	
	public final static long SECOND = 1000;
	public final static long MINUTE = 60 * SECOND;
	public final static long HOUR = 60 * MINUTE;
	public final static long DAY = 24 * HOUR;

	public final static int MORNING_END = 12;
	
	private static final DateFormat MIN_HOUR_SEC_FORMATTER = new SimpleDateFormat("HH:mm:ss");
	
	private static final DateFormat MIN_HOUR_SEC_PLAINFORMATTER = new SimpleDateFormat("HHmmss");
	
	private static final DateFormat MIN_HOUR_FORMATTER = new SimpleDateFormat("HH:mm");
	
	private static final DateFormat DATE_PLAIN = new SimpleDateFormat("yyyyMMdd");
	
	private static final DateFormat DATETIME_PLAIN = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private static final Calendar BASE_CALENDAR = Calendar.getInstance();
	
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	/** @return the calendar passed in, for convenience */
	public static Calendar truncate(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
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
	
	public static Date getCurrentDateTime() throws ParseException {		
		BASE_CALENDAR.setTimeInMillis(System.currentTimeMillis());
		return BASE_CALENDAR.getTime();
	}
	
	public static String formatPlain(Date dateValue) throws ParseException{
		return DATE_PLAIN.format(dateValue);
	}
	
	public static Date addSeconds(Date date, int seconds) {		
		BASE_CALENDAR.setTime(date);
		BASE_CALENDAR.add(Calendar.SECOND, seconds);
		return BASE_CALENDAR.getTime();
	}
	
	public static Date addMinutes(Date date, int minutes) {		
		BASE_CALENDAR.setTime(date);
		BASE_CALENDAR.add(Calendar.MINUTE, minutes);
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
	
	private static Calendar cal = Calendar.getInstance();
	
	public static Calendar toCalendar(Date date) {
		
		cal.setTime(date);
		return cal;
	}
	
	public static int getHourOfDay(Date date) {		
		return toCalendar(date).get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getDayOfWeek(Date date) {		
		return toCalendar(date).get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getMinute(Date date) {		
		return toCalendar(date).get(Calendar.MINUTE);
	}
	
	public static int getHourOfDay() {		
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute() {		
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	public static int getDiffInHours(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) HOUR)));
	}
	
	public static int getDiffInMinutes(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) MINUTE)));
	}
	
	public static int getDiffInSeconds(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) SECOND)));
	}
	
	public static String formatDateTime(Date dateValue1, Date dateValue2) throws ParseException{
		return MIN_HOUR_FORMATTER.format(dateValue1)+"-"+MIN_HOUR_FORMATTER.format(dateValue2);
	}	
	
	public static Date getNextDate() {
		Calendar cal = Calendar.getInstance();
		cal = truncate(cal);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}
	
}
