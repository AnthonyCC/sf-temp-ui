package com.freshdirect.transadmin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransStringUtil {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static DateFormat serverDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static DateFormat dayFormat = new SimpleDateFormat("EEEE");
	
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
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String getServerDate(String clientDate) throws ParseException {		
        Date date = (Date)dateFormat.parse(clientDate);
        return serverDateFormat.format(date);
	}
	
	public static Date getDate(String dateString) throws ParseException {		
        return (Date)dateFormat.parse(dateString);
	}
		
	
	public static String getDayofWeek(String clientDate) throws ParseException {		
        Date date = (Date)dateFormat.parse(clientDate);
        return dayFormat.format(date);
	}

}
