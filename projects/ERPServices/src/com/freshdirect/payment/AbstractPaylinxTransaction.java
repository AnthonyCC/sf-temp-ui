/*
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.payment;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jng
 *
 */
public abstract class AbstractPaylinxTransaction implements Serializable {
	private String serverId;
	private String transactionCode;
	private String lccReturnCode;
	private String lccReturnMessage;
	private String batchId;
	private String draftId;
	private String merchantId;
	private String transactionState;
	private Date transactionDateTime;
	private String orderNumber;
	private double amount;
	private String verificationResult;
	private String approvalCode;
	private String procResponseCode;
	private String procResponseMessage;
	private String sequenceNumber;
	private String badFieldCode;
	private String badFieldData;
	private String customerName;

	public String getServerId() {return this.serverId;}
	public void setServerId(String serverId) { this.serverId = serverId; }

	public String getTransactionCode() {return this.transactionCode;}
	public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

	public String getLccReturnCode() {return this.lccReturnCode;}
	public void setLccReturnCode(String lccReturnCode) { this.lccReturnCode = lccReturnCode; }

	public String getLccReturnMessage() {return this.lccReturnMessage;}
	public void setLccReturnMessage(String lccReturnMessage) { this.lccReturnMessage = lccReturnMessage; }

	public String getBatchId() {return this.batchId;}
	public void setBatchId(String batchId) { this.batchId = batchId; }
	
	public String getDraftId() {return this.draftId;}
	public void setDraftId(String draftId) { this.draftId = draftId; }

	public String getMerchantId() {return this.merchantId;}
	public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
	
	public String getTransactionState() {return this.transactionState;}
	public void setTransactionState(String transactionState) { this.transactionState = transactionState; }
	
	public Date getTransactionDateTime() {return this.transactionDateTime;}
	public void setTransactionDateTime(Date transactionDateTime) { this.transactionDateTime = transactionDateTime; }

	public String getOrderNumber() {return this.orderNumber;}
	public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

	public double getAmount() {return this.amount;}
	public void setAmount(double amount) { this.amount = amount; }

	public String getVerificationResult() {return this.verificationResult;}
	public void setVerificationResult(String verificationResult) { this.verificationResult = verificationResult; }

	public String getApprovalCode() {return this.approvalCode;}
	public void setApprovalCode(String approvalCode) { this.approvalCode = approvalCode; }

	public String getProcResponseCode() {return this.procResponseCode;}
	public void setProcResponseCode(String procResponseCode) { this.procResponseCode = procResponseCode; }

	public String getProcResponseMessage() {return this.procResponseMessage;}
	public void setProcResponseMessage(String procResponseMessage) { this.procResponseMessage = procResponseMessage; }
	
	public String getSequenceNumber() {return this.sequenceNumber;}
	public void setSequenceNumber(String sequenceNumber) { this.sequenceNumber = sequenceNumber; }

	public String getBadFieldCode() {return this.badFieldCode;}
	public void setBadFieldCode(String badFieldCode) { this.badFieldCode = badFieldCode; }

	public String getBadFieldData() {return this.badFieldData;}
	public void setBadFieldData(String badFieldData) { this.badFieldData = badFieldData; }
	
	public String getCustomerName() {return this.customerName;}
	public void setCustomerName(String customerName) { this.customerName = customerName; }

}
