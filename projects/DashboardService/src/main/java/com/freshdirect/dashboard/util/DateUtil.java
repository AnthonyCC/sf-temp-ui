package com.freshdirect.dashboard.util;

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
	
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
	
	public static DateFormat dateFormatwithTime = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	
	public static final DateFormat DAY_OF_WK_FORMATTER = new SimpleDateFormat("EEE");

	/* @return get absolute difference between d1/d2 in days, rounded to nearest */
	public static int getDiffInDays(Date d1, Date d2) {
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (float) DAY)));
	}
	
	public static String getCurrentDate() {		
		return dateFormat.format(new Date());
	}
	
	/** @return the calendar passed in, for convenience */
	public static Calendar truncate(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public static String getNextDate() {
		Calendar baseDate = truncate(Calendar.getInstance());					
		baseDate.add(Calendar.DATE, 1);
		return dateFormat.format(baseDate.getTime());
	}
	
	public static Date getDate(String dateString) throws ParseException {		
        return (Date)dateFormat.parse(dateString);
	}
	
	public static String getDate(Date dateVal) throws ParseException {		
        return dateFormat.format(dateVal);
	}
	
	public static String getServerTime(Date clientDate) throws ParseException {
		if(clientDate != null) {
			return serverTimeFormat.format(clientDate);
		} else {
			return null;
		}
	}
	
	public static String getDatewithTime(Date clientDate) throws ParseException {       
        return dateFormatwithTime.format(clientDate);
	}
	public static Date getDatewithTime(String clientDate) throws ParseException {       
        return dateFormatwithTime.parse(clientDate);
	}
	
	public static int getDiffInHours(Date d1, Date d2) {		
		return Math.abs((int) Math.round(((d1.getTime() - d2.getTime()) / (double) HOUR)));
	}
	
	public static double getDiffInHoursMinutes(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		double diffInHours = diff / ((double) 1000 * 60 * 60);
		int hour = (int) diffInHours;		
		double minutes = ((diffInHours - (int) diffInHours) * 60) / 100;
		double hourMinute = hour + minutes;
		return hourMinute > 0 ? (double) Math.round(hourMinute * 100) / 100 : 0.0;
	}
	
	public static String getPreviousDate(Date baseDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);		
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	
	public static Date getActualDate(Date d1, Date d2) {		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		c1.add(Calendar.DATE, -1);
		c1.getTime();
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		c2.set(Calendar.DATE, c1.get(Calendar.DATE));
		c2.set(Calendar.MONTH, c1.get(Calendar.MONTH));
		c2.set(Calendar.YEAR, c1.get(Calendar.YEAR));
		return c2.getTime();
	}
	
	public static String getShift(Date clientDate) throws ParseException {
		if(clientDate != null) {
			return serverTimeFormat.format(clientDate).substring(6);
		} else {
			return null;
		}
	}
	
	public static Date getMaxSnapshot(Date baseDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.AM_PM, Calendar.PM);
		return cal.getTime();
	}
	
	public static Date getNormalDate(Date baseDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);		
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 1970);
		return cal.getTime();
	}
	
	public static String formatDayOfWk(Date dateValue) {
		return DAY_OF_WK_FORMATTER.format(dateValue);
	}

} 