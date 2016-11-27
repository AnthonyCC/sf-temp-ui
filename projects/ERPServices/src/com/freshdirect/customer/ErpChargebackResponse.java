/*
 * ErpChargebackResponse.java
 *
 * Created on May 07, 2002, 2:39 PM
 */
package com.freshdirect.customer;

import java.io.Serializable;

import com.freshdirect.common.customer.EnumCardType;

/**
 *
 * @author  knadeem
 * @version
 */


public class ErpChargebackResponse implements Serializable{
	
	private String invoiceNumber;
	private EnumCardType cardType;
	
	public ErpChargebackResponse(String invoiceNumber, EnumCardType cardType){
		this.invoiceNumber = invoiceNumber;
		this.cardType = cardType;
	}
	
	public String getInvoiceNumber(){
		return this.invoiceNumber;
	}
	
	public EnumCardType getCardType(){
		return this.cardType;
	}
}
