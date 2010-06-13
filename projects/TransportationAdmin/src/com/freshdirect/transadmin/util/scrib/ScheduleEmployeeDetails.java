package com.freshdirect.transadmin.util.scrib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.ScheduleEmployee;

public class ScheduleEmployeeDetails {
	ScheduleEmployee schedule;
	Date date;
	EmployeeInfo info;
	Collection empRoles;
	List<ScheduleEmployeeDetails> members;
	
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
	public List<ScheduleEmployeeDetails> getMembers() {
		return members;
	}
	public void setMembers(List<ScheduleEmployeeDetails> members) {
		this.members = members;
	}
	
	public void addMember(ScheduleEmployeeDetails member) {
		if(members == null) {
			members = new ArrayList<ScheduleEmployeeDetails>();
		}
		members.add(member);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleEmployeeDetails other = (ScheduleEmployeeDetails) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}
	
	
}
