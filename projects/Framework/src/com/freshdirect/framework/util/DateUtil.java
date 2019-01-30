package com.freshdirect.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.FastDateFormat;

public class DateUtil {

	public final static long SECOND = 1000;
	public final static long MINUTE = 60 * SECOND;
	public final static long HOUR = 60 * MINUTE;
	public final static long DAY = 24 * HOUR;
	public final static long WEEK = 7 * DAY;
	public final static long MONTH = 30 * DAY + DAY / 2;
	public final static long YEAR = 365 * DAY;

	private static final String MONTH_YEAR_DATE_FORMAT = "MM/yyyy";
	
	public final static int MORNING_END = 12; // 12:00 PM
	private static final DateFormat SIMPLE_TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");
	private static final DateFormat DATE_YEAR_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat MONTH_DATE_YEAR_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat MONTH_DATE_YEAR_DAY_FORMATTER = new SimpleDateFormat("MM/dd/yyyy E");
	
	public static final String MONTH_DAY_YEAR_DAYOFWEEK_FORMATTER_STRING = "MM/dd/yy EEE";
	private static final DateFormat MONTH_DAY_YEAR_DAYOFWEEK_FORMATTER = new SimpleDateFormat(MONTH_DAY_YEAR_DAYOFWEEK_FORMATTER_STRING);
	
	private static final DateFormat DATE_PLAIN = new SimpleDateFormat("yyyyMMdd");
	
	private static final DateFormat MIN_HOUR_FORMATTER = new SimpleDateFormat("h:mm a");
	private static final DateFormat DAY_INWEEK_FORMATTER = new SimpleDateFormat("E");
	private static final DateFormat MIN_AMPM_FORMATTER = new SimpleDateFormat("hh_mm_a");

	private static final DateFormat MON_DATE_YEAR_FORMATTER = new SimpleDateFormat("MMddyyyy");
	
	private static final DateFormat dateFormatwithTime = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
	
	public static final DateFormat DAY_OF_WK_FORMATTER = new SimpleDateFormat("EEE");
	
	public static final SimpleDateFormat CREDIT_CARD_EXPIRATION_DATE_FORMAT = new SimpleDateFormat("MM/yyyy");
	
	public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
	
	public static DateFormat hourInDayFormat = new SimpleDateFormat("H:mm");
	
	public static Calendar clientCalendar = Calendar.getInstance();
	
	public static final String MON_D_YEAR_PATTERN = "MMM. d, yyyy";
	private static final DateFormat MON_D_YEAR_FORMATTER = new SimpleDateFormat(MON_D_YEAR_PATTERN);
	private static final DateFormat MON_D_YEAR = new SimpleDateFormat(MON_D_YEAR_PATTERN);
	private static final DateFormat YEAR_OF_THE_DATE = new SimpleDateFormat("yyyy");
	public static final DateFormat MONTH_DATE_FORMATTER = new SimpleDateFormat("MM/dd");
	public static final DateFormat DAY_MONTH_DATE_FORMATTER = new SimpleDateFormat("EEE, MMMM dd");
	public static final DateFormat FULL_DAY_OF_WK_FORMATTER = new SimpleDateFormat("EEEE");
	public static final DateFormat MON_DATE_FORMATTER = new SimpleDateFormat("MMM d");
	public static final DateFormat HOUR_AMPM_FORMATTER = new SimpleDateFormat("ha");
	public static final DateFormat HOUR_MMAMPM_FORMATTER = new SimpleDateFormat("h:mma");
	
    public static final String STANDARDIZED_DATE_PATTERN = "yyyy-MM-dd'T'HH:mmZ";

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
	public static Date getEOD() {		
		Calendar cal = Calendar.getInstance();
		cal = truncate(cal);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
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

	public static int calcDiffIndays(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		
		return Math.abs(cal2.get(Calendar.DATE) - cal1.get(Calendar.DATE));
		
	}
	/* @return get absolute difference between d1/d2 in days, rounded to nearest */
	public static int getDiffInDays(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) DAY)));
	}
	
	public static int getDurationInHours(Date d1, Date d2){
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) HOUR)));
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
	
	public static int getDiffInMinutesVE(Date d1, Date d2) {
		return (int) Math.round(((d1.getTime() - d2.getTime()) / (double) MINUTE));
	}

	public static float diffInDays(Date d1, Date d2) {
		return Math.abs((float) (d1.getTime() - d2.getTime()) / (float) DAY);
	}
	
	/**
	 *  Add a number of days to a Date object.
	 *  
	 * @param date
	 *            the date to add.
	 * @param days
	 *            the number of days to add.
	 * @return a date object, the specified number of days later then the
	 *         supplied one.
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
	
	public static Date getNextDate() {
		Calendar cal = Calendar.getInstance();
		cal = truncate(cal);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		cal = truncate(cal);
		return cal.getTime();
	}
	
	public static Date getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());		
		return cal.getTime();
	}
	
	public static boolean isSameDay(Date d1, Date d2) {
		return isSameDay(toCalendar(d1), toCalendar(d2));
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) && (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Round up to nearest hour if within 1 minute (eg 3:59 -> 4:00, 11:59:59 ->
	 * 12:00:00 etc)
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
	
	public static Date parseMDY(String dateValue) throws ParseException{
		return MONTH_DATE_YEAR_FORMATTER.parse(dateValue);
	}
	
	public static Date parseMDY2(String dateValue) throws ParseException{
		return MON_DATE_YEAR_FORMATTER.parse(dateValue);
	}
	
	public static Date parseMonDYear(String dateValue) throws ParseException {
		return MON_D_YEAR_FORMATTER.parse(dateValue);
	}

	public static String formatSimpleTime(Date dateValue){
		return SIMPLE_TIME_FORMATTER.format(dateValue);
	}
	
	public static String format(Date dateValue) {
		return DATE_YEAR_FORMATTER.format(dateValue);
	}
	
	public static String formatPlain(Date dateValue) {
		return DATE_PLAIN.format(dateValue);
	}
	
	public static String formatDate(Date dateValue) {
		return MONTH_DATE_YEAR_FORMATTER.format(dateValue);
	}
	
	public static String formatDateWithDay(Date dateValue) {
		return MONTH_DATE_YEAR_DAY_FORMATTER.format(dateValue);
	}
	
	public static String formatDateWithDayOfWeek(Date dateValue) {
		return MONTH_DAY_YEAR_DAYOFWEEK_FORMATTER.format(dateValue);
	}
	
	public static String formatDateWithMonDYear(Date dateValue) {
		return MON_D_YEAR.format(dateValue);
	}
	
	public static String formatDateWithYear(Date dateValue) {
		return YEAR_OF_THE_DATE.format(dateValue);
	}

	public static String formatDayOfWeek(Date dateValue) { // replacing with
															// thread safe
															// version.
		FastDateFormat fdf = FastDateFormat.getInstance("E");
		return fdf.format(dateValue);
	}
	
	public static String formatDayOfWk(Date dateValue) {
		if(null!=dateValue){
		return DAY_OF_WK_FORMATTER.format(dateValue);
		} else {
			return null;
		}
	}
	
	public static String formatTime(Date dateValue) {
		return MIN_HOUR_FORMATTER.format(dateValue);
	}
	
	public static String formatTimeAMPM(Date dateValue) {
		return MIN_AMPM_FORMATTER.format(dateValue);
	}
	
	public static Date parseTimeAMPM(String dateValue)  throws ParseException {
		return MIN_AMPM_FORMATTER.parse(dateValue);
	}
	
	public static String formatDay(Date dateValue) {
		return DAY_INWEEK_FORMATTER.format(dateValue);
	}
	
	public static String getDatewithTime(Date clientDate) throws ParseException {       
        return dateFormatwithTime.format(clientDate);
	}
	
	public static String getReceiptCutoffDate(Date date){
		return new SimpleDateFormat("EEEEE, MM/dd/yyyy, h:mm a").format(date);
	}
	
	public static String getCreditCardExpiryDate(Date date){
		return new SimpleDateFormat(MONTH_YEAR_DATE_FORMAT).format(date);
	}
	
	public static String formatDateByYear(Date date) {
		return new SimpleDateFormat("yyyy").format(date);
	}
	
	public static String formatDateByMonth(Date date) {
		return new SimpleDateFormat("MM").format(date);
	}
	
	/**
	 * Report relative time difference as english text.
	 * 
	 * The time is counted as date1 - date2, thus if abs is false and date2
	 * occured before, a negative prefix will be added. The result is also
	 * rounded to the largest logical time unit, ie. seconds, minutes, hours and
	 * days
	 * 
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
		if (T < SECOND)
			return "now";
		else if (T < 2 * SECOND)
			return "1 second " + suffix;
		else if (T < MINUTE)
			return "" + T / SECOND + " seconds " + suffix;
		else if (T < 2 * MINUTE)
			return "1 minute " + suffix;
		else if (T < HOUR)
			return "" + (T / MINUTE) + " minutes " + suffix;
		else if (T < 2 * HOUR)
			return "1 hour " + suffix;
		else if (T < DAY)
			return "" + (T / HOUR) + " hours " + suffix;
		else if (T < 2 * DAY)
			return "1 day " + suffix;
		else
			return "" + (T / DAY) + " days " + suffix;
	}
	
	public static String relativeDifferenceAsString2(Date date1, Date date2) {
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
		if (T < SECOND)
			return "now";
		else if (T < 2 * SECOND)
			return "1 second " + suffix;
		else if (T < MINUTE)
			return "" + T / SECOND + " seconds " + suffix;
		else if (T < 2 * MINUTE)
			return "1 minute " + suffix;
		else if (T < HOUR)
			return "" + (T / MINUTE) + " minutes " + suffix;
		else if (T < 2 * HOUR)
			return "1 hour " + suffix;
		else if (T < DAY)
			return "" + (T / HOUR) + " hours " + suffix;
		else if (T < 2 * DAY)
			return "1 day " + suffix;
		else if (T < WEEK)
			return "" + (T / DAY) + " days " + suffix;
		else if (T < 2 * WEEK)
			return "1 week " + suffix;
		else if (T < MONTH)
			return "" + (T / WEEK) + " weeks " + suffix;
		else if (T < 2 * MONTH)
			return "1 month " + suffix;
		else if (T < YEAR)
			return "" + (T / MONTH) + " months " + suffix;
		else if (T < 2 * YEAR)
			return "1 year " + suffix;
		else
			return "" + (T / YEAR) + " years " + suffix;
	}
	
	/**
	 * Returns the number of days since 1970-01-05, Monday for the date
	 * argument. Calculates using UTC, causes no issues with daylight saving.
	 */
	public static int getDayNumFromEpochFirstMonday(Date date){
		Calendar local = Calendar.getInstance();
		local.setTime(date);
		
		Calendar base = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		base.clear();
		base.set(1970, 0, 5);
		
		Calendar actual = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		actual.clear();
		actual.set(local.get(Calendar.YEAR),local.get(Calendar.MONTH),local.get(Calendar.DATE));
		
		return (int) ( (actual.getTimeInMillis() - base.getTimeInMillis()) / DAY );
	}
	
	/**
	 * Returns the number of weeks since 1970-01-05, Monday for the date
	 * argument. Calculates using UTC, causes no issues with daylight saving.
	 * Week number is zero-based.
	 */
	public static int getWeekNumFromEpochFirstMonday(Date date){
		int dayNum = getDayNumFromEpochFirstMonday(date);
		return (dayNum+1)/7;
	}	

	public static String getDate(Date dateVal) throws ParseException {		
        return MONTH_DATE_YEAR_FORMATTER.format(dateVal);
	}

	public static boolean isPremiumSlot(Date baseDate, TimeOfDay cutoffTime, TimeOfDay premiumCutoffTime, int duration) {
		Calendar cal = Calendar.getInstance();
		Date cutoffDateTime = null;
		Date premiumCutoffDateTime =null;
		Date now = cal.getTime();
		//@TODO
		if (cutoffTime != null && premiumCutoffTime != null) {
			 cutoffDateTime = DateUtil.addDays(cutoffTime.getAsDate(baseDate),-1);
			 premiumCutoffDateTime = premiumCutoffTime.getAsDate(baseDate);
			 cal.setTime(premiumCutoffDateTime);
			 cal.add(Calendar.MINUTE, duration);
			 premiumCutoffDateTime = cal.getTime();
			if(now.after(cutoffDateTime))
				return true;
		}
		
		return false;
	}

	public static String getUTCDate(Date date) {
		TimeZone gmt = TimeZone.getTimeZone("UTC");
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(gmt);
		return formatter.format(date);
	}
	
    public static DateFormat getStandardizedDateFormatter() {
        return new SimpleDateFormat(STANDARDIZED_DATE_PATTERN);
    }

	public static Date getServerTime(Date clientDate) {
		try {
			if(clientDate != null) {
				return serverTimeFormat.parse(serverTimeFormat.format(clientDate));
			} 
		}catch(ParseException pe){
			
		}
		return null;
	}	
	
	public static String formatTimeRange(Date dateVal1, Date dateVal2) {
		try {
			String strTime1 = hourInDayFormat.format(dateVal1);
			String strTime2 = hourInDayFormat.format(addSeconds(dateVal2,1));
			return strTime1+" - "+strTime2;
		} catch (Exception e) {
			// Do Nothing
		}
        return "Error";
	}
	
	public static Date addSeconds(Date date, int seconds) {		
		
		clientCalendar.setTime(date);
		clientCalendar.add(Calendar.SECOND, seconds);
		return clientCalendar.getTime();
	}
	
	public static int monthsBetween(Date minuend, Date subtrahend)
    {
    Calendar cal = Calendar.getInstance();
    // default will be Gregorian in US Locales
    cal.setTime(minuend);
    int minuendMonth =  cal.get(Calendar.MONTH);
    int minuendYear = cal.get(Calendar.YEAR);
    cal.setTime(subtrahend);
    int subtrahendMonth =  cal.get(Calendar.MONTH);
    int subtrahendYear = cal.get(Calendar.YEAR);
     
    // the following will work okay for Gregorian but will not
    // work correctly in a Calendar where the number of months 
    // in a year is not constant
    return ((minuendYear - subtrahendYear) * cal.getMaximum(Calendar.MONTH)) +  
    (minuendMonth - subtrahendMonth);
    }
	
	public static String removeDotAfterMay(String text) {
		if (text.contains("May.")) {
			return text.replaceAll("May.", "May");
		} else {
			return text;
		}
	}
	
	public static String formatDateWithMonthAndDate(Date date){
		if(null!=date){
		return MONTH_DATE_FORMATTER.format(date);
		} else {
			return null;
		}
	}
	public static String formatDayAndMonth(Date date){
		if(null!=date){
			StringBuffer buff=new StringBuffer();
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			buff.append((cal.get(Calendar.MONTH)+1) +"/");
			buff.append(cal.get(Calendar.DATE));

		return buff.toString();
		} else {
			return null;
		}
	}
	
	/* When we want "11:59pm" or "11pm" */
	@SuppressWarnings("deprecation")
	public static String formatShortTimeNoZeroMins(TimeOfDay time) {
		String formattedTime = "";
		Date dateObj = time.getAsDate();
		
		if (dateObj.getMinutes() != 0) {
			DateFormat formatter = new SimpleDateFormat("h:mmaa");
			formattedTime = formatter.format(dateObj);
		} else {
			DateFormat formatter = new SimpleDateFormat("haa");
			formattedTime = formatter.format(dateObj);
		}
		
		return formattedTime.toLowerCase();
	}
	
	public static Date setTimeForDate(Date date, int hourOfDay ){
		Calendar cal= Calendar.getInstance();
		cal.setTime(date);
		cal=truncate(cal);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		return cal.getTime();
	}
	public static String formatFullDayOfWk(Date dateValue) {
		if(null!=dateValue){
		return FULL_DAY_OF_WK_FORMATTER.format(dateValue);
		} else {
			return null;
		}
	}
	
	public static String formatMonthAndDate(Date dateValue) {
		if(null!=dateValue){
		return MON_DATE_FORMATTER.format(dateValue);
		} else {
			return null;
		}
	}
	public static String formatHourAMPM(Date dateValue) {
		Calendar cal= Calendar.getInstance();
		if(null!=dateValue){
			cal.setTime(dateValue);
			 if (cal.get(Calendar.MINUTE) > 0) {
				 return HOUR_MMAMPM_FORMATTER.format(dateValue);
		        }
		return HOUR_AMPM_FORMATTER.format(dateValue);
		} else {
			return null;
		}
	}
	public static String formatHourAMPMRange(Date startDate, Date endDate){
		StringBuilder hourRange=new StringBuilder();
		String startDateString=null;
		String endDateString=null;
		if(null!=startDate){
			startDateString=formatHourAMPM(startDate);
		}
		if(null!=endDate){
			endDateString=formatHourAMPM(endDate);
		}
		hourRange.append(startDateString)
			.append("-")
			.append(endDateString);
		
		return hourRange.toString();
	}
	
	public static Date getSubsequentDeliveryDate(int frequency) {
		
		Calendar cl = Calendar.getInstance();
		cl.setTime(new Date());
		
		cl.add(Calendar.DATE, 7*frequency);
		
		cl.set(Calendar.HOUR, 0);
		cl.set(Calendar.MINUTE, 0);

		return cl.getTime();
	}
} 
