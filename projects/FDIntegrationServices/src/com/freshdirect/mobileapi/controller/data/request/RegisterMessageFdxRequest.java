package com.freshdirect.mobileapi.controller.data.request;

public class RegisterMessageFdxRequest {

	private String email;	
	private String password;
	private String securityQuestion;
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	private String firstName;
	private String lastName;
	private String serviceType;
	private String companyName;
	private String address1;	
	private String apartment;
	private String city;
	private String state;
	private String zipCode;
	private String mobile_number;
	private String workPhone;
	
	private String currentPartnerMessages;
	private boolean recieveSMSAlerts;
	
	
	public boolean isRecieveSMSAlerts() {
		return recieveSMSAlerts;
	}
	public void setRecieveSMSAlerts(boolean recieveSMSAlerts) {
		this.recieveSMSAlerts = recieveSMSAlerts;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getCurrentPartnerMessages() {
		return currentPartnerMessages;
	}
	public void setCurrentPartnerMessages(String currentPartnerMessages) {
		this.currentPartnerMessages = currentPartnerMessages;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	
}
