package com.freshdirect.payment;

import com.freshdirect.payment.ejb.PaymentMethodData;

public class GiveXRequest {

	PaymentMethodData paymentMethod;
	PaymentMethodData paymentMethodTo;
	double amount;
	long authCode;
	String reference;
	
	public PaymentMethodData getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethodData paymentMethod) {
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
	public PaymentMethodData getPaymentMethodTo() {
		return paymentMethodTo;
	}
	public void setPaymentMethodTo(PaymentMethodData paymentMethodTo) {
		this.paymentMethodTo = paymentMethodTo;
	}
	
}
