package com.freshdirect.payment.gateway.impl;

import com.freshdirect.payment.gateway.Currency;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;

abstract class PaymentMethodImpl implements PaymentMethod {

	private String accountNumber;
	private String customerName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String customerID;
	private String billingProfileID;
	private PaymentMethodType type;
	private Currency currency=Currency.USD;
	private String ewalletId;
	private String ewalletTxId;
	private String vendorEWalletID;
	private String emailID;
	private String deviceId;
	
	/* (non-Javadoc)
	 * @see com.freshdirect.payment.gateway.PaymentMethod#setEwalletTxId(java.lang.String)
	 */
	@Override
	public void setEwalletTxId(String ewalletId) {
		this.ewalletTxId = ewalletId;
		
	}
	/* (non-Javadoc)
	 * @see com.freshdirect.payment.gateway.PaymentMethod#getEwalletTxId()
	 */
	@Override
	public String getEwalletTxId() {
		// TODO Auto-generated method stub
		return ewalletTxId;
	}
	PaymentMethodImpl(PaymentMethodType type) {
		this.type=type;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getBillingProfileID() {
		return billingProfileID;
	}
	public void setBillingProfileID(String billingProfileID) {
		this.billingProfileID = billingProfileID;
	}
	
	public String getMaskedAccountNumber() {
		return accountNumber;
	}
	public PaymentMethodType getType() {
		return type;
	}
	
	public void setType(PaymentMethodType type) {
		this.type=type;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency=currency;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result
				+ ((addressLine1 == null) ? 0 : addressLine1.hashCode());
		result = prime * result
				+ ((addressLine2 == null) ? 0 : addressLine2.hashCode());
		result = prime
				* result
				+ ((billingProfileID == null) ? 0 : billingProfileID.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((customerID == null) ? 0 : customerID.hashCode());
		result = prime * result
				+ ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentMethodImpl other = (PaymentMethodImpl) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (addressLine1 == null) {
			if (other.addressLine1 != null)
				return false;
		} else if (!addressLine1.equals(other.addressLine1))
			return false;
		if (addressLine2 == null) {
			if (other.addressLine2 != null)
				return false;
		} else if (!addressLine2.equals(other.addressLine2))
			return false;
		if (billingProfileID == null) {
			if (other.billingProfileID != null)
				return false;
		} else if (!billingProfileID.equals(other.billingProfileID))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (customerID == null) {
			if (other.customerID != null)
				return false;
		} else if (!customerID.equals(other.customerID))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		return true;
	}
	
	public String toString() {
		return "PaymentMethodImpl [accountNumber=" + getMaskedAccountNumber()
				+ ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", billingProfileID=" + billingProfileID
				+ ", city=" + city + ", country=" + country + ", customerID="
				+ customerID + ", customerName=" + customerName + ", state="
				+ state + ", type=" + type + ", zipCode=" + zipCode  
				+", currency=" + currency +"]";
	}
	/**
	 * @return the ewalletId
	 */
	@Override
	public String getEwalletId() {
		return ewalletId;
	}
	/**
	 * @param ewalletId the ewalletId to set
	 */
	@Override
	public void setEwalletId(String ewalletId) {
		this.ewalletId = ewalletId;
	}
	
	@Override
	public String getVendorEWalletID() {
		return vendorEWalletID;
	}
	
	@Override
	public void setVendorEWalletID(String vendorEWalletID) {
		this.vendorEWalletID = vendorEWalletID;
		
	}

	/**
	 * @return the emailID
	 */
	@Override
	public String getEmailID() {
		return emailID;
	}
	/**
	 * @param emailID the emailID to set
	 */
	@Override
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	/**
	 * @return the deviceId
	 */
	@Override
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	@Override
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
