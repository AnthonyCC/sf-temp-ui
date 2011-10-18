package com.freshdirect.transadmin.datamanager.model;

public class CancelOrderInfoModel  implements ICancelOrderInfo {
	
	private String customerId;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	
	private String companyName;
	private String orderNumber;
	private String deliveryWindow;
	private String businessPhone;
	private String cellPhone;
	
	public String getFullName(){
		return this.getLastName()+" "+this.getFirstName();
	}
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompanyName() {
		return companyName != null ? companyName.toUpperCase() : null;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getDeliveryWindow() {
		return deliveryWindow;
	}
	public void setDeliveryWindow(String deliveryWindow) {
		this.deliveryWindow = deliveryWindow;
	}
	public String getBusinessPhone() {
		return businessPhone;
	}
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

}
