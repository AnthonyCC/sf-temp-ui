package com.freshdirect.webapp.ajax.registration;

import java.io.Serializable;

public class RegistrationRequest implements Serializable {
	private static final long serialVersionUID = 6547438405128742682L;
	
	private String serviceType;
	private String firstName;
	private String lastName;
	private String zipcode;
	private String email;
	private String emailConfirm;
	private String altEmail;
	private String password;
	private String passwordConfirm;
	private String securityQuestion;
	private String successPage;
	private String terms;
	private String isAjax;
	
	private String deliveryInstructions;
	private String receiveNews;
	private String plainTextEmail;

	private String companyName;
	private String street1;
	private String street2;
	private String apt;
	private String city;
	private String state;
	private String addressOrLocation;
	
	private String title;
	private String homePhone;
	private String homePhoneExt;
	private String workPhone;
	private String workPhoneExt;
	private String cellPhone;
	private String cellPhoneExt;
	private String workDepartment;
	private String employeeId;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
	public String getHomePhoneExt() {
		return homePhoneExt;
	}
	public void setHomePhoneExt(String homePhoneExt) {
		this.homePhoneExt = homePhoneExt;
	}
	
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	
	public String getWorkPhoneExt() {
		return workPhoneExt;
	}
	public void setWorkPhoneExt(String workPhoneExt) {
		this.workPhoneExt = workPhoneExt;
	}
	
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	public String getCellPhoneExt() {
		return cellPhoneExt;
	}
	public void setCellPhoneExt(String cellPhoneExt) {
		this.cellPhoneExt = cellPhoneExt;
	}
	
	public String getWorkDepartment() {
		return workDepartment;
	}
	public void setWorkDepartment(String department) {
		this.workDepartment = department;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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
	
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmailConfirm() {
		return emailConfirm;
	}
	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	
	public String getSuccessPage() {
		if (this.successPage == null) {
			this.setSuccessPage("");
		}
		return successPage;
	}
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}
	
	public String getTerms() {
		if (this.terms == null) {
			this.setTerms("false");
		}
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	
	public String getIsAjax() {
		if (this.isAjax == null) {
			this.setIsAjax("false");
		}
		return isAjax;
	}
	public void setIsAjax(String isAjax) {
		this.isAjax = isAjax;
	}
	
	public String getDeliveryInstructions() {
		if (this.deliveryInstructions == null) {
			this.setDeliveryInstructions("none");
		}
		return deliveryInstructions;
	}
	public void setDeliveryInstructions(String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}
	
	public String getReceiveNews() {
		if (this.receiveNews == null) {
			this.setReceiveNews("true");
		}
		return receiveNews;
	}
	public void setReceiveNews(String receiveNews) {
		this.receiveNews = receiveNews;
	}
	
	public String getPlainTextEmail() {
		if (this.plainTextEmail == null) {
			this.setPlainTextEmail("false");
		}
		return plainTextEmail;
	}
	public void setPlainTextEmail(String plainTextEmail) {
		this.plainTextEmail = plainTextEmail;
	}
	
	public String getAltEmail() {
		return altEmail;
	}
	public void setAltEmail(String altEmail) {
		this.altEmail = altEmail;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getApt() {
		return apt;
	}
	public void setApt(String apt) {
		this.apt = apt;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getAddressOrLocation() {
		return addressOrLocation;
	}
	public void setAddressOrLocation(String addressOrLocation) {
		this.addressOrLocation = addressOrLocation;
	}
	

}
