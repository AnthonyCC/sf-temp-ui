package com.freshdirect.athena.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeUtil {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2008-10-31
	
	public static long getTimestampMilliseconds(String input) {
		return getTimestamp(input).getTime();
	}
	
	public static Date getTimestamp(String input) {
		try {
			return DATETIME_FORMAT.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
	
	public static long getDateMilliseconds(String input) {
		return getDate(input).getTime();
	}
	
	public static Date getDate(String input) {
		try {
			return DATE_FORMAT.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
	
	public static Integer getInt(String input) {
		Integer result = new Integer(0);
		try {
			result =  Integer.parseInt(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static Double getDouble(String input) {
		Double result = new Double(0);
		try {
			result =  Double.parseDouble(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static Float getFloat(String input) {
		Float result = new Float(0);
		try {
			result =  Float.parseFloat(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
