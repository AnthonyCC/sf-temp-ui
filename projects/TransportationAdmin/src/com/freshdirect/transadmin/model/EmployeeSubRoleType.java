package com.freshdirect.transadmin.model;

import java.io.Serializable;

public class EmployeeSubRoleType implements Serializable 
{
	private String code;
	private String name;
	private String description;
	private EmployeeRoleType role;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public EmployeeRoleType getRole() {
		return role;
	}
	public void setRole(EmployeeRoleType role) {
		this.role = role;
	}
	
	
}
