package com.freshdirect.webapp.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.fdstore.standingorders.FDStandingOrder;

/**
 * Helper class for standing orders, any static method which has no place anywhere else should be here.
 * A lot of code duplicates functionality in DeliveryTimeWindowFormatter with some minor but important changes.  
 */
public class StandingOrderHelper {
	
	public static final String	SEPARATOR	= " - ";
	public static final String	NOON		= "noon";
	public static final String	AM			= "am";
	public static final String	PM			= "pm";
	
	/**
	 * Returns formatted delivery date (e.g. "Tuesday, 6 - 8am")
	 * @param alt use alternative format?
	 * @return next delivery date as a formatted string
	 */
	public static String getDeliveryDate( FDStandingOrder so, boolean alt ) {
		final String dayNames[] = new DateFormatSymbols().getWeekdays();

		Calendar cl = Calendar.getInstance();
		cl.setTime(so.getNextDeliveryDate());

		StringBuffer buf = new StringBuffer();
		
		if (alt) {
			// alternative format "6 - 8am, Tuesday"
			buf.append(formatTime(so.getStartTime(), so.getEndTime()));
			buf.append(", ");
			buf.append( dayNames[ cl.get(Calendar.DAY_OF_WEEK)] );
		} else {
			// normal format "Tuesday, 6 - 8am"
			buf.append( dayNames[ cl.get(Calendar.DAY_OF_WEEK)] );
			buf.append(", ");
			buf.append(formatTime(so.getStartTime(), so.getEndTime()));
		}
		
		return buf.toString();
	}
	
	public static String formatTime(Date s, Date e) {
		int sh = dateToCalendar(s).get(Calendar.HOUR_OF_DAY);
		int eh = dateToCalendar(e).get(Calendar.HOUR_OF_DAY);
		
		StringBuilder sb = new StringBuilder();
		
		if ( sh < 12 ) {
			sb.append( sh );
			sb.append( AM );
		} else if ( sh == 12 ) {
			sb.append( NOON );			
		} else {
			sb.append( sh-12 );
			sb.append( PM );
		}
		
		sb.append( SEPARATOR );
		
		if ( eh < 12 ) {
			sb.append( eh );
			sb.append( AM );
		} else if ( eh == 12 ) {
			sb.append( NOON );			
		} else {
			sb.append( eh-12 );
			sb.append( PM );
		}

		return sb.toString();
	}
	
	public static Calendar dateToCalendar(final Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	public static String getNextDeliveryDate(FDStandingOrder so) {
		if ( so == null || so.getNextDeliveryDate() == null ) 
			return "";
		return new SimpleDateFormat("EEEE, MMMM d.").format( so.getNextDeliveryDate() );
	}
}
