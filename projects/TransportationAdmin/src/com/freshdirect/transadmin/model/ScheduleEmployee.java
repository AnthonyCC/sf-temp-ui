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
	private Zone depotZone;
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
			Region region, Date time, Zone depotZone, String day, Date weekOf) {
		super();
		this.scheduleId = scheduleId;
		this.employeeId = employeeId;
		this.region = region;
		this.time = time;
		this.depotZone = depotZone;
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
	public Zone getDepotZone() {
		return depotZone;
	}
	public void setDepotZone(Zone depotZone) {
		this.depotZone = depotZone;
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

		try 
		{
			if(timeS!=null&&timeS.length()>0)time=TransStringUtil.getServerTime(timeS);
			else time=null;
		} catch (ParseException e) 
		{
			
		}
	}
	
	public String getDepotZoneS() 
	{
		if(depotZone!=null)return depotZone.getZoneCode();
		return null;
	}
	public void setDepotZoneS(String zoneS) 
	{
		if(zoneS!=null&&zoneS.length()>0){ depotZone=new Zone();depotZone.setZoneCode(zoneS);}
		else depotZone=null;
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
				region=null;
				time=null;
				depotZone=null;
			}
			if(this.scheduleId!=null&&this.scheduleId.trim().length()==0)this.scheduleId=null;
			return false;
		}
		return true;
	}
	
	public Object clone() throws CloneNotSupportedException {
		
		ScheduleEmployee _clone = new ScheduleEmployee(this.getScheduleId(), this.getEmployeeId(),
											this.getRegion(), this.getTime(), this.getDepotZone(), this.getDay(), this.getWeekOf());
		return _clone;
	}
	
}
