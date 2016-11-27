package com.freshdirect.giftcard;

import java.util.Date;

import com.freshdirect.customer.ErpPaymentMethodI;

public interface ErpGiftCardI extends ErpPaymentMethodI{
	
	public double getOriginalAmount();

	public void setOriginalAmount(double originalAmount);

	public String getCertificateNumber() ;

	public void setCertificateNumber(String certificateNumber) ;

	public double getBalance() ;

	public void setBalance(double balance) ;
	
	public Date getEffectiveDate();
    
    public void setEffectiveDate(Date date);

	public String getPurchaseSaleId() ;

	public void setPurchaseSaleId(String purchaseSaleId);
	
	public EnumGiftCardStatus getStatus();

	public void setStatus(EnumGiftCardStatus status);
	
	public boolean isRedeemable();
	

}
