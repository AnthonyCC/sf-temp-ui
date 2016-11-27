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
    public String getTrasactionRefIndex(){
		return this.trasactionRefIndex;
	}
	public void setTrasactionRefIndex(String trasactionRefIndex){
		this.trasactionRefIndex = trasactionRefIndex;
	}
	private String trasactionRefIndex="";
	
	private String procResponseCode;

	public String getProcResponseCode() {
		return procResponseCode;
	}

	public void setProcResponseCode(String procResponseCode) {
		this.procResponseCode = procResponseCode;
	}
	
	private String ewalletTxId;

	public String getEwalletTxId() {
		return ewalletTxId;
	}

	public void setEwalletTxId(String ewalletTxId) {
		this.ewalletTxId = ewalletTxId;
	}
}
