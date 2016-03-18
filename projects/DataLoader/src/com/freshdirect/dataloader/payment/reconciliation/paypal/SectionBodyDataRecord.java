package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;
import java.util.Date;

public class SectionBodyDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4468724182798910282L;

	private String transactionId = null;
	private String invoiceId = null;
	private String paypalReferenceId = null;
	private String paypalReferenceIdType = null;
	private String transactionEventCode = null;
	private Date transactionInitiationDate = null;
	private Date transactionCompletionDate = null;
	private String transactionDebitOrCredit = null;
	private long grossTransactionAmount = 0;
	private String grossTransactionCurrency = null;
	private String feeDebitOrCredit = null;
	private long feeAmount = 0;
	private String feeCurrency = null;
	private String customField = null;
	private String consumerId = null;
	private String paymentTrackingId = null;
	private String storeId = null;
	private String bankReferenceId = null; //Not Applicable
	private long creditTransactionalFee = 0; // Not Applicable
	private long creditPromotionalFee = 0; // Not Applicable
	private String creditTerm = null;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getPaypalReferenceId() {
		return paypalReferenceId;
	}
	public void setPaypalReferenceId(String paypalReferenceId) {
		this.paypalReferenceId = paypalReferenceId;
	}
	public String getPaypalReferenceIdType() {
		return paypalReferenceIdType;
	}
	public void setPaypalReferenceIdType(String paypalReferenceIdType) {
		this.paypalReferenceIdType = paypalReferenceIdType;
	}
	public String getTransactionEventCode() {
		return transactionEventCode;
	}
	public void setTransactionEventCode(String transactionEventCode) {
		this.transactionEventCode = transactionEventCode;
	}
	public Date getTransactionInitiationDate() {
		return transactionInitiationDate;
	}
	public void setTransactionInitiationDate(Date transactionInitiationDate) {
		this.transactionInitiationDate = transactionInitiationDate;
	}
	public Date getTransactionCompletionDate() {
		return transactionCompletionDate;
	}
	public void setTransactionCompletionDate(Date transactionCompletionDate) {
		this.transactionCompletionDate = transactionCompletionDate;
	}
	public String getTransactionDebitOrCredit() {
		return transactionDebitOrCredit;
	}
	public void setTransactionDebitOrCredit(String transactionDebitOrCredit) {
		this.transactionDebitOrCredit = transactionDebitOrCredit;
	}
	public long getGrossTransactionAmount() {
		return grossTransactionAmount;
	}
	public void setGrossTransactionAmount(long grossTransactionAmount) {
		this.grossTransactionAmount = grossTransactionAmount;
	}
	public String getGrossTransactionCurrency() {
		return grossTransactionCurrency;
	}
	public void setGrossTransactionCurrency(String grossTransactionCurrency) {
		this.grossTransactionCurrency = grossTransactionCurrency;
	}
	public String getFeeDebitOrCredit() {
		return feeDebitOrCredit;
	}
	public void setFeeDebitOrCredit(String feeDebitOrCredit) {
		this.feeDebitOrCredit = feeDebitOrCredit;
	}
	public long getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(long feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getFeeCurrency() {
		return feeCurrency;
	}
	public void setFeeCurrency(String feeCurrency) {
		this.feeCurrency = feeCurrency;
	}
	public String getCustomField() {
		return customField;
	}
	public void setCustomField(String customField) {
		this.customField = customField;
	}
	public String getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	public String getPaymentTrackingId() {
		return paymentTrackingId;
	}
	public void setPaymentTrackingId(String paymentTrackingId) {
		this.paymentTrackingId = paymentTrackingId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getBankReferenceId() {
		return bankReferenceId;
	}
	public void setBankReferenceId(String bankReferenceId) {
		this.bankReferenceId = bankReferenceId;
	}
	public long getCreditTransactionalFee() {
		return creditTransactionalFee;
	}
	public void setCreditTransactionalFee(long creditTransactionalFee) {
		this.creditTransactionalFee = creditTransactionalFee;
	}
	public long getCreditPromotionalFee() {
		return creditPromotionalFee;
	}
	public void setCreditPromotionalFee(long creditPromotionalFee) {
		this.creditPromotionalFee = creditPromotionalFee;
	}
	public String getCreditTerm() {
		return creditTerm;
	}
	public void setCreditTerm(String creditTerm) {
		this.creditTerm = creditTerm;
	}
	
	
}
