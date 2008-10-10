package com.freshdirect.webapp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

public class TimeslotPageUtil {
	
	public static final int MORNING_START = 1;		// 1:00 AM
	public static final int AFTERNOON_END = 15;		// 3:00 PM
	public static final int EVENING_END = 24;		// midnight
	
	private final static SimpleDateFormat dayNameFormatter = new SimpleDateFormat("EEEE");
	private final static SimpleDateFormat hourFormatter = new SimpleDateFormat("h");
	private final static SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
	private final static SimpleDateFormat monthDayFormatter = new java.text.SimpleDateFormat("MMM. d");
	private final static SimpleDateFormat mayMonthDayFormatter = new java.text.SimpleDateFormat("MMM d");

	public static List getBand(EnumDlvRestrictionReason reason, DlvRestrictionsList restrictions, Date startDate, Date endDate) {
		return aggregate(getDailyRestrictions(reason, restrictions, startDate, endDate));
	}

	protected static boolean[] getDailyRestrictions(
		EnumDlvRestrictionReason reason,
		DlvRestrictionsList restrictions,
		Date startDate,
		Date endDate) {

		Calendar baseCal = DateUtil.truncate(DateUtil.toCalendar(startDate));
		Calendar endCal = DateUtil.truncate(DateUtil.toCalendar(endDate));

		Calendar baseEndCal = DateUtil.truncate(DateUtil.toCalendar(startDate));

		int dayDiff = (int) Math.round((endDate.getTime() - startDate.getTime() + (24 * DateUtil.HOUR)) / (double)DateUtil.DAY);
		
		System.out.println("total dayDiff :"+dayDiff);
		
		boolean[] restDays = new boolean[dayDiff];

		int i = 0;
		while (baseCal.before(endCal) || baseCal.equals(endCal)) {
			if(dayDiff==7 && EnumDlvRestrictionReason.THANKSGIVING.equals(reason)){
			restDays[i]=true;
			}else{
			restDays[i] = restrictions.isRestricted(EnumDlvRestrictionCriterion.DELIVERY, reason, new DateRange(
				baseCal.getTime(),
				baseEndCal.getTime()));
			}
			i++;
			baseCal.add(Calendar.DATE, 1);
			baseEndCal.add(Calendar.DATE, 1);
		}
		return restDays;
	}

	public static class Span {
		private final int span;
		private final boolean value;

		public Span(int span, boolean value) {
			this.span = span;
			this.value = value;
		}

		public int getSpan() {
			return span;
		}

		public boolean isValue() {
			return value;
		}
		
		public String toString(){
			return "[Span: "+span+" value: "+value+"]";
		}
	}

	/** @return List of Span */
	protected static List aggregate(boolean[] arr) {
		List l = new ArrayList();
		int span = 1;
		for (int i = 0; i < arr.length; i++) {
			if (i + 1 == arr.length) {
				l.add(new Span(span, arr[i]));
			} else {
				if (arr[i] != arr[i + 1]) {
					l.add(new Span(span, arr[i]));
					span = 0;
				}
			}
			span++;
		}
		System.out.println(l);
		return l;
	}
	
	public static synchronized String formatDayName(Date day) {
		return dayNameFormatter.format(day);
	}

	public static synchronized String formatCutoffTime(Date day) {
		return hourFormatter.format(day);
	}

	public static String formatFirstOrderYear(Date day) {
		//this happens if the chefs table user has never ordered anything
		//which should never happen, by the current definition of the chefs table users
		if(day == null) {
			day = new Date();
		}
		return yearFormatter.format(day);
	}

	public static synchronized String formatDeliveryDate(Date day){
		Calendar cal = DateUtil.toCalendar(day);
		if ( cal.get(Calendar.MONTH) >= Calendar.MAY && cal.get(Calendar.MONTH) <= Calendar.JULY ) {
			return monthDayFormatter.format( cal.getTime() );
		}
		return mayMonthDayFormatter.format( cal.getTime() );
	}
}
