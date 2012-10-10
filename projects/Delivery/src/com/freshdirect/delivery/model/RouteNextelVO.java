package com.freshdirect.delivery.model;

import java.io.Serializable;

public class RouteNextelVO implements Serializable {
	
	private int nextel;
	private String employee;
	private String empId;
	
	public int getNextel() {
		return nextel;
	}
	public void setNextel(int nextel) {
		this.nextel = nextel;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public RouteNextelVO(int nextel, String employee, String empId) {
		super();
		this.nextel = nextel;
		this.employee = employee;
		this.empId = empId;
	}
	public RouteNextelVO() {
		super();	
	}	
}
