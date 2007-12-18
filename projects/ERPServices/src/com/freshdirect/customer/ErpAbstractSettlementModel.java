/*
 * Created on Apr 6, 2005
 *
 */
package com.freshdirect.customer;

public abstract class ErpAbstractSettlementModel extends ErpPaymentModel {
	private String sequenceNumber;
	private EnumPaymentResponse responseCode;
	private String description;
	private String authCode;
	    
	public ErpAbstractSettlementModel(EnumTransactionType transType) {
        super(transType);
    }
	
    public String getSequenceNumber(){
    	return this.sequenceNumber;
    }
    public void setSequenceNumber(String sequenceNumber){
    	this.sequenceNumber = sequenceNumber;
    }

	public EnumPaymentResponse getResponseCode(){
		return this.responseCode;
	}
	public void setResponseCode(EnumPaymentResponse responseCode){
		this.responseCode = responseCode;
	}

	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getAuthCode(){
		return this.authCode;
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

}
