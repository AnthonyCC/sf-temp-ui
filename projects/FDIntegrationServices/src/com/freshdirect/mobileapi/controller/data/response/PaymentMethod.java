package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.customer.ErpPaymentMethodI;

public class PaymentMethod {

    private String id;

    private BillingAddress billingAddress;

    private String bankName;

    private String abaRoutingNumber;

    private String maskedAccountNumber;

    private String cardType;
    
    private String name;
    
    private String ewalletType;
    
    private String emailId;
    
    private String tokenType;
    
    private String value;
    
    private boolean isDebitCard;
    
    public PaymentMethod(com.freshdirect.mobileapi.model.PaymentMethod paymentMethod) {
        this.id = paymentMethod.getId();
        this.billingAddress = new BillingAddress(paymentMethod);
        this.maskedAccountNumber = paymentMethod.getMaskedAccountNumber();
        this.name = paymentMethod.getName();
        this.bankName = paymentMethod.getBankName();
        this.abaRoutingNumber = paymentMethod.getAbaRouteNumber();
        this.cardType = paymentMethod.getCardType();
        this.ewalletType = paymentMethod.geteWalletID();
        this.emailId = paymentMethod.getEmailId();
        this.value = paymentMethod.getProfileId();
        this.tokenType = paymentMethod.getTokenType();
        this.isDebitCard = paymentMethod.isDebitCard();
    }

    public PaymentMethod(com.freshdirect.mobileapi.model.PaymentMethod paymentMethod,String type) {
    	this.id = paymentMethod.getId();
        this.name = paymentMethod.getName();
        this.ewalletType = paymentMethod.geteWalletID();
        this.emailId = paymentMethod.getEmailId();
        this.tokenType = paymentMethod.getTokenType();
        if(this.id != null && this.id.trim().length() > 0 ){	// Do not return the Vault Token
        	this.value ="XXXXXX";
        }else{
        	this.value = paymentMethod.getProfileId(); // Set Client Token
        }
        this.isDebitCard = paymentMethod.isDebitCard();
    }
   
    
    public String getBankName() {
        return bankName;
    }

    public String getAbaRoutingNumber() {
        return abaRoutingNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getId() {
        return id;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    public String getName() {
        return name;
    }

	/**
	 * @return the ewalletType
	 */
	public String getEwalletType() {
		return ewalletType;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public boolean isDebitCard() {
		return isDebitCard;
	}
	
}
