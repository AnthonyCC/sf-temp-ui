package com.freshdirect.transadmin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TransStringUtil {
	
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static DateFormat serverDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
	public static DateFormat dayFormat = new SimpleDateFormat("EEEE");
	
	private static Calendar clientCalendar = Calendar.getInstance();
	
	private static String[] daysList = new String[] {"Monday","Tuesday",
														"Wednesday",
														"Thurdsay",
														"Friday",
														"Saturday",
														"Sunday"};
		
	static {
		dateFormat.setLenient(false);
		serverDateFormat.setLenient(false);
		dayFormat.setLenient(false);
	}
	public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
	}
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static String getCurrentDate() {		
		return dateFormat.format(new Date());
	}
	
	public static String getServerDate(String clientDate) throws ParseException {       
        return serverDateFormat.format((Date)dateFormat.parse(clientDate));
	}
	
	public static Date getDate(String dateString) throws ParseException {		
        return (Date)dateFormat.parse(dateString);
	}
	
	public static String getDate(Date dateVal) throws ParseException {		
        return dateFormat.format(dateVal);
	}
	
	public static String getServerDate(Date dateVal) throws ParseException {		
        return serverDateFormat.format(dateVal);
	}
	
	public static String getDayofWeek(String clientDate) throws ParseException {	
        
        return dayFormat.format((Date)dateFormat.parse(clientDate));
	}
	
	public static String getDayofWeek(Date clientDate) throws ParseException {	
        
        return dayFormat.format(clientDate);
	}
	
	public static int getClientDayofWeek(String clientDate) throws ParseException {        
        clientCalendar.setTime(dateFormat.parse(clientDate));        
        return clientCalendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public static String[] getDays() {
		return daysList;
	}
	
	public static int getDayinWeek(String day) {
		return Arrays.binarySearch(daysList, day);
	}
	
	public static Date addDays(Date srcDate, int days) {
		clientCalendar.setTime(srcDate);
		clientCalendar.add(Calendar.DATE, days);
		return clientCalendar.getTime();
	}
}
