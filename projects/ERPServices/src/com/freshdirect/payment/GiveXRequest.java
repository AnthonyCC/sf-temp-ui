package com.freshdirect.payment;

import com.freshdirect.customer.ErpPaymentMethodI;

public class GiveXRequest {

	ErpPaymentMethodI paymentMethod;
	ErpPaymentMethodI paymentMethodTo;
	double amount;
	long authCode;
	String reference;
	
	public ErpPaymentMethodI getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(ErpPaymentMethodI paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getAuthCode() {
		return authCode;
	}
	public void setAuthCode(long authCode) {
		this.authCode = authCode;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public ErpPaymentMethodI getPaymentMethodTo() {
		return paymentMethodTo;
	}
	public void setPaymentMethodTo(ErpPaymentMethodI paymentMethodTo) {
		this.paymentMethodTo = paymentMethodTo;
	}
	
}
