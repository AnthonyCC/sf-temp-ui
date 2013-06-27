package com.freshdirect.transadmin.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.freshdirect.transadmin.util.TransStringUtil;

public class ScheduleEmployee implements Serializable, Cloneable {
	
	private String scheduleId;
	private String employeeId;
	private Region region;
	private Date time;
	private TrnFacility depotFacility;
	private String day;
	
	private Date weekOf;
	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ScheduleEmployee() {
		super();
	}
	
	public ScheduleEmployee(String scheduleId, String employeeId,
			Region region, Date time, TrnFacility depotFacility, String day, Date weekOf) {
		super();
		this.scheduleId = scheduleId;
		this.employeeId = employeeId;
		this.region = region;
		this.time = time;
		this.depotFacility = depotFacility;
		this.day = day;
		this.weekOf = weekOf;
	}
	
	
	public Date getWeekOf() {
		return weekOf;
	}
	public void setWeekOf(Date weekOf) {
		this.weekOf = weekOf;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public TrnFacility getDepotFacility() {
		return depotFacility;
	}
	public void setDepotFacility(TrnFacility depotFacility) {
		this.depotFacility = depotFacility;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getTimeS() 
	{
		try {
			if(time!=null)return TransStringUtil.getServerTime(time);
		} catch (ParseException e) 
		{
			
		}
		return null;
	}
	public void setTimeS(String timeS) 
	{
		try {
			if (timeS != null && timeS.length() > 0)
				time = TransStringUtil.getServerTime(timeS);
			else
				time = null;
		} catch (ParseException e) {

		}
	}
	
	public String getDepotFacilityS() 
	{
		if (depotFacility != null)
			return depotFacility.getFacilityId();
		return null;
	}
	public void setDepotFacilityS(String depotFacilityS) 
	{
		if(depotFacilityS != null && depotFacilityS.length() > 0){
			depotFacility = new TrnFacility(); 
			depotFacility.setFacilityId(depotFacilityS);
		} else { 
			depotFacility = null;
		}
	}
	
	public String getRegionS() 
	{
		if(region!=null)return region.getCode();
		return null;
	}
	public void setRegionS(String regionS) 
	{
		if(regionS!=null&&regionS.length()>0){ region=new Region();region.setCode(regionS);}
		else region=null;
	}	
	public boolean isEmpty(EmployeeInfo empInfo,String day)
	{
		if(region!=null)
		{
			this.day=day;
			this.employeeId=empInfo.getEmployeeId();
			if(ScheduleEmployeeInfo.OFF.equalsIgnoreCase(region.getCode()))
			{
				region = null;
				time = null;
				depotFacility = null;
			}
			if(this.scheduleId!=null&&this.scheduleId.trim().length()==0)this.scheduleId=null;
			return false;
		}
		return true;
	}
	
	public Object clone() throws CloneNotSupportedException {
		
		ScheduleEmployee _clone = new ScheduleEmployee(this.getScheduleId(), this.getEmployeeId(),
											this.getRegion(), this.getTime(), this.getDepotFacility(), this.getDay(), this.getWeekOf());
		return _clone;
	}
	
}
