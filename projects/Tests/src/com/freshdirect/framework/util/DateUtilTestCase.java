package com.freshdirect.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class DateUtilTestCase extends TestCase {

	public DateUtilTestCase(String name) {
		super(name);
	}

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public void testTruncate() throws ParseException {
		assertEquals(
			DateUtil.toCalendar(DF.parse("2003-10-10 00:00:00.0")),
			DateUtil.truncate(DateUtil.toCalendar(DF.parse("2003-10-10 12:23:30.5"))));

		assertEquals(
			DateUtil.toCalendar(DF.parse("2003-10-10 00:00:00.0")),
			DateUtil.truncate(DateUtil.toCalendar(DF.parse("2003-10-10 00:00:00.0"))));
	}

	public void testGetDiffInDays() throws ParseException {
		Date d = DF.parse("2004-03-01 00:00:00.0");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, 7);
		Date d2 = cal.getTime();

		assertEquals(0, DateUtil.getDiffInDays(d, d));
		assertEquals(7, DateUtil.getDiffInDays(d, d2));
		assertEquals(7, DateUtil.getDiffInDays(d2, d));
	}

	public void testGetDiffInDaysWithDaylightSavings() throws ParseException {
		Date d = DF.parse("2004-04-01 00:00:00.0");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, 7);
		Date d2 = cal.getTime();

		assertEquals(0, DateUtil.getDiffInDays(d, d));
		assertEquals(7, DateUtil.getDiffInDays(d, d2));
		assertEquals(7, DateUtil.getDiffInDays(d2, d));
	}

	public void testIsSameDay() throws ParseException {
		assertTrue(DateUtil.isSameDay(DF.parse("2003-10-10 00:00:00.0"), DF.parse("2003-10-10 00:00:00.0")));
		assertTrue(DateUtil.isSameDay(DF.parse("2003-10-10 23:59:59.0"), DF.parse("2003-10-10 00:00:00.0")));
		assertFalse(DateUtil.isSameDay(DF.parse("2003-10-11 00:00:00.0"), DF.parse("2003-10-10 00:00:00.0")));
		assertFalse(DateUtil.isSameDay(DF.parse("2003-10-09 23:59:59.0"), DF.parse("2003-10-10 00:00:00.0")));
	}
	
	public void testRelativeTimeDifference() throws ParseException {
	    String oneSecond = 
	        DateUtil.relativeDifferenceAsString(DF.parse("2007-04-16 10:15:02.0"), DF.parse("2007-04-16 10:15:01.0"));
		
		String nineSeconds = 
		        DateUtil.relativeDifferenceAsString(DF.parse("2007-04-16 10:15:10.3"), DF.parse("2007-04-16 10:15:01.2"));
		
		String oneHour = 
		    	DateUtil.relativeDifferenceAsString(DF.parse("2007-04-16 12:15:10.3"), DF.parse("2007-04-16 11:11:23.2"));
		
		String seventeenHours = 
				DateUtil.relativeDifferenceAsString(DF.parse("2007-04-16 23:15:10.3"), DF.parse("2007-04-16 06:10:47.8"));
		
		String oneDay =
		        DateUtil.relativeDifferenceAsString(DF.parse("2007-04-17 10:15:12.0"), DF.parse("2007-04-16 10:15:03.0"));
		
		String threeDays =
			    DateUtil.relativeDifferenceAsString(DF.parse("2007-04-19 10:20:32.0"), DF.parse("2007-04-16 07:23:12.6"));
		
		String minusThreeDays =
		    DateUtil.relativeDifferenceAsString( DF.parse("2007-04-16 07:23:12.6"), DF.parse("2007-04-19 10:20:32.0"));
		
		System.out.println(oneSecond);
		System.out.println(nineSeconds);	
		System.out.println(oneHour);	
		System.out.println(seventeenHours);
		System.out.println(oneDay);
		System.out.println(threeDays);
		System.out.println(minusThreeDays);
		
		
		assertTrue(oneSecond.equals("1 second ago"));
		assertTrue(nineSeconds.equals("9 seconds ago"));
		assertTrue(oneHour.equals("1 hour ago"));
		assertTrue(seventeenHours.equals("17 hours ago"));
		assertTrue(oneDay.equals("1 day ago"));
		assertTrue(threeDays.equals("3 days ago"));
		assertTrue(minusThreeDays.equals("3 days from now"));
			
	}

}
