package com.freshdirect.payment.gateway;

import java.util.Date;

public interface CreditCard extends PaymentMethod {
	public CreditCardType getCreditCardType();
	public void setCreditCardType(CreditCardType ccType);
	public Date getExpirationDate();
	public void setExpirationDate(Date date);
	public String getCVV();
	public void setCVV(String cvv);
}
