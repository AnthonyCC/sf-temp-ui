package com.freshdirect.dataloader.autoorder.create.command;

import java.util.Date;

public class OrderBean {
	
	private String orderId;
	private String locationId;
	private String customerId;
	private String address1;
	private String address2;
	private String apt;
	private String serviceType;
	private String city;
	private String country;
	private Date startTime;
	private Date endTime;
	private String state;
	private String zip;
	
	private String erpCustomerPK;
	private String fdCustomerPK;
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = new Date(endTime);
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = new Date(startTime);
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getApt() {
		return apt;
	}
	public void setApt(String apt) {
		this.apt = apt;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getErpCustomerPK() {
		return erpCustomerPK;
	}
	public void setErpCustomerPK(String erpCustomerPK) {
		this.erpCustomerPK = erpCustomerPK;
	}
	public String getFdCustomerPK() {
		return fdCustomerPK;
	}
	public void setFdCustomerPK(String fdCustomerPK) {
		this.fdCustomerPK = fdCustomerPK;
	} 

}
