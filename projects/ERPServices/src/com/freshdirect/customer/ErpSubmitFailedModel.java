/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpSubmitFailedModel extends ErpTransactionModel {
	
	private String message;

    public ErpSubmitFailedModel() {
		super(EnumTransactionType.SUBMIT_FAILED);
    }
    
    public double getAmount() {
    	return 0;
    }
	
	public String getMessage(){
		return this.message;
	}

	public void setMessage(String message){
		this.message = message;
	}
	
}
