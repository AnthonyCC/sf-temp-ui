/*
 * 
 * ErpInvoicedCreditModel.java
 * Date: Jul 26, 2002 Time: 5:26:27 PM
 */
package com.freshdirect.customer;

/**
 * 
 * @author knadeem
 */
public class ErpInvoicedCreditModel extends ErpAppliedCreditModel {
	
	private String originalCreditId;
	
	public ErpInvoicedCreditModel (){
		super();
	}
	
	public String getOriginalCreditId(){
		return this.originalCreditId;
	}
	
	public void setOriginalCreditId(String originalCreditId){
		this.originalCreditId = originalCreditId;
	}

}
