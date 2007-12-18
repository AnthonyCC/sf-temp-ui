package com.freshdirect.crm;

import java.io.Serializable;

import com.freshdirect.common.address.PhoneNumber;

public class CrmCustomerHeaderInfo implements Serializable {
	
	private final String id;
	private final String email;
	private String firstName;
	private String middleName;
	private String lastName;
	private PhoneNumber homePhone;
	private PhoneNumber businessPhone;
	private PhoneNumber cellPhone;
	private int credits;
	private double remainingAmount;
	private int cases;
	private boolean onAlert;
	private boolean active;
	
	public CrmCustomerHeaderInfo(String id, String email) {
		this.id = id;
		this.email = email;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getEmail() {
		return this.email;
	}

	public PhoneNumber getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(PhoneNumber businessPhone) {
		this.businessPhone = businessPhone;
	}

	public int getCases() {
		return cases;
	}

	public void setCases(int cases) {
		this.cases = cases;
	}

	public PhoneNumber getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(PhoneNumber cellPhone) {
		this.cellPhone = cellPhone;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public PhoneNumber getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(PhoneNumber homePhone) {
		this.homePhone = homePhone;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isOnAlert() {
		return onAlert;
	}

	public void setOnAlert(boolean onAlert) {
		this.onAlert = onAlert;
	}

}
