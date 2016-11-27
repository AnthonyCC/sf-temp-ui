/**
 * 
 */
package com.freshdirect.mobileapi.controller.data.request;

/**
 * @author skrishnasamy
 *
 */
public class RegisterMessage extends ZipCheck {
	private String firstName;
	private String lastName;
	private String email;
	private String confirmEmail;
	private String password;
	private String confirmPassword;
	private String securityQuestion;
	private String workPhone;
	private String dlvhomephone;
	//private boolean partialDelivery;
	
	
	
	public String getDlvhomephone() {
		return dlvhomephone;
	}
	public void setDlvhomephone(String dlvhomephone) {
		this.dlvhomephone = dlvhomephone;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
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
	public String getConfirmEmail() {
		return confirmEmail;
	}
	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	/*
	public boolean isPartialDelivery() {
		return partialDelivery;
	}
	public void setPartialDelivery(boolean isPartialDelivery) {
		this.partialDelivery = isPartialDelivery;
	}
	*/
	
}
