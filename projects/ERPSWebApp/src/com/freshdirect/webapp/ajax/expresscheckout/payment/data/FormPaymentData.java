package com.freshdirect.webapp.ajax.expresscheckout.payment.data;

import java.util.List;

public class FormPaymentData {

	private String selected;
	private List<PaymentData> payments;

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public List<PaymentData> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentData> payments) {
		this.payments = payments;
	}

}
