package com.freshdirect.delivery.restriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

public class GeographyRestriction extends ModelSupport   {
	
	private String name;  
	private String active;	  
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
	
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
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
		List _restrictedDays = new ArrayList();
		Integer primaryKey = new Integer(dayOfWeek);
		Integer allKey = new Integer(0);
		if(restrictedDays != null) {
			if(restrictedDays.get(primaryKey) != null) {
				_restrictedDays.addAll((List)restrictedDays.get(primaryKey));
			}
			if(restrictedDays.get(allKey) != null) {
				_restrictedDays.addAll((List)restrictedDays.get(allKey));
			}
		}
		return _restrictedDays;
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
		return range.containsEx(date);
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
	
	public static boolean isTimeSlotGeoRestricted(List geographicRestrictions
			, FDTimeslot timeslot
			, List messages) {
		boolean isRestricted = false;
		if(geographicRestrictions != null && timeslot != null && messages != null) {
			Iterator _iterator = geographicRestrictions.iterator();
			while(_iterator.hasNext()) {
				GeographyRestriction geoRestriction = (GeographyRestriction)_iterator.next();
				if(isTimeSlotGeoRestricted(geoRestriction, timeslot, messages)) {
					isRestricted = true;
					break;
				}
			}
		}
		return isRestricted;
	}
	
	public static boolean isTimeSlotGeoRestricted(GeographyRestriction geographicRestrictions
												, FDTimeslot timeslot
												, List messages) {
		
		boolean isRestricted = false;
		if(geographicRestrictions != null) {
			List restrictedDays = geographicRestrictions.getRestrictedDay(timeslot.getDayOfWeek());
			if(restrictedDays != null) {
				Iterator _iterator = restrictedDays.iterator();
				while(_iterator.hasNext()) {
					GeographyRestrictedDay restrictedDay = (GeographyRestrictedDay)_iterator.next();
					if(restrictedDay != null) {
						try {							
						
							if(geographicRestrictions.contains(timeslot.getBaseDate())) {
								//System.out.println("Check Passed >"+DateUtil.format(timeslot.getBaseDate()));
								isRestricted = restrictedDay.isMatching(timeslot.getDlvTimeslot().getStartTime());
								if(isRestricted) {
									messages.add(geographicRestrictions.getMessage());
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return isRestricted;
	}

	
}
