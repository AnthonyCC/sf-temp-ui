package com.freshdirect.customer;

import java.util.Date;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

public abstract class ErpPaymentMethodModel extends ModelSupport implements ErpPaymentMethodI {

	private static final long	serialVersionUID	= -3496284200065106331L;
	
	private String customerId;	
	private String name;
	private String accountNumber;

	private ContactAddressModel address;
	
	//!!! these donot belong here this is a side effect of not having a ErpPaymentInfoModel should be refactored (kn).
	private String billingRef;
	private EnumPaymentType paymentType = EnumPaymentType.REGULAR;
	private String referencedOrder;

	public ErpPaymentMethodModel() {
		this.address = new ContactAddressModel();
	}

	/**
	 * get expiration date for this credit card
	 *
	 * @return java.util.Date expirationDate
	 */
	public Date getExpirationDate(){return null;}

	/**
	 * set expiration date for this credit card
	 *
	 * @param java.util.Date expirationDate
	 */
	public void setExpirationDate(Date expirationDate){}

	/**
	 * get type of this credit card ie VISA, MASTERCARD, DIS
	 *
	 * @return String type
	 */
	public EnumCardType getCardType(){return null;}

	/**
	 * set type for this credit card
	 *
	 * @param String type
	 */
	public void setCardType(EnumCardType cardType){}
	
	
	public AddressInfo getAddressInfo(){
		return this.address.getAddressInfo();
	}
	

	public ContactAddressModel getAddress() { 
		return this.address; 
	}
	
	public void setAddress(ContactAddressModel address) {
		this.address = address;
	}

	/**
	 * get first line of this address
	 *
	 * @return String address1
	 */
	public String getAddress1(){ return address.getAddress1(); }

	/**
	 * set first line of this address
	 *
	 * @param String address1
	 */
	public void setAddress1(String address1){ this.address.setAddress1(address1); }

	/**
	 * get second line of this address
	 *
	 * @return String address2
	 */
	public String getAddress2(){ return address.getAddress2(); }

	/**
	 * set second line for this address
	 *
	 * @param String address2
	 */
	public void setAddress2(String address2){ this.address.setAddress2(address2); }

	/**
	 * get third line for this address
	 *
	 * @return String address3
	 */
	public String getApartment(){ return address.getApartment(); }

	/**
	 * set third address for this address
	 *
	 * @param String address3
	 */
	public void setApartment(String apartment){ this.address.setApartment(apartment); }

	/**
	 * get city for this address
	 *
	 * @return String city
	 */
	public String getCity(){ return address.getCity(); }

	/**
	 * set city for this address
	 *
	 * @param String city
	 */
	public void setCity(String city){ this.address.setCity(city); }

	/**
	 * get state for this address
	 *
	 * @return String state
	 */
	public String getState(){ return address.getState(); }

	/**
	 * set state for this address
	 *
	 * @param String state
	 */
	public void setState(String state){ this.address.setState(state); }

	public String getZipCode(){ return address.getZipCode(); }

	public void setZipCode(String zip){ this.address.setZipCode(zip); }

	/**
	 * get country for this address
	 *
	 * @return String country
	 */
	public String getCountry(){ return address.getCountry(); }

	/**
	 * set country for this address
	 *
	 * @param String country
	 */
	public void setCountry(String country){ this.address.setCountry(country); }

	/**
	 * get name on this payment method
	 *
	 * @return String customer id
	 */
	public String getCustomerId() {
		return this.customerId;
	}

	/**
	 * set name on this payment method
	 *
	 * @param String name
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * get name on this payment method
	 *
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * set name on this payment method
	 *
	 * @param String name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * set account number of this payment method
	 *
	 * @param String number
	 */
	public void setAccountNumber(String number) {
		this.accountNumber = number;
	}

	/**
	 * get account number of this payment method
	 *
	 * @return String number
	 */
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public String getMaskedAccountNumber() {
    	int numDisplayedDigits = Math.min(accountNumber.length(), 4); 
    	int numMaskedDigits = 10 - numDisplayedDigits;
    	return  "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".substring(0, numMaskedDigits) + accountNumber.substring(accountNumber.length()-numDisplayedDigits);  
		//int pmLen = this.accountNumber.length()-4;
		//return "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".substring(0,pmLen) + this.accountNumber.substring(pmLen);
	}

	/**
	 * get credit card number, displaying only the last five digits
	 *
	 * @return String number
	 */
	public String getDisplayableAccountNumber() {
		int hideLength = this.accountNumber.length() - 5;
		StringBuffer buf = new StringBuffer(this.accountNumber);
		if (hideLength > 0) {
			for (int i = 0; i < hideLength; i++) {
				buf.setCharAt(i, 'x');
			}
		}
		return buf.toString();
	}	

	public String getBillingRef() {
		return billingRef;
	}

	public void setBillingRef(String billingRef) {
		this.billingRef = billingRef;
	}
	
	public EnumPaymentType getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(EnumPaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	public void setReferencedOrder(String referencedOrder){
		this.referencedOrder = referencedOrder;
	}
	
	public String getReferencedOrder(){
		return this.referencedOrder;
	}
	
	public String getBankName() {return null;}
	
	public void setBankName(String bankName) {}

	public String getAbaRouteNumber() {return null;}

	public void setAbaRouteNumber(String abaRouteNumber) {}

	public EnumBankAccountType getBankAccountType() {return null;}

	public void setBankAccountType(EnumBankAccountType bankAccountType) {}

	public EnumPaymentMethodType getPaymentMethodType() {return null;}

	public void setIsTermsAccepted(boolean isTermsAccepted) {}
	
	public boolean getIsTermsAccepted() { return false; }
	
	public boolean isGiftCard() {
		return this.getPaymentMethodType().equals(EnumPaymentMethodType.GIFTCARD);
	}
}
