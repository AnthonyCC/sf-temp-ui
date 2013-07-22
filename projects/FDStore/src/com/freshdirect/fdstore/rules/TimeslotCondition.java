package com.freshdirect.fdstore.rules;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.rules.ConditionI;
import com.freshdirect.rules.RuleRuntimeI;

public class TimeslotCondition implements ConditionI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String startTime;
    private String endTime;
    private String day;
    private TimeOfDay startTimeDay;
    private TimeOfDay endTimeDay;
    private Double orderMinimum;
    
    
	public TimeslotCondition() {
	}

	public TimeslotCondition(String day) {
		this.day = day;
	}


	public boolean evaluate(Object target, RuleRuntimeI ctx) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public TimeOfDay getStartTimeDay() 
	{
		if(startTimeDay==null&&startTime!=null)
		{
			this.startTimeDay=new TimeOfDay(startTime);
		}
		return startTimeDay;
	}

	public void setStartTimeDay(TimeOfDay startTimeDay) {
		this.startTimeDay = startTimeDay;
	}

	public TimeOfDay getEndTimeDay() 
	{
		if(endTimeDay==null&&endTime!=null)
		{
			this.endTimeDay=new TimeOfDay(endTime);
		}
		return endTimeDay;
	}

	public void setEndTimeDay(TimeOfDay endTimeDay) {
		this.endTimeDay = endTimeDay;
	}

	public String getDay() 
	{		
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
	public Double getOrderMinimum() {
		return orderMinimum;
	}

	public void setOrderMinimum(Double orderMinimum) {
		this.orderMinimum = orderMinimum;
	}
	

}