package com.freshdirect.webapp.ajax.expresscheckout.payment.data;

import java.util.List;

public class FormPaymentData {

	private String selected;
	private List<PaymentData> payments;
	private boolean coveredByGiftCard;
	private boolean backupPaymentRequiredForGiftCard;

	public boolean isCoveredByGiftCard() {
		return coveredByGiftCard;
	}

	public void setCoveredByGiftCard(boolean coveredByGiftCard) {
		this.coveredByGiftCard = coveredByGiftCard;
	}

	public boolean isBackupPaymentRequiredForGiftCard() {
		return backupPaymentRequiredForGiftCard;
	}

	public void setBackupPaymentRequiredForGiftCard(boolean backupPaymentRequiredForGiftCard) {
		this.backupPaymentRequiredForGiftCard = backupPaymentRequiredForGiftCard;
	}

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
