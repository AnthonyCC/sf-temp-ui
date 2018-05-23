/*
 * 
 * ErpInvoicedCreditModel.java
 * Date: Jul 26, 2002 Time: 5:26:27 PM
 */
package com.freshdirect.customer;

import com.freshdirect.affiliate.ErpAffiliate;

/**
 * 
 * @author knadeem
 */
public class ErpInvoicedCreditModel extends ErpAppliedCreditModel {
	

	private static final long serialVersionUID = -1058781054972446148L;
	private String originalCreditId;
	
	public ErpInvoicedCreditModel (){
		super();
	}
	
	public ErpInvoicedCreditModel(ErpAffiliate affiliate) {
		super(affiliate);
	}
	
	public String getOriginalCreditId(){
		return this.originalCreditId;
	}
	
	public void setOriginalCreditId(String originalCreditId){
		this.originalCreditId = originalCreditId;
	}

}
