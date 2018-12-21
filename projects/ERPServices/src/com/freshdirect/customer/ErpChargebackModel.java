/*
 * ErpChargebackTransaction.java
 *
 * Created on January 9, 2002, 3:06 PM
 */
package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.util.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.common.date.SimpleDateDeserializer;

public class ErpChargebackModel extends ErpPaymentModel {
	
	
	private static final long serialVersionUID = 5236617128370452805L;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date batchDate;
	private String batchNumber;
	private String cbkControlNumber;
	private double cbkDiscount;
	private String cbkReasonCode;
	private String cbkReferenceNumber;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date cbkRespondDate;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date cbkWorkDate;
	private double originalTxAmount;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date originalTxDate;
	private String description;
	private String merchantReferenceNumber;
	private boolean trxnComplete=false;
	
	/** 
	 * Creates new ErpAuthorizeTransaction 
	 */
    public ErpChargebackModel() {
		super(EnumTransactionType.CHARGEBACK);
    }

    protected ErpChargebackModel(EnumTransactionType txType){
    	super(txType);
    }
    
    public Date getBatchDate(){
    	return this.batchDate;
    }
    public void setBatchDate(Date batchDate){
    	this.batchDate = batchDate;
    }
    
    public String getBatchNumber(){
    	return this.batchNumber;
    }
    public void setBatchNumber(String batchNumber){
    	this.batchNumber = batchNumber;
    }
    
    public String getCbkControlNumber(){
    	return this.cbkControlNumber;
    }
    public void setCbkControlNumber(String cbkControlNumber){
    	this.cbkControlNumber = cbkControlNumber;
    }
    
    public double getCbkDiscount(){
    	return this.cbkDiscount;
    }
    public void setCbkDiscount(double cbkDiscount){
    	this.cbkDiscount = cbkDiscount;
    }
    
    public String getCbkReasonCode(){
    	return this.cbkReasonCode;
    }
    public void setCbkReasonCode(String cbkReasonCode){
    	this.cbkReasonCode = cbkReasonCode;
    }
    
    public String getCbkReferenceNumber(){
    	return this.cbkReferenceNumber;
    }
    public void setCbkReferenceNumber(String cbkReferenceNumber){
    	this.cbkReferenceNumber = cbkReferenceNumber;
    }
    
    public Date getCbkRespondDate(){
    	return this.cbkRespondDate;
    }
    public void setCbkRespondDate(Date cbkRespondDate){
    	this.cbkRespondDate = cbkRespondDate;
    }
    
    public Date getCbkWorkDate(){
    	return this.cbkWorkDate;
    }
    public void setCbkWorkDate(Date cbkWorkDate){
    	this.cbkWorkDate = cbkWorkDate;
    }
    
    public double getOriginalTxAmount(){
    	return this.originalTxAmount;
    }
    public void setOriginalTxAmount(double originalTxAmount){
    	this.originalTxAmount = originalTxAmount;
    }
    
    public Date getOriginalTxDate(){
    	return this.originalTxDate;
    }
    public void setOriginalTxDate(Date originalTxDate){
    	this.originalTxDate = originalTxDate;
    }
    
    public String getDescription(){
    	return this.description;
    }
    public void setDescription(String description){
    	this.description = description;
    }
    
    public String getMerchantReferenceNumber(){
    	return this.merchantReferenceNumber;
    }
    public void setMerchantReferenceNumber(String merchantReferenceNumber){
    	this.merchantReferenceNumber = merchantReferenceNumber;
    }

	/**
	 * @return the trxnComplete
	 */
	public boolean isTrxnComplete() {
		return trxnComplete;
	}

	/**
	 * @param trxnComplete the trxnComplete to set
	 */
	public void setTrxnComplete(boolean trxnComplete) {
		this.trxnComplete = trxnComplete;
	}
    
    

}
