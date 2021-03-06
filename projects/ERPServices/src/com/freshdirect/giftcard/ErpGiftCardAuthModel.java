package com.freshdirect.giftcard;

import java.util.Date;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;

/**
 * @stereotype fd-model
 */
public class ErpGiftCardAuthModel extends ErpGiftCardTransModel {
	
	private static final long	serialVersionUID	= -1723794521538000094L;
	
	ErpGiftCardTransactionModel tranModel;
	

	public double getTransactionAmount() {
		return tranModel.getTransactionAmount();
	}

	public void setTransactionAmount(double transactionAmount) {
		this.tranModel.setTransactionAmount(transactionAmount);
	}

	public Date getActionTime() {
		return this.tranModel.getActionTime();
	}

	public void setActionTime(Date actionTime) {
		this.tranModel.setActionTime(actionTime);
	}

	public Date getPostedTime() {
		return this.tranModel.getPostedTime();
	}

	public void setPostedTime(Date postedTime) {
		this.tranModel.setPostedTime(postedTime);
	}

	
	public String getErrorMsg() {
		return this.tranModel.getErrorMsg();
	}

	public void setErrorMsg(String errorMsg) {
		this.tranModel.setErrorMsg(errorMsg);
	}

	public String getAuthCode() {
		return this.tranModel.getAuthCode();
	}

	public void setAuthCode(String authCode) {
		this.tranModel.setAuthCode(authCode);
	}
 	
	public EnumGiftCardTransactionStatus getGcTransactionStatus() {
		return this.tranModel.getGcTransactionStatus();
	}

	public void setGcTransactionStatus(
			EnumGiftCardTransactionStatus gcTransactionStatus) {
		this.tranModel.setGcTransactionStatus(gcTransactionStatus);
	}

	public EnumGiftCardTransactionType getGCTransactionType() {
		return this.tranModel.getGiftCardTransactionType();
	}

	public void setGCTransactionType(EnumGiftCardTransactionType transactionType) {
		this.tranModel.setGiftCardTransactionType(transactionType);
	}

    
    public ErpGiftCardAuthModel(EnumTransactionType txType) {
		super(txType);
		this.tranModel = new ErpGiftCardTransactionModel();
		this.tranModel.setActionTime(new Date());
		this.tranModel.setPostedTime(new Date());		
    }
    
    
    public String getCertificateNum(){
		return this.tranModel.getCertificateNumber();
	}
    
	public void setCertificateNum(String certificateNum){
		this.tranModel.setCertificateNumber(certificateNum);
	}


	public String getPreAuthCode() {
		return this.tranModel.getPreAuthCode();
	}

	public void setPreAuthCode(String preAuthCode) {
		this.tranModel.setPreAuthCode(preAuthCode);
	}


	public String getReferenceId() {
		return this.tranModel.getReferenceId();
	}

	public void setReferenceId(String referenceId) {
		this.tranModel.setReferenceId(referenceId);
	}
	
	public boolean isPending() {
		return this.getGcTransactionStatus().equals(EnumGiftCardTransactionStatus.PENDING);
	}
	
	public boolean isApproved() {
		return this.getGcTransactionStatus().equals(EnumGiftCardTransactionStatus.SUCCESS);
	}

	public boolean isDeclined() {
		return this.getGcTransactionStatus().equals(EnumGiftCardTransactionStatus.FAILURE);
	}

	public boolean isCancelled() {
		return this.getGcTransactionStatus().equals(EnumGiftCardTransactionStatus.CANCEL);
	}
}
