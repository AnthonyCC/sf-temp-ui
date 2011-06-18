package com.freshdirect.customer;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpECheckModel extends ErpPaymentMethodModel {

	private static final long	serialVersionUID	= 4033694179515871080L;
	
	private String bankName;
	private String abaRouteNumber;
	private EnumBankAccountType bankAccountType;
	private boolean isTermsAccepted;
	
	public ErpECheckModel() {
		super();
	}

	public void setAbaRouteNumber(String abaRouteNumber) {  
		this.abaRouteNumber = abaRouteNumber; 
	}

	public String getAbaRouteNumber() { return abaRouteNumber; }
	
	public void setBankName(String bankName) { 
		this.bankName = bankName; 
	}

	public String getBankName() { return bankName; }

	public void setBankAccountType(EnumBankAccountType bankAccountType) { 
		this.bankAccountType = bankAccountType;
	}
	
	public EnumBankAccountType getBankAccountType() {return bankAccountType;}

	public EnumPaymentMethodType getPaymentMethodType() { return EnumPaymentMethodType.ECHECK; }
		
	public EnumCardType getCardType(){return EnumCardType.ECP;}

	public void setIsTermsAccepted(boolean isTermsAccepted) { this.isTermsAccepted = isTermsAccepted; }
	
	public boolean getIsTermsAccepted() { return isTermsAccepted; }
	
	//Only applicable to Gift card - Begin
	public String getCertificateNumber() {return null;}

	public void setCertificateNumber(String certificateNumber) { }

	public double getBalance() { return 0.0; }

	public void setBalance(double balance) { }

	public EnumGiftCardStatus getStatus() { return null; }

	public void setStatus(EnumGiftCardStatus status) { }
	
	public boolean isRedeemable(){ return false; }
	//Only applicable to Gift card - End

	
 
	// for credit card only
	public boolean isAvsCkeckFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	// for credit card only
	public boolean isBypassAVSCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	// for credit card only
	public void setAvsCkeckFailed(boolean avsCkeckFailed) {
		// TODO Auto-generated method stub		
	}

	// for credit card only
	public void setBypassAVSCheck(boolean bypassAVSCheck) {
		// TODO Auto-generated method stub		
	}

}
