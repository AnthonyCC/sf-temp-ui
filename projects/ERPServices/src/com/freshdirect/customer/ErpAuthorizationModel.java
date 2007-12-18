package com.freshdirect.customer;


public class ErpAuthorizationModel extends ErpPaymentModel {

	private int returnCode;
	private EnumPaymentResponse responseCode;
	private String procResponseCode;
	private String merchantId;
	private String authCode;
	private String sequenceNumber;
	private String description;
	private String avs;
	
	/** 
	 * Creates new ErpAuthorizeTransaction 
	 */
    public ErpAuthorizationModel() {
		super(EnumTransactionType.AUTHORIZATION);
    }
	
	public String getAuthCode(){
		return this.authCode;
	}
	public void setAuthCode(String authCode){
		this.authCode = authCode;
	}
	
	public String getSequenceNumber(){
		return this.sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}
	
	public String getMerchantId(){
		return this.merchantId;
	}
	
	public void setMerchantId(String merchantId){
		this.merchantId = merchantId;
	}
	
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getAvs(){
		return this.avs;
	}
	public void setAvs(String avs){
		this.avs = avs;
	}
	
	public int getReturnCode(){
		return this.returnCode;
	}
	public void setReturnCode(int returnCode){
		this.returnCode = returnCode;
	}
	
	public EnumPaymentResponse getResponseCode(){
		return this.responseCode;
	}
	public void setResponseCode(EnumPaymentResponse responseCode){
		this.responseCode = responseCode;
	}

	public String getProcResponseCode(){
		return this.procResponseCode;
	}
	public void setProcResponseCode(String procResponseCode){
		this.procResponseCode = procResponseCode;
	}

	public boolean isApproved() {
		return EnumPaymentResponse.APPROVED.equals(this.responseCode);
	}
	
	public boolean hasAvsMatched(){
		return "Y".equalsIgnoreCase(this.avs);
	}
	
}
