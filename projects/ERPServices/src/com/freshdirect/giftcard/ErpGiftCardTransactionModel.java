package com.freshdirect.giftcard;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;

public class ErpGiftCardTransactionModel extends ModelSupport {
	
	private String authCode;
	private String errorMsg;
	private String givexNum;
	private String certificateNumber;
	private String preAuthCode;
	private Date actionTime;
	private double transactionAmount;
    private String securityCode;
	private EnumGiftCardTransactionType  giftCardTransactionType;
	private EnumGiftCardTransactionStatus gcTransactionStatus;		 
	private String referenceId;
	

	public EnumGiftCardTransactionStatus getGcTransactionStatus() {
		return gcTransactionStatus;
	}

	public void setGcTransactionStatus(
			EnumGiftCardTransactionStatus gcTransactionStatus) {
		this.gcTransactionStatus = gcTransactionStatus;
	}

			

			
	public EnumGiftCardTransactionType getGiftCardTransactionType() {
		return giftCardTransactionType;
	}

	public void setGiftCardTransactionType(EnumGiftCardTransactionType transactionType) {
		this.giftCardTransactionType = transactionType;
	}

    
    public ErpGiftCardTransactionModel(){    	
    }
    
    public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	public String getGivexNum(){
			return this.givexNum;
	}
	
	public void setGivexNum(String givexNum){
			this.givexNum = givexNum;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getPreAuthCode() {
		return preAuthCode;
	}

	public void setPreAuthCode(String preAuthCode) {
		this.preAuthCode = preAuthCode;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

    
}
