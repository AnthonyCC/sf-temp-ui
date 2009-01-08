package com.freshdirect.delivery.restriction;

import java.io.Serializable;

import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;

public class GeographyRestrictedDay implements Serializable {
	
	private int dayOfWeek;
	private int seqNo;
	private TimeOfDay startTime;
    private TimeOfDay endTime;
    private EnumLogicalOperator condition;
    
	public EnumLogicalOperator getCondition() {
		return condition;
	}
	public void setCondition(EnumLogicalOperator condition) {
		this.condition = condition;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public TimeOfDay getEndTime() {
		return endTime;
	}
	public void setEndTime(TimeOfDay endTime) {
		this.endTime = endTime;
	}
	public TimeOfDay getStartTime() {
		return startTime;
	}
	public void setStartTime(TimeOfDay startTime) {
		this.startTime = startTime;
	}
	
	public boolean isMatching(TimeOfDay timeSlotStartTime) {
		if(condition == null) {
			return true;
		} else {
			System.out.println(timeSlotStartTime+" : "+startTime+" : "+(timeSlotStartTime.after(startTime) && timeSlotStartTime.before(endTime)));
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
            	return timeSlotStartTime.after(startTime) && timeSlotStartTime.before(endTime);
            }
            else if (condition.equals(EnumLogicalOperator.NOT_EQUAL)) {
             
                return !timeSlotStartTime.equals(startTime);
            }            
		}
		return false;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
}
