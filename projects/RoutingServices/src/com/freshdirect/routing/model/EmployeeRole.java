package com.freshdirect.routing.model;

public class EmployeeRole {
	private String employeeId;
	private String role;
	private String subRole;
	
	public EmployeeRole(String employeeId, String role, String subRole) {
		super();
		this.employeeId = employeeId;
		this.role = role;
		this.subRole = subRole;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSubRole() {
		return subRole;
	}
	public void setSubRole(String subRole) {
		this.subRole = subRole;
	}
	
	
}
