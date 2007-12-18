/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.EnumPaymentMethodType;

import java.util.Date;

/**
 * ErpCreditCard model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCreditCardModel extends ErpPaymentMethodModel {

	private Date expirationDate;
	private EnumCardType cardType;

	public ErpCreditCardModel() {
		super();
	}

	/**
	 * get expiration date for this credit card
	 *
	 * @return java.util.Date expirationDate
	 */
	public Date getExpirationDate(){
		return this.expirationDate;
	}

	/**
	 * set expiration date for this credit card
	 *
	 * @param java.util.Date expirationDate
	 */
	public void setExpirationDate(Date expirationDate){
		this.expirationDate = expirationDate;
	}

	/**
	 * get type of this credit card ie VISA, MASTERCARD, DIS
	 *
	 * @return String type
	 */
	public EnumCardType getCardType(){
		return this.cardType;
	}

	/**
	 * set type for this credit card
	 *
	 * @param String type
	 */
	public void setCardType(EnumCardType cardType){
		this.cardType = cardType;
	}
	
	public EnumPaymentMethodType getPaymentMethodType() {
		return EnumPaymentMethodType.CREDITCARD;
	}
}


