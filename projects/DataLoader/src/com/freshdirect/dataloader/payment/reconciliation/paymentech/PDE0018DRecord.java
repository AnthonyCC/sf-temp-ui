/*
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.util.Date;

/**
 * @author jng
 * 
 */
public class PDE0018DRecord extends DFRDataRecord {

	private String statusFlag;
	private String sequenceNumber;
	private String merchantReferenceNumber;
	private String accountNumber;
	private String reasonCode;
	private Date transactionDate;
	private Date ecpReturnDate;
	private Date activityDate;
	private double ecpReturnAmount;
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
	public Date getEcpReturnDate() {
		return ecpReturnDate;
	}
	public void setEcpReturnDate(Date ecpReturnDate) {
		this.ecpReturnDate = ecpReturnDate;
	}
	public double getEcpReturnAmount() {
		return ecpReturnAmount;
	}
	public void setEcpReturnAmount(double ecpReturnAmount) {
		this.ecpReturnAmount = ecpReturnAmount;
	}
	public String getMerchantReferenceNumber() {
		return merchantReferenceNumber;
	}
	public void setMerchantReferenceNumber(String merchantOrderNumber) {
		this.merchantReferenceNumber = merchantOrderNumber;
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
