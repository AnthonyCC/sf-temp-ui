package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.util.Date;

public class PDE0017DRecord extends DFRDataRecord {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private double issuerCBKAmount;
	private String partialRepresentement;
	private String statusFlag;
	private String sequenceNumber;
	private String merchantReferenceNumber;
	private String accountNumber;
	private String reasonCode;
	private Date transactionDate;
	private Date chargebackDate;
	private Date activityDate;
	private double feeAmount;
	private int usageCode;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	public Date getChargebackDate() {
		return chargebackDate;
	}
	public void setChargebackDate(Date chargebackDate) {
		this.chargebackDate = chargebackDate;
	}
	public double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}
	public double getIssuerCBKAmount() {
		return issuerCBKAmount;
	}
	public void setIssuerCBKAmount(double issuerCBKAmount) {
		this.issuerCBKAmount = issuerCBKAmount;
	}
	public String getMerchantReferenceNumber() {
		return merchantReferenceNumber;
	}
	public void setMerchantReferenceNumber(String merchantOrderNumber) {
		this.merchantReferenceNumber = merchantOrderNumber;
	}
	public String getPartialRepresentement() {
		return partialRepresentement;
	}
	public void setPartialRepresentement(String partialRepresentement) {
		this.partialRepresentement = partialRepresentement;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public int getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(int usageCode) {
		this.usageCode = usageCode;
	}
}
