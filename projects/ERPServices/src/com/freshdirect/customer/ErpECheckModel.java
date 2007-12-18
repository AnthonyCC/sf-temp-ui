package com.freshdirect.customer;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpECheckModel extends ErpPaymentMethodModel {


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
	
}
