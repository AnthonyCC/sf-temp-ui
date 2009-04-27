/*
 * $Workfile: QuickDateFormat.java$
 *
 * $Date: 9/10/2001 6:02:11 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Relatively efficient and formatter for Dates with some commonly used patterns.
 * It is thread safe.
 *
 * @version $Revision: 4$
 * @author $Author: Viktor Szathmary$
 */
public class QuickDateFormat {

	/** Format as "yyyy-MM-dd" */
	public final static int FORMAT_DATE = 0;

	/** Format as "yyyyMMdd" */
	public final static int FORMAT_SHORT_DATE = 1;

	/** Format as "HH:mm:ss" */
	public final static int FORMAT_TIME = 2;

	/** Format as "HHmmss" */
	public final static int FORMAT_SHORT_TIME = 3;
	
	/** Format as "yyyy-MM-dd HH:mm:ss,SSS" */
	public final static int FORMAT_ISO = 4;

	/** Format as "yyyy-MM-dd HH:mm:ss.SSS" */
	public final static int FORMAT_ISO_2 = 5;

	public final static QuickDateFormat DATE_FORMATTER = new QuickDateFormat( FORMAT_DATE );
	public final static QuickDateFormat SHORT_DATE_FORMATTER = new QuickDateFormat( FORMAT_SHORT_DATE );
	public final static QuickDateFormat TIME_FORMATTER = new QuickDateFormat( FORMAT_TIME );
	public final static QuickDateFormat SHORT_TIME_FORMATTER = new QuickDateFormat( FORMAT_SHORT_TIME );
	public final static QuickDateFormat ISO_FORMATTER = new QuickDateFormat( FORMAT_ISO );
	public final static QuickDateFormat ISO_FORMATTER_2 = new QuickDateFormat( FORMAT_ISO_2 );
	
	private final int format;
	private final TimeZone timeZone;
	
	public QuickDateFormat(int format) {
		this( format, TimeZone.getDefault() );
	}

	public QuickDateFormat(int format, TimeZone timeZone) {
		switch (format) {
			case FORMAT_DATE:
			case FORMAT_SHORT_DATE:
			case FORMAT_TIME:
			case FORMAT_SHORT_TIME:
			case FORMAT_ISO:
			case FORMAT_ISO_2:
				this.format = format;
				break;
			default:
				throw new IllegalArgumentException("Illegal date-format code");
		}			
		this.timeZone = timeZone;
	}
	
	public String format(Date date) {
		Calendar cal = Calendar.getInstance( this.timeZone );
		cal.setTime(date);
		return this.format(cal);	
	}
	
	public String format(Calendar cal) {

		StringBuffer buf = new StringBuffer();
		switch (this.format) {
			case FORMAT_DATE:
				appendDate(true, buf, cal);
				break;

			case FORMAT_SHORT_DATE:
				appendDate(false, buf, cal);
				break;
			
			case FORMAT_TIME:
				appendTime(true, buf, cal);
				break;

			case FORMAT_SHORT_TIME:
				appendTime(false, buf, cal);
				break;

			case FORMAT_ISO:
				appendDate(true, buf, cal);
				buf.append(' ');
				appendTime(true, buf, cal);
				buf.append(',');
				int millis = cal.get(Calendar.MILLISECOND);
				if (millis < 100) {
					buf.append('0');
				}
				if (millis < 10) {
					buf.append('0');
				}
				buf.append(millis);

				break;	

			case FORMAT_ISO_2:
				appendDate(true, buf, cal);
				buf.append(' ');
				appendTime(true, buf, cal);
				buf.append('.');
				int millis2 = cal.get(Calendar.MILLISECOND);
				if (millis2 < 100) {
					buf.append('0');
				}
				if (millis2 < 10) {
					buf.append('0');
				}
				buf.append(millis2);

				break;	
		}
		return buf.toString();
	}
	
	private static void appendDate(boolean separator, StringBuffer buf, Calendar cal) {
		buf.append(cal.get(Calendar.YEAR));

		if (separator) {
			buf.append('-');
		}
		switch( cal.get(Calendar.MONTH) ) {
			case Calendar.JANUARY:	buf.append("01"); break;      
			case Calendar.FEBRUARY:	buf.append("02"); break;     
			case Calendar.MARCH:	buf.append("03"); break;      
			case Calendar.APRIL:	buf.append("04"); break;     
			case Calendar.MAY:		buf.append("05"); break;      
			case Calendar.JUNE:		buf.append("06"); break;     
			case Calendar.JULY:		buf.append("07"); break;      
			case Calendar.AUGUST:	buf.append("08"); break;     
			case Calendar.SEPTEMBER:buf.append("09"); break;      
			case Calendar.OCTOBER:	buf.append("10"); break;      
			case Calendar.NOVEMBER:	buf.append("11"); break;           
			case Calendar.DECEMBER:	buf.append("12"); break;
			default:				buf.append("NA"); break;
	    }
		if (separator) {
			buf.append('-');
		}

		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day<10) {
			buf.append('0');
		}
		buf.append(day);
	}


	private static void appendTime(boolean separator, StringBuffer buf, Calendar cal) {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour<10) {
			buf.append('0');
		}
		buf.append(hour);
		if (separator) {
			buf.append(':');
		}
		
		int mins = cal.get(Calendar.MINUTE);
		if (mins<10) {
			buf.append('0');
		}
		buf.append(mins);
		if (separator) {
			buf.append(':');
		}
		
		int secs = cal.get(Calendar.SECOND);
		if (secs<10) {
			buf.append('0');
		}
		buf.append(secs);
	}

}
