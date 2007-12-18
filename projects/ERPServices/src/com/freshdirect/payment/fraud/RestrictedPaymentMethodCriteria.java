package com.freshdirect.payment.fraud;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.payment.EnumBankAccountType;

public class RestrictedPaymentMethodCriteria implements Serializable {

	private EnumBankAccountType bankAccountType;
	private String abaRouteNumber = null;
	private String accountNumber = null;
	private String lastName = null;
	private String firstName = null;
	private Date createDate;
	private EnumRestrictionReason reason;
	private EnumRestrictedPaymentMethodStatus status;
	
	public RestrictedPaymentMethodCriteria () {
		super();
	}
		
	public RestrictedPaymentMethodCriteria (EnumBankAccountType bankAccountType, String abaRouteNumber, String accountNumber, String firstName, String lastName, Date createDate,
			EnumRestrictionReason reason, EnumRestrictedPaymentMethodStatus status){
		this();
		setAbaRouteNumber(abaRouteNumber);
		setAccountNumber(accountNumber);
		setFirstName(firstName);
		setLastName(lastName);
		setCreateDate(createDate);
		setStatus(status);
		setReason(reason);
	}
	
	public RestrictedPaymentMethodCriteria(RestrictedPaymentMethodCriteria criteria){
		this(criteria.bankAccountType, criteria.abaRouteNumber, criteria.getAccountNumber(), criteria.getFirstName(), criteria.getLastName(), criteria.getCreateDate(),
			criteria.getReason(), criteria.getStatus());
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public EnumBankAccountType getBankAccountType() {
		return this.bankAccountType;
	}
	
	public String getAbaRouteNumber() {
		return this.abaRouteNumber;
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public EnumRestrictedPaymentMethodStatus getStatus() {
		return this.status;
	}
		
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public EnumRestrictionReason getReason() {
		return this.reason;
	}
	

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBankAccountType(EnumBankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public void setAbaRouteNumber(String abaRouteNumber) {
		this.abaRouteNumber = abaRouteNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public void setStatus(EnumRestrictedPaymentMethodStatus status) {
		this.status = status;
	}
			
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
		
	public void setReason(EnumRestrictionReason reason) {
		this.reason = reason;
	}
	
}
