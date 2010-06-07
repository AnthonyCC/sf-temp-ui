package com.freshdirect.transadmin.util.scrib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.ScheduleEmployee;

public class SchdeuleEmployeeDetails implements Comparable {
	ScheduleEmployee schedule;
	Date date;
	EmployeeInfo info;
	Collection empRoles;
	List<SchdeuleEmployeeDetails> members;
	
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
	public List<SchdeuleEmployeeDetails> getMembers() {
		return members;
	}
	public void setMembers(List<SchdeuleEmployeeDetails> members) {
		this.members = members;
	}
	
	public void addMember(SchdeuleEmployeeDetails member) {
		if(members == null) {
			members = new ArrayList<SchdeuleEmployeeDetails>();
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
		SchdeuleEmployeeDetails other = (SchdeuleEmployeeDetails) obj;
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
	@Override
	public int compareTo(Object o) {
		
		if (o instanceof SchdeuleEmployeeDetails) {			
			SchdeuleEmployeeDetails s2 = (SchdeuleEmployeeDetails) o;
			long z1 = 0;
			long z2 = 0;
			if (this.getInfo() != null && this.getInfo().getHireDate() != null)
				z1 = this.getInfo().getHireDate().getTime();
			if (s2.getInfo() != null && s2.getInfo().getHireDate() != null)
				z2 = s2.getInfo().getHireDate().getTime();
			int result = -1;
			if (z1 > z2)
				result = 1;
			return result;
		}
		return 0;
	}
	
}
