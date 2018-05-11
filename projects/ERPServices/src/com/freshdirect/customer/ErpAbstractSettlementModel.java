/*
 * Created on Apr 6, 2005
 *
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.PrimaryKey;

public abstract class ErpAbstractSettlementModel extends ErpPaymentModel {
	private String sequenceNumber;
	private EnumPaymentResponse responseCode;
	private String description;
	private String authCode;
	private String processorTrxnId;
	private String ewallet_tx_id;
	    
	public String getEwallet_tx_id() {
		return ewallet_tx_id;
	}

	public void setEwallet_tx_id(String ewallet_tx_id) {
		this.ewallet_tx_id = ewallet_tx_id;
	}

	public String getProcessorTrxnId() {
		return processorTrxnId;
	}

	public void setProcessorTrxnId(String processorTrxnId) {
		this.processorTrxnId = processorTrxnId;
	}

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
	
	public void setPK(PrimaryKey pk) {
		if (pk != null ) {
			super.setPK(pk);
		}
	}
	public void setId(String id) {
		if (id != null ) {
			super.setId(id);
		}
	}
}
