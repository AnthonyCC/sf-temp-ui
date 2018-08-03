/*
 * 
 * ErpSettlementInvoiceModel.java
 * Date: 02/20/16
 */

package com.freshdirect.payment.model;

/**
 * 
 * @author imohammed
 * @version
 */
 
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.common.date.SimpleDateDeserializer;
import com.freshdirect.framework.core.*;

public class ErpSettlementTransactionModel extends ModelSupport {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String transactionId = null;
	private String gatewayOrderId = null;
	private String paypalReferenceId = null;
	private String paypalReferenceIdType = null;
	private String transactionEventCode = null;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date transactionInitiationDate = null;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date transactionCompletionDate = null;
	private String transactionDebitOrCredit = null;
	private long grossTransactionAmount = 0;
	private String grossTransactionCurrency = null;
	private String feeDebitOrCredit = null;
	private long feeAmount = 0;
	private String feeCurrency = null;
	private String consumerId = null;
	private String paymentTrackingId = null;
	
	private String status = "";
	private Date creationDate = null;
	
	private String customField = null;
	private String storeId = null;
	private long creditTransactionalFee = 0;
	private long creditPromotionalFee = 0;
	private String creditTerm = null;
	private String orderId = null;
	public ErpSettlementTransactionModel(){
		super();
	}
	
	public ErpSettlementTransactionModel(String transactionId,
			String gatewayOrderId, String paypalReferenceId,	String paypalReferenceIdType,
			String transactionEventCode, Date transactionInitiationDate, Date transactionCompletionDate,
			String transactionDebitOrCredit, long grossTransactionAmount,
			String grossTransactionCurrency, String feeDebitOrCredit,
			long feeAmount, String feeCurrency,
			String consumerId, String paymentTrackingId, String customField, String storeId,
			long creditTransactionalFee, long creditPromotionalFee, String creditTerm, String status){
		super();
    	this.transactionId = transactionId;
    	this.gatewayOrderId = gatewayOrderId;
    	this.paypalReferenceId = paypalReferenceId;
    	this.paypalReferenceIdType = paypalReferenceIdType;
    	this.transactionEventCode = transactionEventCode;
    	this.transactionInitiationDate = transactionInitiationDate;
    	this.transactionCompletionDate = transactionCompletionDate;
    	this.transactionDebitOrCredit = transactionDebitOrCredit;
    	this.grossTransactionAmount = grossTransactionAmount;
    	this.grossTransactionCurrency = grossTransactionCurrency;
    	this.feeDebitOrCredit = feeDebitOrCredit;
    	this.feeAmount = feeAmount;
    	this.feeCurrency = feeCurrency;
    	this.consumerId = consumerId;
    	this.paymentTrackingId = paymentTrackingId;
    	this.customField = customField;
    	this.storeId = storeId;
    	this.creditTransactionalFee = creditTransactionalFee;
    	this.creditPromotionalFee = creditPromotionalFee;
    	this.creditTerm = creditTerm;
    	this.status = status;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String getwayOrderId) {
		this.gatewayOrderId = getwayOrderId;
		this.orderId = gatewayOrderId.substring(0, gatewayOrderId.indexOf("X"));
	}

	public String getOrderId() {
		return orderId;
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

	public boolean equals(Object o){
		if(!(o instanceof ErpSettlementTransactionModel)){
			return false;
		}
		
		ErpSettlementTransactionModel comp = (ErpSettlementTransactionModel)o;
		
		return transactionId.equals(comp.getTransactionId()) &&
				gatewayOrderId.equals(comp.getGatewayOrderId()) &&
				paypalReferenceId.equals(comp.getPaypalReferenceId())&&
				paypalReferenceIdType.equals(comp.getPaypalReferenceIdType()) &&
				transactionEventCode.equals(comp.getTransactionEventCode()) &&
				transactionInitiationDate.equals(comp.getTransactionInitiationDate()) &&
				transactionCompletionDate.equals(comp.getTransactionCompletionDate()) &&
				transactionDebitOrCredit.equals(comp.getTransactionDebitOrCredit()) &&
				grossTransactionAmount == comp.getGrossTransactionAmount() &&
				grossTransactionCurrency.equals(comp.getGrossTransactionCurrency()) &&
				feeDebitOrCredit.equals(comp.getFeeDebitOrCredit()) &&
				feeAmount == comp.getFeeAmount() && feeCurrency.equals(comp.getFeeCurrency()) &&
				consumerId.equals(comp.getConsumerId()) &&
				paymentTrackingId.equals(comp.getPaymentTrackingId());
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCustomField() {
		return customField;
	}

	public void setCustomField(String customField) {
		this.customField = customField;
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	
}
