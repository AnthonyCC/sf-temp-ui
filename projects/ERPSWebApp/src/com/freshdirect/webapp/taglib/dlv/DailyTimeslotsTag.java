/*
 * DailyTimeslotTag.java
 *
 * Created on November 10, 2001, 6:59 PM
 */

package com.freshdirect.webapp.taglib.dlv;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.servlet.jsp.*;

import java.util.*;

import com.freshdirect.delivery.model.*;
import com.freshdirect.framework.util.QuickDateFormat;

public class DailyTimeslotsTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	private String id;
	private GregorianCalendar day;
	private List timeslots;
	
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public GregorianCalendar getDay(){
		return this.day;
	}
	public void setDay(GregorianCalendar day){
		this.day = day;
	}
	
	public List getTimeslots(){
		return this.timeslots;
	}
	public void setTimeslots(List timeslots){
		this.timeslots = timeslots;
	}
	
	public int doStartTag() throws JspException {
		Map ret = new TreeMap(new CalendarComparator());
		Calendar endCal = new GregorianCalendar();
		endCal.setTime(day.getTime());
		endCal.add(Calendar.DATE, 1);
		endCal.set(Calendar.HOUR, 12);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.AM_PM, Calendar.AM);
		day.set(Calendar.HOUR, 6);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.AM_PM, Calendar.AM);
		for(Iterator i = timeslots.iterator(); i.hasNext();){
			DlvTimeslotModel timeslot = (DlvTimeslotModel)i.next();
			String startDate = QuickDateFormat.SHORT_DATE_FORMATTER.format(timeslot.getBaseDate());
			String curDay = QuickDateFormat.SHORT_DATE_FORMATTER.format(day.getTime());
			/*if(timeslot.getBegDateTime().after(day.getTime()) && timeslot.getEndDateTime().before(endCal.getTime())){
				ret.put(timeslot.getBegDateTime(), timeslot);
			}*/
			if(startDate.equals(curDay)){
				ret.put(timeslot.getBaseDate(), timeslot);
			}
			
		}
		pageContext.setAttribute(id, ret);
		return EVAL_BODY_BUFFERED;
	}
	
	private class CalendarComparator implements Comparator{
		public int compare(Object obj1, Object obj2){
			Date cal1 = (Date)obj1;
			Date cal2 = (Date)obj2;
			if(cal1.before(cal2)){
				return -1;
			}
			if(cal1.after(cal2)){
				return 1;
			}
			
			return 0;
		}
		
		public boolean equals(Object obj){
			return this.equals(obj);
		}
	}
	
}