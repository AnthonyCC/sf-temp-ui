package com.freshdirect.delivery.model;

import java.io.Serializable;

public class DispatchNextTelVO implements Serializable {
	
	private String employeeId;
	private String employeeName;
	private String nextTelNo;
	private String dispatchId;
		
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getNextTelNo() {
		return nextTelNo;
	}
	public void setNextTelNo(String nextTelNo) {
		this.nextTelNo = nextTelNo;
	}
	public String getDispatchId() {
		return dispatchId;
	}
	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}
