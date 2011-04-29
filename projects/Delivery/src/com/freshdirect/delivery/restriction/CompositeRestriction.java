package com.freshdirect.delivery.restriction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;

public class CompositeRestriction extends AbstractRestriction {
	private final DateRange dateRange;
	private Map<Integer, List<TimeOfDayRange>> timeRangeMap;
	private final EnumDlvRestrictionType type;
	
	public CompositeRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,
		DateRange dateRange,
		EnumDlvRestrictionType type,
		String path) {
		super(id, criterion, reason, name, message,path);
		this.dateRange = dateRange;
		//this.timeRangeMap = timeRangeMap;
		this.type = type;
	}

	public boolean contains(Date date) {
		boolean eval = false;
		if(EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(type)){
			//Check for date range.not contained. return. no further evaluation required.
			if(!dateRange.contains(date)){
				return false;
			} else {
				eval = true;
			}
		}
		//At this point either it is a RRN or OTR with date contained with in date range.
		//Now check for day of week and time ranges.
		if(timeRangeMap == null || timeRangeMap.isEmpty())
			return eval;// There is nothing to evaluate further.
		//reset eval
		eval = false;
		//Check for time range fall within available time ranges.
		Calendar c = Calendar.getInstance();
		c.setTime(date); 
		Set<Integer> dayOfWeeks = timeRangeMap.keySet();
outer:	for(Iterator<Integer> outer = dayOfWeeks.iterator(); outer.hasNext();){
			int dayOfWeek = outer.next();
			if (dayOfWeek == 0 || dayOfWeek == c.get(Calendar.DAY_OF_WEEK)) {
				List<TimeOfDayRange> timeRanges = timeRangeMap.get(dayOfWeek);
				Iterator<TimeOfDayRange> inner = timeRanges.iterator();
				while(inner.hasNext()){
					TimeOfDayRange timeRange = inner.next();
					if(timeRange.contains(new TimeOfDay(date))){
						eval = true;
						break outer;
					}
				}
			}
		}
		return eval;
	}


	public Map<Integer, List<TimeOfDayRange>>  getTimeRangeMap() {
		return timeRangeMap;
	}

	public String getDisplayDate() {
		return this.dateRange+ " " + (timeRangeMap != null ? timeRangeMap.toString() : ""); 
	}

	/**
	 * @return List of DateRange
	 */
	private List<DateRange> toDateRanges(DateRange range, int dayOfWeek) {
		List<DateRange> l = new ArrayList<DateRange>();
		Calendar endCal = DateUtil.truncate(DateUtil.toCalendar(range.getEndDate()));
		Calendar c = DateUtil.truncate(DateUtil.toCalendar(range.getStartDate()));
		while (!c.after(endCal)) {
			if (dayOfWeek == 0 || dayOfWeek == c.get(Calendar.DAY_OF_WEEK)) {
				List<TimeOfDayRange> timeRanges = timeRangeMap.get(dayOfWeek);
				Iterator<TimeOfDayRange> it = timeRanges.iterator();
				while(it.hasNext()){
					TimeOfDayRange timeRange = it.next();
					l.add(timeRange.toDateRange(c.getTime()));
				}
			}
			c.add(Calendar.DATE, 1);
		}
		return l;
	}

	public boolean overlaps(DateRange range) {
		boolean eval = false;
		if(EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(type)){
			//Check for date range.not overllaping. return. no further evaluation required.
			if(!dateRange.overlaps(range)){
				return false;
			} else {
				eval = true;
			}
		}
		//At this point either it is a RRN or OTR with date contained with in date range.
		//Now check for day of week and time ranges.
		if(timeRangeMap == null || timeRangeMap.isEmpty())
			return eval;// There is nothing to evaluate further.
		//reset eval
		eval = false;
		
		Set<Integer> dayOfWeeks = timeRangeMap.keySet();
outer:	for(Iterator<Integer> outer = dayOfWeeks.iterator(); outer.hasNext();){
			int dayOfWeek = outer.next();
			List<DateRange> dateRanges = toDateRanges(range, dayOfWeek);
			for (Iterator<DateRange> i = dateRanges.iterator(); i.hasNext();) {
				DateRange r = (DateRange) i.next();
				if (r.overlaps(range)) {
					eval = true;
					break outer;
				}
			}
		}
		return eval;
	}

	public boolean contains(DateRange range) {
		boolean eval = false;
		if(EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(type)){
			//Check for date range.not contained. return. no further evaluation required.
			if(!dateRange.contains(range)){
				return false;
			} else {
				eval = true;
			}
		}
		//At this point either it is a RRN or OTR with date contained with in date range.
		//Now check for day of week and time ranges.
		if(timeRangeMap == null || timeRangeMap.isEmpty())
			return eval;// There is nothing to evaluate further.
		//reset eval
		eval = false;
		
		Set<Integer> dayOfWeeks = timeRangeMap.keySet();
outer:	for(Iterator<Integer> outer = dayOfWeeks.iterator(); outer.hasNext();){
			int dayOfWeek = outer.next();
			List<DateRange> dateRanges = toDateRanges(range, dayOfWeek);
			for (Iterator<DateRange> i = dateRanges.iterator(); i.hasNext();) {
				DateRange r = (DateRange) i.next();
				if (r.contains(range)) {
					eval = true;
					break outer;
				}
			}
		}
		return eval;
	}

	public EnumDlvRestrictionType getType() {
		return type;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

	public void setTimeRangeMap(Map<Integer, List<TimeOfDayRange>> timeRangeMap) {
		this.timeRangeMap = timeRangeMap;
	}

}