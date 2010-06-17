package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.freshdirect.transadmin.model.EmployeeInfo;

public class WebPlanResource  implements Serializable
{
	String planId;
	WebEmployeeInfo emp;
	String paycode;
	
	public WebEmployeeInfo getEmp() {
		return emp;
	}
	public void setEmp(WebEmployeeInfo emp) {
		this.emp = emp;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	public EmployeeInfo getEmpInfo() {
		return emp.getEmpInfo();
	}
	public String getEmployeeId() {
		return emp.getEmployeeId();
	}
	public String getEmployeeRoleType() {
		return emp.getEmployeeRoleType();
	}
	public Collection getEmployeeRoleTypes() {
		return emp.getEmployeeRoleTypes();
	}
	public Collection getEmpRole() {
		return emp.getEmpRole();
	}
	public String getFirstName() {
		return emp.getFirstName();
	}
	public Date getHireDate() {
		return emp.getHireDate();
	}
	public String getJobType() {
		return emp.getJobType();
	}
	public String getLastName() {
		return emp.getLastName();
	}
	public String getMiddleInitial() {
		return emp.getMiddleInitial();
	}
	public String getShortName() {
		return emp.getShortName();
	}
	public String getStatus() {
		return emp.getStatus();
	}
	public String getSupervisorFirstName() {
		return emp.getSupervisorFirstName();
	}
	public String getSupervisorId() {
		return emp.getSupervisorId();
	}
	public String getSupervisorLastName() {
		return emp.getSupervisorLastName();
	}
	public String getSupervisorMiddleInitial() {
		return emp.getSupervisorMiddleInitial();
	}
	public String getSupervisorShortName() {
		return emp.getSupervisorShortName();
	}
	public Date getTerminationDate() {
		return emp.getTerminationDate();
	}
	public int hashCode() {
		return emp.hashCode();
	}
	public void setEmpInfo(EmployeeInfo empInfo) {
		emp.setEmpInfo(empInfo);
	}
	public void setEmployeeRoleType(Collection employeeRoles) {
		emp.setEmployeeRoleType(employeeRoles);
	}
	public void setEmpRole(Collection empRole) {
		emp.setEmpRole(empRole);
	}
	public String toString() {
		return emp.toString();
	}
	public String getPaycode() {
		return paycode;
	}
	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}
	
	

}
