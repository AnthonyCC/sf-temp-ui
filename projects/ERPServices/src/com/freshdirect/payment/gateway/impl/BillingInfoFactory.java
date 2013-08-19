package com.freshdirect.payment.gateway.impl;

import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethod;

public class BillingInfoFactory {
	
	public static BillingInfo getBillingInfo(Merchant merchant,PaymentMethod pymtMethod) {
		return new BillingInfoImpl(merchant,pymtMethod);
	}

}
