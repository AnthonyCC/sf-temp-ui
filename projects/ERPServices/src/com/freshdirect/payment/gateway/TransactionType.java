package com.freshdirect.payment.gateway;

public enum TransactionType implements java.io.Serializable {
	
	AUTHORIZE,
	CAPTURE,
	REVERSE_AUTHORIZE,
	VOID_CAPTURE,
	CC_VERIFY,/* Credit card Verification*/
	ACH_VERIFY,/*ECheck Verification*/
	CASHBACK,
	ADD_PROFILE,
	UPDATE_PROFILE,
	DELETE_PROFILE,
	GET_PROFILE;
}
