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
import com.freshdirect.giftcard.EnumGiftCardStatus;
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

	private boolean avsCkeckFailed;
	public boolean isAvsCkeckFailed() {
		return avsCkeckFailed;
	}

	public void setAvsCkeckFailed(boolean avsCkeckFailed) {
		this.avsCkeckFailed = avsCkeckFailed;
	}

	public boolean isBypassAVSCheck() {
		return bypassAVSCheck;
	}

	public void setBypassAVSCheck(boolean bypassAVSCheck) {
		this.bypassAVSCheck = bypassAVSCheck;
	}

	private boolean bypassAVSCheck;
	
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
	
	//Only applicable to Gift card - Begin
	public String getCertificateNumber() {return null;}

	public void setCertificateNumber(String certificateNumber) { }

	public double getBalance() { return 0.0; }

	public void setBalance(double balance) { }

	public EnumGiftCardStatus getStatus() { return null; }

	public void setStatus(EnumGiftCardStatus status) { }
	
	public boolean isRedeemable(){ return false; }
	//Only applicable to Gift card - End


}


