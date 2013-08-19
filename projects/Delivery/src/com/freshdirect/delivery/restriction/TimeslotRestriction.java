package com.freshdirect.delivery.restriction;

import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;

public class TimeslotRestriction extends ModelSupport   {
	
	private static final long serialVersionUID = -3997430755012958137L;
	private final static Logger LOGGER = LoggerFactory.getInstance(TimeslotRestriction.class);
	private String dayOfWeek;
	private TimeOfDay startTime;
    private TimeOfDay endTime;
    private String zoneCode;
    private EnumLogicalOperator condition;

	public static boolean isTimeSlotRestricted(List<TimeslotRestriction> tsRestrictions
			, FDTimeslot timeslot, DateRange baseRange) {
		boolean isRestricted = false;
		if(tsRestrictions != null && timeslot != null) {
			for ( TimeslotRestriction tsRestriction : tsRestrictions ) {
				if(isTimeSlotRestricted(tsRestriction, timeslot, baseRange)) {
					isRestricted = true;
					break;
				}
			}
		}
		return isRestricted;
	}
	
	public static boolean isTimeSlotRestricted(TimeslotRestriction tsRestriction
												, FDTimeslot timeslot, DateRange baseRange) {
		
		boolean isRestricted = false;
		if(tsRestriction != null) {
				try {											
					if(timeslot.getZoneCode().equals(tsRestriction.getZoneCode()) 
							&& DateUtil.formatDayOfWeek(timeslot.getBaseDate()).equalsIgnoreCase(tsRestriction.getDayOfWeek())
							&& tsRestriction.isMatching(timeslot.getDlvTimeslot().getStartTime())) {
							return true;
					}
				}
				catch(Exception e) {
							e.printStackTrace();
							LOGGER.debug("TimeslotRestriction Restrictions Error :"+e.getMessage());
				}
		}
		return isRestricted;
	}
	
	public boolean isMatching(TimeOfDay timeSlotStartTime) {
		if(condition == null) {
			return true;
		} else {
			//System.out.println(timeSlotStartTime+" : "+startTime+" : "+(timeSlotStartTime.after(startTime) && timeSlotStartTime.before(endTime)));
			if (condition.equals(EnumLogicalOperator.LESS_THAN)) {             
                return timeSlotStartTime.before(startTime);
            }
            else if (condition.equals(EnumLogicalOperator.GREATER_THAN)) {              
                return timeSlotStartTime.after(startTime);
            }
            else if (condition.equals(EnumLogicalOperator.LESS_THAN_OR_EQUAL)) {               
            	return timeSlotStartTime.before(startTime) || timeSlotStartTime.equals(startTime);
            }
            else if (condition.equals(EnumLogicalOperator.GREATER_THAN_OR_EQUAL)) {                
                return timeSlotStartTime.after(startTime) || timeSlotStartTime.equals(startTime);
            }
            else if (condition.equals(EnumLogicalOperator.BETWEEN))  {
            	return (timeSlotStartTime.after(startTime) && timeSlotStartTime.before(endTime))
            			|| timeSlotStartTime.equals(startTime);
            }
            else if (condition.equals(EnumLogicalOperator.NOT_EQUAL)) {
             
                return !timeSlotStartTime.equals(startTime);
            }            
		}
		return false;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public TimeOfDay getStartTime() {
		return startTime;
	}

	public void setStartTime(TimeOfDay startTime) {
		this.startTime = startTime;
	}

	public TimeOfDay getEndTime() {
		return endTime;
	}

	public void setEndTime(TimeOfDay endTime) {
		this.endTime = endTime;
	}

	public EnumLogicalOperator getCondition() {
		return condition;
	}

	public void setCondition(EnumLogicalOperator condition) {
		this.condition = condition;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
}
