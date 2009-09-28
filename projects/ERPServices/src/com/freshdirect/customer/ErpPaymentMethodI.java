/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.util.Date;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * ErpPaymentMethod interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-interface
 */
public interface ErpPaymentMethodI extends AddressI {

    public EnumPaymentMethodType getPaymentMethodType();

    public String getCustomerId();

    public void setCustomerId(String customerId);

    public String getName();

    public void setName(String name);

    public String getAccountNumber();
    
    public void setAccountNumber(String number);

    public Date getExpirationDate();
    
    public void setExpirationDate(Date expirationDate);
    
	public EnumCardType getCardType();
    
	public void setCardType(EnumCardType cardType);

	public void setAbaRouteNumber(String abaRouteNumber);

	public String getAbaRouteNumber();

	public void setBankName(String bankName);

	public String getBankName();
	
	public void setBankAccountType(EnumBankAccountType bankAccountType);

	public EnumBankAccountType getBankAccountType();

	public ContactAddressModel getAddress();
	
	public String getAddress1();
	
	public String getAddress2();
		
	public String getApartment();
	
	public String getCity();
		
	public String getState();
	
	public String getZipCode();
		
	public String getCountry();
		
	public String getMaskedAccountNumber();
	
	public void setBillingRef(String billingRef);
	
	public String getBillingRef();
	
	public EnumPaymentType getPaymentType();
	
	public void setPaymentType(EnumPaymentType paymentType);
	
	public void setReferencedOrder(String orderId);
	
	public String getReferencedOrder();
			
	public void setIsTermsAccepted(boolean isTermsAccepted);
	
	public boolean getIsTermsAccepted();
	
	public String getCertificateNumber() ;

	public void setCertificateNumber(String certificateNumber) ;

	public double getBalance() ;

	public void setBalance(double balance) ;

	public EnumGiftCardStatus getStatus();

	public void setStatus(EnumGiftCardStatus status);
	
	public boolean isRedeemable();
	
	public boolean isGiftCard();
	
}

