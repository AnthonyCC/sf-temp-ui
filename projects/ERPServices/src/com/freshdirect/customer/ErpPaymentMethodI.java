package com.freshdirect.customer;

import java.util.Date;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.framework.core.PrimaryKey;
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
				
	public boolean isAvsCkeckFailed();

	public void setAvsCkeckFailed(boolean avsCkeckFailed);
	
	public PrimaryKey getPK();
	
	public boolean isBypassAVSCheck();
	
	public void setBypassAVSCheck(boolean bypassAVSCheck);
	
	/*public boolean isCVVVerified();
	
	public void setCVVVerified(boolean cvvVerified);*/
	
	public void setCVV(String val);
	
	public String getCVV();
	
	public String getProfileID();
	
	public void setProfileID(String profileID);
	
	/**
	 * @return emailID
	 */
	public String getEmailID();
	
	/**
	 * @param emailID
	 */
	public void setEmailID(String emailID);
	
	
	public final String DEFAULT_ACCOUNT_NUMBER="1111111111111111";
	
	String getBestNumberForBillingInquiries();
	
	void setBestNumberForBillingInquiries(String phoneNumber);

	/**
	 * @return the eWalletID
	 */
	public String geteWalletID();

	/**
	 * @param eWalletID the eWalletID to set
	 */
	public void seteWalletID(String eWalletID);

	/**
	 * @return the vendorEWalletID
	 */
	public String getVendorEWalletID();

	/**
	 * @param vendorEWalletID the vendorEWalletID to set
	 */
	public void setVendorEWalletID(String vendorEWalletID);
	
	
	/**
	 * @return the trxnId
	 */
	public String geteWalletTrxnId();

	/**
	 * @param trxnId the trxnId to set
	 */
	public void seteWalletTrxnId(String trxnId);
	
	public void setDeviceId(String deviceId);
	public String getDeviceId();
	public boolean isDebitCard();
	public void setDebitCard(boolean isDebitCard);
}

