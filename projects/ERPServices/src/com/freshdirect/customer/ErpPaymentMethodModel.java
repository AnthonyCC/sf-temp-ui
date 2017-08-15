package com.freshdirect.customer;

import java.util.Date;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.StringUtil;
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

	private String csv;

	private String profileID="";
	private String accountNumLast4;
	private String bestNumberForBillingInquiries;

	private String eWalletID="";
	private String vendorEWalletID="";
	private String eWalletTrxnId="";
	private String emailID="";
	private String deviceId;
	private boolean isDebitCard;
	private boolean isValidCardForDefault;
	
	/**
	 * @return the trxnId
	 */
	public String geteWalletTrxnId() {
		return eWalletTrxnId;
	}

	/**
	 * @param trxnId the trxnId to set
	 */
	public void seteWalletTrxnId(String trxnId) {
		this.eWalletTrxnId = trxnId;
	}

	public String getAccountNumLast4() {
		return accountNumLast4;
	}

	public void setAccountNumLast4(String accountNumLast4) {
		this.accountNumLast4 = accountNumLast4;
	}

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
		if(StringUtil.isEmpty(accountNumLast4) && !StringUtil.isEmpty(accountNumber)) {
			int hideLength = this.accountNumber.length() - 4;
			StringBuffer buf = new StringBuffer(this.accountNumber);
			if (hideLength > 0) {
				for (int i = 0; i < hideLength; i++) {
					buf.setCharAt(i, 'X');
				}
			}
			accountNumLast4=buf.toString();
		}
    	return accountNumLast4;
	}

	/**
	 * get credit card number, displaying only the last five digits
	 *
	 * @return String number
	 */
	public String getDisplayableAccountNumber() {

		
		return accountNumLast4;
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
		return EnumPaymentMethodType.GIFTCARD.equals(getPaymentMethodType());
	}
    public void setCVV(String val) {
    	this.csv=val;
    }

	public String getCVV() {
		return csv;
	}
	public String getProfileID() {
		return profileID;
	}
	 public void setProfileID(String profileID) {
	    	this.profileID=profileID;
	 }

	public String getBestNumberForBillingInquiries() {
		return bestNumberForBillingInquiries;
	}

	public void setBestNumberForBillingInquiries(String bestNumberForBillingInquiries) {
		this.bestNumberForBillingInquiries = bestNumberForBillingInquiries;
	}

	/**
	 * @return the eWalletID
	 */
	@Override
	public String geteWalletID() {
		return eWalletID;
	}

	/**
	 * @param eWalletID the eWalletID to set
	 */
	@Override
	public void seteWalletID(String eWalletID) {
		this.eWalletID = eWalletID;
	}

	/**
	 * @return the vendorEWalletID
	 */
	@Override
	public String getVendorEWalletID() {
		return vendorEWalletID;
	}

	/**
	 * @param vendorEWalletID the vendorEWalletID to set
	 */
	@Override
	public void setVendorEWalletID(String vendorEWalletID) {
		this.vendorEWalletID = vendorEWalletID;
	}

	/**
	 * @return
	 */
	public String getEmailID() {
		return emailID;
	}

	/**
	 * @param emailID
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	/* (non-Javadoc)
	 * @see com.freshdirect.customer.ErpPaymentMethodI#setDeviceId(java.lang.String)
	 */
	@Override
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.customer.ErpPaymentMethodI#getDeviceId()
	 */
	@Override
	public String getDeviceId() {
		return this.deviceId;
	}

	public boolean isDebitCard() {
		return isDebitCard;
	}

	public void setDebitCard(boolean isDebitCard) {
		this.isDebitCard = isDebitCard;
	}

	public void setIsValidCardForDefault(boolean isValidCardForDefault){
		this.isValidCardForDefault = isValidCardForDefault;
	}
}
