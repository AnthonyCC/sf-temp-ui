package com.freshdirect.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public final static long SECOND = 1000;
	public final static long MINUTE = 60 * SECOND;
	public final static long HOUR = 60 * MINUTE;
	public final static long DAY = 24 * HOUR;

	public final static int MORNING_END = 12; // 12:00 PM
	private static final DateFormat DATE_YEAR_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat MONTH_DATE_YEAR_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	
	private static final DateFormat DATE_PLAIN = new SimpleDateFormat("yyyyMMdd");
	
	private static final DateFormat MIN_HOUR_FORMATTER = new SimpleDateFormat("h:mm a");
	private static final DateFormat DAY_INWEEK_FORMATTER = new SimpleDateFormat("E");
	private static final DateFormat MIN_AMPM_FORMATTER = new SimpleDateFormat("hh_a");
	
	private DateUtil() {
	}

	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static int getDayOfWeek(Date date) {		
		return toCalendar(date).get(Calendar.DAY_OF_WEEK);
	}

	public static Date truncate(Date date) {
		Calendar cal = toCalendar(date);
		cal = truncate(cal);
		return cal.getTime();
	}

	/** @return the calendar passed in, for convenience */
	public static Calendar truncate(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/* @return get absolute difference between d1/d2 in days, rounded to nearest */
	public static int getDiffInDays(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) DAY)));
	}
	
	public static int getDiffInDaysFloor(Date d1, Date d2) {
		return Math.abs((int) Math.floor(((d1.getTime() - d2.getTime()) / (double) DAY)));
	}
	
	public static int getDiffInMinutes(TimeOfDay t1, TimeOfDay t2) {
		return getDiffInMinutes(t1.getAsDate(), t2.getAsDate());
	}

	public static int getDiffInMinutes(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) MINUTE)));
	}

	/**
	 *  Add a number of days to a Date object.
	 *  
	 *  @param date the date to add.
	 *  @param days the number of days to add.
	 *  @return a date object, the specified number of days later then the supplied one.
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	public static Date addHours(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hours);
		return cal.getTime();
	}
	
	

	public static boolean isSameDay(Date d1, Date d2) {
		return isSameDay(toCalendar(d1), toCalendar(d2));
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
			&& (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
			&& (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Round up to nearest hour if within 1 minute (eg 3:59 -> 4:00, 11:59:59 -> 12:00:00 etc)
	 */
	public static Date roundUp(Date date) {
		Calendar cal = DateUtil.toCalendar(date);
		if (cal.get(Calendar.MINUTE) != 59) {
			return date;
		}
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		cal = DateUtil.truncate(cal);
		cal.add(Calendar.HOUR_OF_DAY, hours + 1);
		return cal.getTime();
	}

	public static Date min(Date d1, Date d2) {
		return d1.before(d2) ? d1 : d2;
	}

	public static Date max(Date d1, Date d2) {
		return d1.after(d2) ? d1 : d2;
	}

	public static Date parse(String dateValue) throws ParseException{
		return DATE_YEAR_FORMATTER.parse(dateValue);
	}
	
	public static String format(Date dateValue) throws ParseException{
		return DATE_YEAR_FORMATTER.format(dateValue);
	}
	
	public static String formatPlain(Date dateValue) throws ParseException{
		return DATE_PLAIN.format(dateValue);
	}
	
	public static String formatDate(Date dateValue) throws ParseException{
		return MONTH_DATE_YEAR_FORMATTER.format(dateValue);
	}
	
	public static String formatTime(Date dateValue) throws ParseException{
		return MIN_HOUR_FORMATTER.format(dateValue);
	}
	
	public static String formatTimeAMPM(Date dateValue) throws ParseException{
		return MIN_AMPM_FORMATTER.format(dateValue);
	}
	
	public static String formatDay(Date dateValue) throws ParseException{
		return DAY_INWEEK_FORMATTER.format(dateValue);
	}
	
	
	
	/** Report relative time difference as english text. 
	 * 
	 * The time is counted as date1 - date2, thus if abs is false and date2 occured before, a negative prefix will be added.
	 * The result is also rounded to the largest logical time unit, ie. seconds, minutes, hours and days
	 * @param date1 
	 * @param date2
	 * @return time difference as english text
	 */
	public static String relativeDifferenceAsString(Date date1, Date date2) {
		// time in milliseconds		
		long T = date1.getTime() - date2.getTime();
		
		// display negative time as "T .. from now", with a positive T value
		String suffix;
		if (T >= 0) {
			suffix = "ago";
		} else {
			suffix = "from now";
			T = -T;
		}
		  
		// a bit ugly, e.g a minute less than two days is one day.
		if (T < SECOND) return "now";
		else if (T < 2*SECOND) return "1 second " + suffix;
		else if (T < MINUTE) return "" + T/SECOND + " seconds " + suffix;
		else if (T < 2*MINUTE) return "1 minute " + suffix;
		else if (T < HOUR) return "" + (T/MINUTE) + " minutes " + suffix;
		else if (T < 2*HOUR) return "1 hour " + suffix;
		else if (T < DAY) return "" + (T/HOUR) + " hours " + suffix;
		else if (T < 2*DAY) return "1 day " + suffix;
		else return "" + (T/DAY) + " days " + suffix;
	}
} 