package com.freshdirect.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ZoneCondition implements ConditionI {

	private final static Category LOGGER = LoggerFactory.getInstance(ZoneCondition.class);

	private String expression;
	private String zonecode;
    private String startTime;
    private String endTime;
    private String day;
    private TimeOfDay startTimeDay;
    private TimeOfDay endTimeDay;
    private List<String> zones;
    
	public ZoneCondition() {
	}

	public ZoneCondition(String expression) {
		this.expression = expression;
	}



	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean evaluate(Object target, RuleRuntimeI ctx) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getZonecode() {
		return zonecode;
	}

	public void setZonecode(String zonecode) {
		this.zonecode = zonecode;
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

	public List<String> getZones() 
	{
		if(zones==null&&zonecode!=null)
		{			
			String[] bcc = zonecode.split(",");
			zones= new ArrayList<String>(bcc.length);
	 		for (int i = 0; i < bcc.length; i++) {
	 			String addr = bcc[i].trim();
	 			if (addr.length() != 0)
	 				zones.add(addr);
	 		}			
		}
		return zones;
	}

	public void setZones(List<String> zones) {
		this.zones = zones;
	}


}