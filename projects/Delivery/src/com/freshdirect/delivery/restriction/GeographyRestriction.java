package com.freshdirect.delivery.restriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DateRange;

public class GeographyRestriction extends ModelSupport   {
	
	private static final long	serialVersionUID	= 1923999018513521720L;
	private final static Category LOGGER = Category.getInstance(GeographyRestriction.class);
	private String name;  
	private String active;	  
	private String comments;
	private String message;
	private String showMessage;
	private String serviceType;
	private Map<Integer,List<GeographyRestrictedDay>> restrictedDays;
	
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
	public Map<Integer,List<GeographyRestrictedDay>> getRestrictedDays() {
		return restrictedDays;
	}
	public void setRestrictedDays(Map<Integer,List<GeographyRestrictedDay>> restrictedDays) {
		this.restrictedDays = restrictedDays;
	}
	
	public void setRestrictedDay(int dayOfWeek, GeographyRestrictedDay restrictedDay) {
		Integer objDayofWeek = new Integer(dayOfWeek);
		if(restrictedDays == null) {
			restrictedDays = new HashMap<Integer,List<GeographyRestrictedDay>>();
		}
		if(restrictedDays.get(objDayofWeek) == null) {
			restrictedDays.put(objDayofWeek, new ArrayList<GeographyRestrictedDay>());
		}
		restrictedDays.get(objDayofWeek).add(restrictedDay);
	}
	
	public List<GeographyRestrictedDay> getRestrictedDay(int dayOfWeek ) {
		List<GeographyRestrictedDay> _restrictedDays = new ArrayList<GeographyRestrictedDay>();
		Integer primaryKey = new Integer(dayOfWeek);
		Integer allKey = new Integer(0);
		if(restrictedDays != null) {
			if(restrictedDays.get(primaryKey) != null) {
				_restrictedDays.addAll(restrictedDays.get(primaryKey));
			}
			if(restrictedDays.get(allKey) != null) {
				_restrictedDays.addAll(restrictedDays.get(allKey));
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
	
	public static boolean isTimeSlotGeoRestricted(List<GeographyRestriction> geographicRestrictions
			, FDTimeslot timeslot
			, List<String> messages, DateRange baseRange, List comments) {
		boolean isRestricted = false;
		if(geographicRestrictions != null && timeslot != null && messages != null) {
			for ( GeographyRestriction geoRestriction : geographicRestrictions ) {
				if(isTimeSlotGeoRestricted(geoRestriction, timeslot, messages, baseRange, comments)) {
					isRestricted = true;
					break;
				}
			}
		}
		return isRestricted;
	}
	
	public static boolean isTimeSlotGeoRestricted(GeographyRestriction geographicRestrictions
												, FDTimeslot timeslot
												, List<String> messages, DateRange baseRange, List comments) {
		
		boolean isRestricted = false;
		if(geographicRestrictions != null) {
			List<GeographyRestrictedDay> restrictedDays = geographicRestrictions.getRestrictedDay(timeslot.getDayOfWeek());
			if( restrictedDays != null ) {
				for ( GeographyRestrictedDay restrictedDay : restrictedDays ) {
					if( restrictedDay != null ) {
						try {											
							if(geographicRestrictions.contains(timeslot.getBaseDate())) {
								//System.out.println("Check Passed >"+DateUtil.format(timeslot.getBaseDate()));
								isRestricted = restrictedDay.isMatching(timeslot.getDlvTimeslot().getStartTime());
								if(isRestricted && isWithinBaseRange(baseRange, timeslot.getBaseDate()) 
										&& "X".equalsIgnoreCase(geographicRestrictions.getShowMessage())) {
									
									GeographyRestrictionMessage _tmpMessage = new GeographyRestrictionMessage();
									_tmpMessage.setMessage(geographicRestrictions.getMessage());
									_tmpMessage.setShowMessage(geographicRestrictions.getShowMessage());
									messages.add(_tmpMessage.getMessage());								
								}
								if(isRestricted && isWithinBaseRange(baseRange, timeslot.getBaseDate())) {
									comments.add(geographicRestrictions.getComments());
								}
								if(isRestricted) {
									break;
								}
							}
						} catch(Exception e) {
							//Timeslot filtering failed should display ignore filtering
							e.printStackTrace();
							LOGGER.debug("GeoRestriction Restrictions Error :"+e.getMessage());
						}
					}
				}
			}
		}
		return isRestricted;
	}
	
	public static boolean isWithinBaseRange(DateRange range, Date date) {
		
		boolean result = true;
		if(range != null && date != null && range.getStartDate() != null && range.getEndDate() != null) {
			
			return (date.after(range.getStartDate()) && date.before(range.getEndDate()))
						|| date.equals(range.getStartDate()) || date.equals(range.getEndDate());
		}
		return result;
	}
	public String getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	
}
