package com.freshdirect.payment.gateway;
public interface PaymentMethod extends java.io.Serializable {
	
	public PaymentMethodType getType();
	public void setType(PaymentMethodType type);
	public String getAccountNumber();
	public void setAccountNumber(String accountNumber);
	public String getMaskedAccountNumber();
	public String getCustomerName();
	public void setCustomerName(String customerName);
	public String getAddressLine1();
	public void setAddressLine1(String addressLine1);
	public String getAddressLine2();
	public void setAddressLine2(String addressLine2);
	public String getCity();
	public void setCity(String city);
	public String getState();
	public void setState(String state);
	public String getZipCode();
	public void setZipCode(String zipCode);
	public String getCountry();
	public void setCountry(String country);
	public String getBillingProfileID();
	public void setBillingProfileID(String profileID);
	public String getCustomerID();
	public void setCustomerID(String customerID);
	public void setCurrency(Currency currency);
	public Currency getCurrency();
	
	public void setEwalletId(String ewalletId);
	public String getEwalletId();
	public void setEwalletTxId(String ewalletId);
	public String getEwalletTxId();
}

