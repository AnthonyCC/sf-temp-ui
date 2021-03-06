package com.freshdirect.transadmin.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.framework.util.DateUtil;

public class TransStringUtil {
	
	
	public static DateFormat sqlserverdateFormat = new SimpleDateFormat("yyyy/MM/dd");

	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static DateFormat serverDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public static DateFormat dayFormat = new SimpleDateFormat("EEEE");
	
	public static DateFormat serverDayFormat = new SimpleDateFormat("EEE");
	
	public static DateFormat timeFormat = new SimpleDateFormat("kk:mm:ss");
	
	public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
	
	public static DateFormat hourInDayFormat = new SimpleDateFormat("H:mm");
	
	public static DateFormat hourInDayFormat1 = new SimpleDateFormat("H.mm");
	
	public static DateFormat dateFormatwithTime = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
	
	public static DateFormat dateFormatwithTime1 = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
	
	public static DateFormat dateFormatwithTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		
	public static Calendar clientCalendar = Calendar.getInstance();
	
	private static String[] daysList = new String[] {"Monday","Tuesday",
														"Wednesday",
														"Thurdsay",
														"Friday",
														"Saturday",
														"Sunday"};
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
	
	public static String MASTER_WEEKOF = "01/01/1900";
	public static String MASTER_DATE = "01/01/1970";
	
	private static final String DEFAULT_SEPARATOR = ",";

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
	
	public static String getSqlServerDate(String clientDate) {
		try{
        return sqlserverdateFormat.format((Date)serverDateFormat.parse(clientDate));
		}catch(ParseException pe){
			pe.printStackTrace();
		}
		return "";
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
	public static Date getDateWithSeconds(String clientDate) throws ParseException {       
        return dateFormatwithTime2.parse(clientDate);
	}
	public static boolean checkHourOfDate(Date clientDate) throws ParseException {       
        boolean isGreater=false;        
        Calendar cal=Calendar.getInstance();
		cal.setTime(clientDate);
		if(cal.get(Calendar.HOUR_OF_DAY)>=21){
			isGreater=true;
		}
        return isGreater;
	}
	
	public static double getHourOfDate(Date clientDate) throws ParseException {       
          
        Calendar cal=Calendar.getInstance();
		cal.setTime(clientDate);
		int h=cal.get(Calendar.HOUR);
		double m=cal.get(Calendar.MINUTE)/60;
		double finalVal = h+m;	
        return finalVal;
	}
	
	public static Date getDate(String dateString) throws ParseException {		
        return (Date)dateFormat.parse(dateString);
	}
	
	public static String getDate(Date dateVal) throws ParseException {		
        return dateFormat.format(dateVal);
	}
	
	public static String getFullMonthDate(Date dateVal) throws ParseException {		
        return fullMonthFormat.format(dateVal);
	}
	
	public static String getTime(Date dateVal) throws ParseException {		
        return (String)timeFormat.format(dateVal);
	}
	
	public static Date getTime(String dateString) throws ParseException {		
        return (Date)timeFormat.parse(dateString);
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
	
	public static String formatTime(Date dateVal) {
		try {
			String strTime1 = hourInDayFormat.format(dateVal);
			return strTime1;
		} catch (Exception e) {
			// Do Nothing
		}
        return ">>";
	}
	
	public static Date formatTimeFromString(String dateVal) {
		try {
			Date strTime1 = hourInDayFormat1.parse(dateVal);
			return strTime1;
		} catch (Exception e) {
			// Do Nothing
		}
        return null;
	}
	public static String formatTimeFromDate(Date dateVal) {
		try {
			String strTime1 = hourInDayFormat1.format(dateVal);
			return strTime1;
		} catch (Exception e) {
			// Do Nothing
		}
        return null;
	}
	public static String formatTime1(Date dateVal) {
		try {
			String strTime1 = hourInDayFormat1.format(dateVal);
			return strTime1;
		} catch (Exception e) {
			// Do Nothing
		}
        return "0";
	}	
	public static String formatTime2(Date dateVal) {
		try {
			String strTime1 = serverTimeFormat.format(dateVal);
			return strTime1;
		} catch (Exception e) {
			// Do Nothing
		}
        return ">>";
	}	
	
	public static String calcHMS(int timeInSeconds) {
	      int hours, minutes, seconds;
	      hours = timeInSeconds / 3600;
	      timeInSeconds = timeInSeconds - (hours * 3600);
	      minutes = timeInSeconds / 60;
	      timeInSeconds = timeInSeconds - (minutes * 60);
	      seconds = timeInSeconds;
	      return formatTwoDigitNumber(hours) + ":" + formatTwoDigitNumber(minutes) + ":" + formatTwoDigitNumber(seconds) ;
	  }

	
	public static boolean compareDate(Date dateVal1, Date dateVal2) {
		try {
			String strDate1 = dateFormat.format(dateVal1);
			String strDate2 = dateFormat.format(dateVal2);
			if(strDate1.equals(strDate2))
				return true;
			
		} catch (Exception e) {
			// Do Nothing
		}
        return false;
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
		return Integer.parseInt(intVal);
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
	
	public static String formatDateSearch(String search) throws DateFilterException {  
		
        try {		
	        Date dateToCompare = null;
	        Date dateToCompare2 = null;
	        
			search = search.toLowerCase().trim();
	            
			String[] result = search.split(IDateContants.DELIM);
	
	        String operator = result[0];
	        
	        if(search == null || search.trim().length() ==0) {
	        	return null;
	        }
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
	        	dateToCompare = IDateContants.FILTER_DATEFORMAT.parse(search);
	        	
	            return IDateContants.EQUAL + "'"+serverDateFormat.format(dateToCompare)+"'";	        	
	        }
        } catch (Exception e) {
        	throw new DateFilterException();
        }
	}
	
	public static String splitStringForCode(String search) {
		String[] dataLst = StringUtils.split(search, "-");
		if(dataLst != null && dataLst.length >0) {
			return dataLst[0];
		} else {
			return "000";
		}
	}
	
	public static int splitStringForValue(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return Integer.parseInt(dataLst[1]);
			} 
		} catch(Exception e) {
			//do nothing
		}
		return 0;
	}
	
	public static String[] splitStringForValue1(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return dataLst;
			} 
		} catch(Exception e) {
			//do nothing
		}
		return null;
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
	
	public static boolean isValidZipCode(String zip) {	    
	    return zip.matches(zipCodePattern);    
	}
	
	public static boolean isValidDecimalFormat(String zip) {
		if(zip != null && zip.indexOf(".") ==-1) {
			zip = zip+".00";
		}
	    return zip.matches(bigDecimalPattern);    
	}
	
	public static boolean isValidDecimalFormat(String zip, String format) {
		if(zip != null && zip.indexOf(".") ==-1) {
			zip = zip+".00";
		}
	    return zip.matches(format);    
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
	
	public static String getZoneNumber(String routeId) {
		
		String result = null;
		if(routeId != null) {
			try {
				result = routeId.substring(1,4);
			} catch(Exception e) {
				//Do Nothing
			}
		}
	    return result;    
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
	
	public static boolean isToday(String date) throws ParseException
	{
		return isToday(TransStringUtil.getDate(date));		
	}
	public static boolean isToday(Date date) throws ParseException
	{
		clientCalendar.setTimeInMillis(System.currentTimeMillis());
		clientCalendar.set(Calendar.HOUR,0);
		clientCalendar.set(Calendar.MINUTE, 0);
		clientCalendar.set(Calendar.SECOND, 0);
		clientCalendar.set(Calendar.MILLISECOND, 0);
		clientCalendar.set(Calendar.AM_PM, Calendar.AM);
		long todayStartTime=clientCalendar.getTimeInMillis();
		clientCalendar.add(Calendar.DATE, 1);
		clientCalendar.add(Calendar.MILLISECOND, -1);
		long todayEndTime=clientCalendar.getTimeInMillis();
		
		long toCalculate=date.getTime();
		
		if(todayStartTime<=toCalculate&&toCalculate<=todayEndTime) return true;
		
		return false;
	}
	
	public static int getDayOfWeek(Date _date) {
		clientCalendar.setTime(_date);
		return clientCalendar.get( Calendar.DAY_OF_WEEK ) ;        
	}
	
	public static Date getAdjustedWeekOf(Date _date, int days) {
		clientCalendar.setTime(_date);
		int adjust = Calendar.MONDAY - clientCalendar.get( Calendar.DAY_OF_WEEK ) ;
        if (adjust > 0)  adjust -= 7 ;
        clientCalendar.add(Calendar.DATE, adjust) ;
        clientCalendar.add(Calendar.DATE, days) ;
        return clientCalendar.getTime();
	}
	
	public static Date getAdjustedDayOf(Date _date, int _days) {
		clientCalendar.setTime(_date);
		clientCalendar.add(Calendar.DATE, _days) ;
		return clientCalendar.getTime();
	}

	public static Date getAdjustedHourOf(Date _date, int _hours) {
		clientCalendar.setTime(_date);
		clientCalendar.add(Calendar.HOUR, _hours) ;
		return clientCalendar.getTime();
	}

	public static Date getWeekOf(Date _date) {
		clientCalendar.setTime(_date);
		int adjust = Calendar.MONDAY - clientCalendar.get( Calendar.DAY_OF_WEEK ) ;
        if (adjust > 0)  adjust -= 7 ;
        clientCalendar.add(Calendar.DATE, adjust) ;
        return clientCalendar.getTime();
	}
	
	public static Date getWeekOf(String _date) throws ParseException {
		clientCalendar.setTime(getServerDateString(_date));
		int adjust = Calendar.MONDAY - clientCalendar.get( Calendar.DAY_OF_WEEK ) ;
        if (adjust > 0) {
        	adjust = adjust - 7 ;
        }
        clientCalendar.add(Calendar.DATE, adjust) ;
        return clientCalendar.getTime();
	}
	
	public static Date getCurrentWeekOf() {
		Calendar baseDate = DateUtil.truncate(Calendar.getInstance());						
		return getWeekOf(baseDate.getTime());
	}
	
	public static Date getMasterWeekOf() throws ParseException {							
		return getWeekOf(getServerDate(MASTER_WEEKOF));
	}

	public static Date getNormalDate() throws ParseException {							
		return getDate(MASTER_DATE);
	}

	public static void main(String args[]) throws Exception {
		/*Calendar cal = Calendar.getInstance();
		Date _tmpDate = getDate("05/09/2010");
		cal.setTime(_tmpDate);
		
		int adjust = Calendar.MONDAY - cal.get( Calendar.DAY_OF_WEEK ) ;
		if (adjust > 0) {
        	adjust = adjust - 7 ;
        }
        cal.add(Calendar.DATE, adjust) ;
        cal.add(Calendar.DATE, 7) ;
        System.out.println(getDate(cal.getTime()));*/
		
		GregorianCalendar delWindCal = new GregorianCalendar();
		delWindCal.add(Calendar.DAY_OF_YEAR, 7);
		java.util.Date deliveryWindow = delWindCal.getTime();
		System.out.println("deliveryWindow >>"+deliveryWindow);

	}
	
	public static String[] getDates(String date, String day) throws Exception {
		
		Date d = TransStringUtil.getDate(date);
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		if ("All".equalsIgnoreCase(day)) {
			String[] dates = new String[7];
			for (int i = 2; i <= 8; i++) {
				c.set(Calendar.DAY_OF_WEEK, i);
				String ds = TransStringUtil.getServerDate1(c.getTime());
				dates[i - 2] = ds;
			}
			return dates;
		} else {
			if (day == null) {
				return new String[] { TransStringUtil.getServerDate1(c
						.getTime()) };
			} else {
				try {
					int k = Integer.parseInt(day);
					{ c.set(Calendar.DAY_OF_WEEK , k); }
					String ds = TransStringUtil.getServerDate1(c.getTime());
					return new String[] { ds };
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static String[] getDatesEx(String date, String day) throws Exception {

		Date d = TransStringUtil.getDate(date);
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		if ("All".equalsIgnoreCase(day)) {
			String[] dates = new String[7];
			for (int i = 2; i <= 8; i++) {
				c.set(Calendar.DAY_OF_WEEK, i);

				String ds = TransStringUtil.getServerDate(c.getTime());
				dates[i - 2] = ds;
			}
			return dates;
		} else {
			if (day == null) {
				return new String[] { TransStringUtil
						.getServerDate(c.getTime()) };
			} else {
				try {
					int k = Integer.parseInt(day);
					// if(k<8)
					{
						c.set(Calendar.DAY_OF_WEEK, k);
					}

					String ds = TransStringUtil.getServerDate(c.getTime());
					return new String[] { ds };
				} catch (Exception e) {

				}
			}
		}
		return null;

	}
	
	public static Date getCurrentTimeWithDate(String date){
		Calendar currentTime = Calendar.getInstance();
		
		Calendar requestedDate = Calendar.getInstance();
		try {
			requestedDate.setTime(TransStringUtil.getDate(date));
		
			requestedDate.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
			requestedDate.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));
			requestedDate.set(Calendar.SECOND, currentTime.get(Calendar.SECOND));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return requestedDate.getTime();
	}
	
	public static String[] decodeStrings(String stringVal) {	
		return StringUtils.split(stringVal, DEFAULT_SEPARATOR);
	}

	public static String encodeString(String strArray[]) {
		return StringUtils.join(strArray, DEFAULT_SEPARATOR);
	}

	public static String encodeString(List<? extends Object> list) {
				
		if (list != null && list.size() > 0) {
			return StringUtils.join(list.toArray(), DEFAULT_SEPARATOR);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getSequence(String str){
		List result = new ArrayList();
		if(str == "" || (str != null && str.length() == 0)){
			return null;
		}
		
		int k = 0;
		String[] strArray  = TransStringUtil.decodeStrings(str);
		String[] tempArray = null;
		for(int i = 0;i < strArray.length; i++){			
			if(strArray[i] != null && strArray[i] != "") {
				tempArray = StringUtils.split(strArray[i], "-");
			}
			
			if(tempArray != null && StringUtils.isNumeric(tempArray[0])) {
				if(tempArray.length > 1){
					k = Integer.parseInt(tempArray[0]);
					while(k <= Integer.parseInt(tempArray[1])){
						result.add(k);
						k++;
					}
				} else {
					result.add(Integer.parseInt(tempArray[0]));
				}
			}
		}
		
		
		return result;
	}
	
	public static String join(List<String> list, String delim) {

	    StringBuilder sb = new StringBuilder();
	    String loopDelim = "";

	    for(String s : list) {
	        sb.append(loopDelim);
	        sb.append(s);     
	        loopDelim = delim;
	    }
	    return sb.toString();
	}
	
	public static String join(Set<String> list, String delim) {

	    StringBuilder sb = new StringBuilder();
	    String loopDelim = "";

	    for(String s : list) {
	        sb.append(loopDelim);
	        sb.append(s);     
	        loopDelim = delim;
	    }
	    return sb.toString();
	}
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		Collections.sort(list);
		return list;
	}
 }
