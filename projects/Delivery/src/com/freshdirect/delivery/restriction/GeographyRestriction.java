package com.freshdirect.delivery.restriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DateRange;

public class GeographyRestriction extends ModelSupport   {
	
	private String name;  
	private String isActive;	  
	private String comments;
	private String message;
	private Map restrictedDays;
	
	private DateRange range;
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map getRestrictedDays() {
		return restrictedDays;
	}
	public void setRestrictedDays(Map restrictedDays) {
		this.restrictedDays = restrictedDays;
	}
	
	public void setRestrictedDay(int dayOfWeek, GeographyRestrictedDay restrictedDay) {
		Integer objDayofWeek = new Integer(dayOfWeek);
		if(restrictedDays == null) {
			restrictedDays = new HashMap();
		}
		if(restrictedDays.get(objDayofWeek) == null) {
			restrictedDays.put(objDayofWeek, new ArrayList());
		}
		((List)restrictedDays.get(objDayofWeek)).add(restrictedDay);
	}
	
	public List getRestrictedDay(int dayOfWeek ) {
		if(restrictedDays != null) {
			return (List)restrictedDays.get(new Integer(dayOfWeek));
		}
		return null;
	}
	public DateRange getRange() {
		return range;
	}
	public void setRange(DateRange range) {
		this.range = range;
	}
	
	public void setRange(Date startDate, Date endDate) {
		this.range = new DateRange(startDate, endDate);
	}
	
	public boolean contains(Date date) {
		return !range.contains(date);
	}
	
	public String getDisplayDate() {
		return DateRangeFormat.format(range);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
}
