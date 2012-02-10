package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.util.Date;

public class AirclicNextelVO implements Serializable {
	
	private String employee;
	private String nextTelNo;
	private String cnNo;
	private Date scanDate;
	private String routeNo;
	
	public AirclicNextelVO() {
		super();
	}
	public AirclicNextelVO(String employee, String nextTelNo, Date scanDate, String routeNo) {
		super();
		this.employee = employee;
		this.nextTelNo = nextTelNo;
		this.scanDate = scanDate;
		this.routeNo = routeNo;
	}
	
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getNextTelNo() {
		return nextTelNo;
	}
	public void setNextTelNo(String nextTelNo) {
		this.nextTelNo = nextTelNo;
	}
	public Date getScanDate() {
		return scanDate;
	}
	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	public String getRouteNo() {
		return routeNo;
	}
	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}
	public String getCnNo() {
		return cnNo;
	}
	public void setCnNo(String cnNo) {
		this.cnNo = cnNo;
	}
	
}
