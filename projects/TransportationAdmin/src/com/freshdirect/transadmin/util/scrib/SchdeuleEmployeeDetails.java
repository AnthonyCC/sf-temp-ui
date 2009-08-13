package com.freshdirect.transadmin.util.scrib;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.util.TransAdminCacheManager;

public class SchdeuleEmployeeDetails 
{
	ScheduleEmployee schedule;
	Date date;
	EmployeeInfo info;
	Collection empRoles;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Collection getEmpRoles() {
		return empRoles;
	}
	public void setEmpRoles(Collection empRoles) {
		this.empRoles = empRoles;
	}
	public EmployeeInfo getInfo() {
		return info;
	}
	public void setInfo(EmployeeInfo info) {
		this.info = info;
	}
	public ScheduleEmployee getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleEmployee schedule) {
		this.schedule = schedule;
	}
	
	
}
