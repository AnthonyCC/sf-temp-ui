package com.freshdirect.payment.gateway.impl;

import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.ECheck;

public class PaymentMethodFactory {
	
	public static CreditCard getCreditCard() {
		return new CreditCardImpl();
	}

	public static ECheck getECheck() {
		return new ECheckImpl();
	}
}
