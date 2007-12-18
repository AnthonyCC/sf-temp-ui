package com.freshdirect.customer;

public class ErpCaptureModel extends ErpPaymentModel{
	
	private String sequenceNumber;
	private String responseCode;
	private String authCode;
	private String description;
	private String merchantId;
	
	/** Creates new ErpCaptureTransactionModel */
    public ErpCaptureModel() {
		super(EnumTransactionType.CAPTURE);
    }
    
    public String getSequenceNumber(){
    	return this.sequenceNumber;
    }
    public void setSequenceNumber(String sequenceNumber){
    	this.sequenceNumber = sequenceNumber;
    }
    
    public String getResponseCode(){
    	return this.responseCode;
    }
    public void setResponseCode(String responseCode){
    	this.responseCode = responseCode;
    }
    
    public String getAuthCode(){
    	return this.authCode;
    }
    public void setAuthCode(String authCode){
    	this.authCode = authCode;
    }
    
    public String getDescription(){
    	return this.description;
    }
    public void setDescription(String description){
    	this.description = description;
    }
    
    public String getMerchantId(){
    	return this.merchantId;
    }
    
    public void setMerchantId(String merchantId) {
    	this.merchantId = merchantId;
    }
	
}
