package com.freshdirect.transadmin.web.model;

import java.io.Serializable;

public class ScheduleCheckResult implements Serializable {
	
	String firstName;
	String lastName;
	String employeeId;
	String hasData;
	
	public ScheduleCheckResult(String firstName, String lastName,
			String employeeId, String hasData) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeId = employeeId;
		this.hasData = hasData;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getHasData() {
		return hasData;
	}
	public void setHasData(String hasData) {
		this.hasData = hasData;
	}
	
	
}
