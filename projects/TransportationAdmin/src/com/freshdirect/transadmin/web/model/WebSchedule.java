package com.freshdirect.transadmin.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;

public class WebSchedule 
{
	private EmployeeInfo empInfo;
	
	private ScheduleEmployee mon;
	private ScheduleEmployee tue;
	private ScheduleEmployee wed;
	private ScheduleEmployee thu;
	private ScheduleEmployee fri;
	private ScheduleEmployee sat;
	private ScheduleEmployee sun;
	
	private Date weekOf;
	
	private String employeeIds;
	
	public WebSchedule(String employeeIds, Date weekOf)
	{
		empInfo = new EmployeeInfo();
		mon = initSchedule(weekOf);
		tue = initSchedule(weekOf);
		wed = initSchedule(weekOf);
		thu = initSchedule(weekOf);
		fri = initSchedule(weekOf);
		sat = initSchedule(weekOf);
		sun = initSchedule(weekOf);
		
		this.weekOf = weekOf;
		this.employeeIds = employeeIds;
	}
	
	private ScheduleEmployee initSchedule(Date weekOf) {
		ScheduleEmployee _sch = new ScheduleEmployee(); 
		_sch.setWeekOf(weekOf);
		return _sch;
	}
	
	public String getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(String employeeIds) {
		this.employeeIds = employeeIds;
	}

	public Date getWeekOf() {
		return weekOf;
	}

	public void setWeekOf(Date weekOf) {
		this.weekOf = weekOf;
	}

	public EmployeeInfo getEmpInfo() {
		return empInfo;
	}
	public ScheduleEmployee getNonNullValue(ScheduleEmployee s)
	{
		if(s==null) return new ScheduleEmployee();
		return s;
	}
	public void setEmpInfo(EmployeeInfo empInfo) {
		this.empInfo = empInfo;
	}
	public ScheduleEmployee getFri() {
		return getNonNullValue(fri);
	}
	public void setFri(ScheduleEmployee fri) {
		this.fri = fri;
	}
	public ScheduleEmployee getMon() {
		return getNonNullValue(mon);
	}
	public void setMon(ScheduleEmployee mon) {
		this.mon = mon;
	}
	public ScheduleEmployee getSat() {
		return getNonNullValue(sat);
	}
	public void setSat(ScheduleEmployee sat) {
		this.sat = sat;
	}
	public ScheduleEmployee getSun() {
		return getNonNullValue(sun);
	}
	public void setSun(ScheduleEmployee sun) {
		this.sun = sun;
	}
	public ScheduleEmployee getThu() {
		return getNonNullValue(thu);
	}
	public void setThu(ScheduleEmployee thu) {
		this.thu = thu;
	}
	public ScheduleEmployee getTue() {
		return getNonNullValue(tue);
	}
	public void setTue(ScheduleEmployee tue) {
		this.tue = tue;
	}
	public ScheduleEmployee getWed() {
		return getNonNullValue(wed);
	}
	public void setWed(ScheduleEmployee wed) {
		this.wed = wed;
	}
	
	public void setSchdules(Collection s)
	{
		for(Iterator it=s.iterator();it.hasNext();)
		{
			ScheduleEmployee se=(ScheduleEmployee)it.next();
			if(se.getDay()!=null&&se.getTime()==null&&se.getRegion()==null)
			{				
				se.setRegion(new Region(ScheduleEmployeeInfo.OFF,null));
			}
			if(ScheduleEmployeeInfo.DAY[0].equalsIgnoreCase(se.getDay())) mon=se;
			if(ScheduleEmployeeInfo.DAY[1].equalsIgnoreCase(se.getDay())) tue=se;
			if(ScheduleEmployeeInfo.DAY[2].equalsIgnoreCase(se.getDay())) wed=se;
			if(ScheduleEmployeeInfo.DAY[3].equalsIgnoreCase(se.getDay())) thu=se;
			if(ScheduleEmployeeInfo.DAY[4].equalsIgnoreCase(se.getDay())) fri=se;
			if(ScheduleEmployeeInfo.DAY[5].equalsIgnoreCase(se.getDay())) sat=se;
			if(ScheduleEmployeeInfo.DAY[6].equalsIgnoreCase(se.getDay())) sun=se;
		}
	}
	
	public Collection getSchedules()
	{
		List l=new ArrayList();
		if(!mon.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[0]))l.add(mon);
		if(!tue.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[1]))l.add(tue);
		if(!wed.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[2]))l.add(wed);
		if(!thu.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[3]))l.add(thu);
		if(!fri.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[4]))l.add(fri);
		if(!sat.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[5]))l.add(sat);
		if(!sun.isEmpty(empInfo,ScheduleEmployeeInfo.DAY[6]))l.add(sun);
		return l;
	}
	
	public boolean getIsMultiEdit() {
		if(employeeIds != null) {
			String[] ids = StringUtil.decodeStrings(employeeIds);
			if(ids != null && ids.length > 1) {
				return true;
			}
		}
		return false;
	}
}
