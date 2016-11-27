package com.freshdirect.mobileapi.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.framework.util.DateUtil;

public class StringUtil {
	
		public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		public static DateFormat serverDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
		public static DateFormat dayFormat = new SimpleDateFormat("EEEE");
		
		public static DateFormat serverDayFormat = new SimpleDateFormat("EEE");
		
		public static DateFormat timeFormat = new SimpleDateFormat("kk:mm:ss");
		
		public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
		
		public static DateFormat hourInDayFormat = new SimpleDateFormat("H:mm");
		
		public static DateFormat hourInDayFormat1 = new SimpleDateFormat("H.mm");
		
		public static DateFormat dateFormatwithTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		public static Calendar clientCalendar = Calendar.getInstance();
		
		private static String[] daysList = new String[] {"Monday","Tuesday","Wednesday","Thurdsay","Friday","Saturday","Sunday"};
		
		private static Map daysMap = new HashMap();
		
		private static String zipCodePattern = "\\d{5}(-\\d{4})?";
		
		private static String bigDecimalPattern = "\\d{0,8}\\.\\d{0,2}";
		
		private static NumberFormat formatter = new DecimalFormat("00");
		
		private static NumberFormat twoDigitNumber = new DecimalFormat("###.##");
		
		private static NumberFormat singleDigitNumber = new DecimalFormat("###.#");
		
		private static DateFormat fullMonthFormat = new SimpleDateFormat("dd MMMMM yyyy");
		
		public final static long SECOND = 1000;
		public final static long MINUTE = 60 * SECOND;
		public final static long HOUR = 60 * MINUTE;
		public final static long DAY = 24 * HOUR;
				
		static {
				for(int intCount = 0;intCount < daysList.length;intCount++) {
					daysMap.put(daysList[intCount], new Integer(intCount));
				}
		}
			
		static {
			dateFormat.setLenient(false);
			serverDateFormat.setLenient(false);
			dayFormat.setLenient(false);
		}
		public static Calendar toCalendar(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
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
			
			return str == null || str.length() == 0||"null".equalsIgnoreCase(str);
			
		}
			
		public static String getCurrentDate() {		
			return dateFormat.format(new Date());
		}
		
		public static String getCurrentTime() {		
			return serverTimeFormat.format(new Date());
		}
		
		public static String getDispatchCurrentDate()
		{
			clientCalendar.setTimeInMillis(System.currentTimeMillis());
			clientCalendar.set(Calendar.HOUR,3);
			clientCalendar.set(Calendar.MINUTE, 0);
			clientCalendar.set(Calendar.SECOND, 0);
			clientCalendar.set(Calendar.MILLISECOND, 0);
			clientCalendar.set(Calendar.AM_PM, Calendar.AM);
			long tomorrowat3=clientCalendar.getTimeInMillis();
			long currentTime=System.currentTimeMillis();
			if(currentTime<tomorrowat3)
			{
				clientCalendar.add(Calendar.DATE, -1);
				return dateFormat.format(clientCalendar.getTime());
			}
			else
			{
				return dateFormat.format(new Date());
			}
		}
		public static String getNextDate() {
			Calendar baseDate = DateUtil.truncate(Calendar.getInstance());					
			baseDate.add(Calendar.DATE, 1);
			return dateFormat.format(baseDate.getTime());
		}
		
		public static String getCurrentServerDate() {		
			return serverDateFormat.format(new Date());
		}
		
		public static String getServerDate(String clientDate) throws ParseException {       
	        return serverDateFormat.format((Date)dateFormat.parse(clientDate));
		}
		public static Date getServerDateString(String clientDate) throws ParseException {       
	        return serverDateFormat.parse(clientDate);
		}
		public static Date getServerDateString1(String clientDate) throws ParseException {       
	        return dateFormat.parse(clientDate);
		}
		public static String getServerTime(Date clientDate) throws ParseException {
			if(clientDate != null) {
				return serverTimeFormat.format(clientDate);
			} else {
				return null;
			}
		}
		
		public static String getServerDay(Date clientDate) throws ParseException {       
	        return serverDayFormat.format(clientDate);
		}
		
		public static Date getServerTime(String clientDate) throws ParseException {       
	        return (Date)serverTimeFormat.parse(clientDate);
		}
		
		public static String getDatewithTime(Date clientDate) throws ParseException {       
	        return dateFormatwithTime.format(clientDate);
		}
		public static Date getDatewithTime(String clientDate) throws ParseException {       
	        return dateFormatwithTime.parse(clientDate);
		}
		
		public static String getServerDate(Date dateVal) throws ParseException {		
	        return serverDateFormat.format(dateVal);
		}
		
		public static String getServerDate1(Date dateVal) throws ParseException {		
			return dateFormat.format(dateVal);
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
		
		public static Date addSeconds(Date date, int seconds) {		
			
			clientCalendar.setTime(date);
			clientCalendar.add(Calendar.SECOND, seconds);
			return clientCalendar.getTime();
		}
		
		public static int getInt(String intVal) throws NumberFormatException {		
			if(intVal != null && !"".equals(intVal)){
				return Integer.parseInt(intVal);
			}
			return 0;
		}
		
		public static Long getLong(String intVal) throws NumberFormatException {		
			return Long.parseLong(intVal);
		}
		
		public static String formatRouteNumber(String input) {
			return formatter.format(Integer.parseInt(input));
		}
		
		public static String formatTwoDigitNumber(double input) {
			return twoDigitNumber.format(input);
		}
		
		public static String formatSingleDigitNumber(double input) {
			return singleDigitNumber.format(input);
		}
		
		
		public static String formatTwoDigitNumber(int input) {
			return formatter.format(input);
		}
		
		public static String formatIntoHHMMSS(double inMinutes) {
			
			double secsIn = inMinutes * 60;
			int hours = (int)(secsIn / 3600);
			double remainder = secsIn % 3600;
			int minutes = (int)(remainder / 60);
			int seconds = (int)(remainder % 60);

			return ( (hours < 10 ? "0" : "") + hours
						+ ":" + (minutes < 10 ? "0" : "") + minutes
						+ ":" + (seconds< 10 ? "0" : "") + seconds );

		}
		
		public static String formatIntoHHMM(double inMinutes) {
			
			double secsIn = inMinutes * 60;
			int hours = (int)(secsIn / 3600);
			double remainder = secsIn % 3600;
			int minutes = (int)(remainder / 60);
			
			return ( (hours < 10 ? "0" : "") + hours
						+ ":" + (minutes < 10 ? "0" : "") + minutes);

		}
		
		public static boolean isValidInteger(String intVal) throws NumberFormatException {	
			try {
				Integer.parseInt(intVal);
			} catch(NumberFormatException exp) {
				return false;
			}
			return true;
		}
		
		public static boolean isValidDecimal(String intVal) throws NumberFormatException {	
			try {
				Double.parseDouble(intVal);
			} catch(NumberFormatException exp) {
				return false;
			}
			return true;
		}

}
