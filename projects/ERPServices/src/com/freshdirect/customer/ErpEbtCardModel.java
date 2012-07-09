package com.freshdirect.customer;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpEbtCardModel extends ErpPaymentMethodModel {

	private EnumCardType cardType;
	
	public EnumCardType getCardType() {
		return cardType;
	}

	public void setCardType(EnumCardType cardType) {
		this.cardType = cardType;
	}

	public EnumPaymentMethodType getPaymentMethodType() {
		return EnumPaymentMethodType.EBT;
	}
	
	//Only applicable to Credit card - Begin
	public boolean isAvsCkeckFailed() {	return false;	}
	
	public boolean isBypassAVSCheck() {	return false;	}
	
	public void setAvsCkeckFailed(boolean avsCkeckFailed) {	}

	public void setBypassAVSCheck(boolean bypassAVSCheck) {	}
	//Only applicable to Credit card - End
	
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
