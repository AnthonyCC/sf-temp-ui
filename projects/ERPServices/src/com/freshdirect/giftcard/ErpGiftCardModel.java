package com.freshdirect.giftcard;

import java.util.Date;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpGiftCardModel extends ErpPaymentMethodModel {

	private static final long	serialVersionUID	= -8372965576631514916L;
	
	private double balance = 0.0; 
	private double originalAmount;
	private String purchaseSaleId;
	private Date purchaseDate;
	
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	private EnumGiftCardStatus status = EnumGiftCardStatus.UNKNOWN;
	
	public double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	private String certificateNumber;	
	
	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public ErpGiftCardModel() {
		super();
	}

	public EnumPaymentMethodType getPaymentMethodType() { return EnumPaymentMethodType.GIFTCARD; }
		
	public EnumCardType getCardType(){return EnumCardType.GCP;}

	public Date getEffectiveDate() { return null; }
    
    public void setEffectiveDate(Date date) { };

	public Date getExpirationDate(){ return null; }
    
    public void setExpirationDate(Date date){ }

	public String getPurchaseSaleId() {
		return purchaseSaleId;
	}

	public void setPurchaseSaleId(String purchaseSaleId) {
		this.purchaseSaleId = purchaseSaleId;
	}

	public EnumGiftCardStatus getStatus() {
		return status;
	}

	public void setStatus(EnumGiftCardStatus status) {
		this.status = status;
	};

	public boolean isRedeemable() {
		return (this.status.equals(EnumGiftCardStatus.ACTIVE) || this.status.equals(EnumGiftCardStatus.UNKNOWN)
				|| this.status.equals(EnumGiftCardStatus.ZERO_BALANCE));
				
	}
	
	public boolean isActive(){
		return this.status.equals(EnumGiftCardStatus.ACTIVE);
	}
	
	public boolean isCancelled(){
		return this.status.equals(EnumGiftCardStatus.INACTIVE);
	}

	public boolean equals(Object o){
	
		if(o instanceof ErpGiftCardModel){
			ErpGiftCardModel model=(ErpGiftCardModel)o;						
			if(this.getCertificateNumber()!=null && (this.getCertificateNumber().equalsIgnoreCase(model.getCertificateNumber()))) return true;			
		}
		return false;
	}


   // only for credit card
	public boolean isAvsCkeckFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	   // only for credit card
	public boolean isBypassAVSCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	   // only for credit card
	public void setAvsCkeckFailed(boolean avsCkeckFailed) {
		// TODO Auto-generated method stub
		
	}

	   // only for credit card
	public void setBypassAVSCheck(boolean bypassAVSCheck) {
		// TODO Auto-generated method stub
		
	}
	
}
