package com.freshdirect.transadmin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class TransStringUtil {
	
	//public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static DateFormat serverDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
	public static DateFormat dayFormat = new SimpleDateFormat("EEEE");
	
	public static DateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
	
	private static Calendar clientCalendar = Calendar.getInstance();
	
	private static String[] daysList = new String[] {"Monday","Tuesday",
														"Wednesday",
														"Thurdsay",
														"Friday",
														"Saturday",
														"Sunday"};
	private static Map daysMap = new HashMap();
	
	
	
	static {
			for(int intCount=0;intCount<daysList.length;intCount++) {
				daysMap.put(daysList[intCount], new Integer(intCount));
			}
	}
		
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
		return ((Integer)daysMap.get(day)).intValue();
	}
	
	public static boolean compareTime(String startTime, String endTime) throws NumberFormatException{
		//return ((Date)timeFormat.parse(endTime)).after((Date)timeFormat.parse(startTime));
		return getInt(startTime) > getInt(endTime);
	}
	
	public static Date addDays(Date srcDate, int days) {
		clientCalendar.setTime(srcDate);
		clientCalendar.add(Calendar.DATE, days);
		return clientCalendar.getTime();
	}
	
	public static int getInt(String intVal) throws NumberFormatException {		
		return Integer.parseInt(intVal);
	}
	
	public static String formatDateSearch(String search) throws DateFilterException {  
		
        try {		
	        Date dateToCompare = null;
	        Date dateToCompare2 = null;
	        
			search = search.toLowerCase().trim();
	            
			String[] result = search.split(IDateContants.DELIM);
	
	        String operator = result[0];
	        
	        if (operator.equals(IDateContants.EQUAL)) {
	            dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(result[1]);
	
	            return IDateContants.EQUAL + " '"+serverDateFormat.format(dateToCompare)+"'";
	        }
	        else if (operator.equals(IDateContants.LESS_THAN)) {
	            dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(result[1]);
	
	            return IDateContants.LESS_THAN + " '"+serverDateFormat.format(dateToCompare)+"'";
	        }
	        else if (operator.equals(IDateContants.GREATER_THAN)) {
	        	dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(result[1]);
	
	            return IDateContants.GREATER_THAN + " '"+serverDateFormat.format(dateToCompare)+"'";
	        }
	        else if (operator.equals(IDateContants.LESS_THAN_OR_EQUAL)) {
	        	dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(result[1]);
	
	            return IDateContants.LESS_THAN_OR_EQUAL + " '"+serverDateFormat.format(dateToCompare)+"'";
	        }
	        else if (operator.equals(IDateContants.GREATER_THAN_OR_EQUAL)) {
	        	dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(result[1]);
	
	            return IDateContants.GREATER_THAN_OR_EQUAL + " '"+serverDateFormat.format(dateToCompare)+"'";
	        }
	        else if (operator.equals(IDateContants.BETWEEN))  {
	            dateToCompare = dateFormat.parse(result[1]);
	            dateToCompare2 = dateFormat.parse(result[2]);
	
	            return IDateContants.SQL_BETWEEN+" '"+ serverDateFormat.format(dateToCompare)+"' "+ IDateContants.LOGICAL_AND
	            	+" '"+ serverDateFormat.format(dateToCompare2)+"'";
	        }
	        else if (operator.equals(IDateContants.NOT_EQUAL)) {
	        	dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(result[1]);
	
	            return IDateContants.NOT_EQUAL + "'"+serverDateFormat.format(dateToCompare)+"'";
	        }
	        else {
	            return null;
	        }
        } catch (Exception e) {
        	throw new DateFilterException();
        }
	}
	
	public static String formatStringSearch(String search) throws StringFilterException {
		String[] dataLst = StringUtils.split(search, ",");
		StringBuffer strBuf = new StringBuffer();
		boolean hasInfo = false;
		String strTmp = null;
		
		if(dataLst != null) {
			int length = dataLst.length;
			for(int intCount=0;intCount<length;intCount++) {
				strTmp = dataLst[intCount].trim();
				if(strTmp.length() > 0) {
					strBuf.append(((hasInfo) ? "," : "")).append("'").append(strTmp).append("'");
					hasInfo = true;					
				}
			}
		}
		return (hasInfo ? "IN ("+strBuf.toString()+")" : null);
	}
	
	public static class DateFilterException extends Exception {
		
		public DateFilterException() {
			super();
		}
	}
	
	public static class StringFilterException extends Exception {
		
		public StringFilterException() {
			super();
		}
	}
 }
