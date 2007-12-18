/*
 * ErpCashbackModel.java
 *
 * Created on January 14, 2002, 9:32 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */

public class ErpCashbackModel extends ErpPaymentModel{
	
	private int returnCode;
	private String responseCode;
	private String authCode;
	private String sequenceNumber;
	private String description;
	private String avs;
	
	/** Creates new ErpCashbackModel */
    public ErpCashbackModel() {
		super(EnumTransactionType.CASHBACK);
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
	
	public String getResponseCode(){
		return this.responseCode;
	}
	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

}
