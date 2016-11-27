package com.freshdirect.payment.gateway.impl;

import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.TransactionType;

public class RequestFactory {
	
	public static Request getRequest(TransactionType transactionType) {
		
		return new RequestImpl(transactionType);
	}
}
