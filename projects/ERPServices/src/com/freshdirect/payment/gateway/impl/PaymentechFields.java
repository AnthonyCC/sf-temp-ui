package com.freshdirect.payment.gateway.impl;

final class PaymentechFields implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 443480563412780872L;
	private PaymentechFields() {
	}
	static enum ProfileRequest implements java.io.Serializable {
		CustomerBin,
		CustomerMerchantID,
		CustomerName,
		CustomerRefNum,
		CustomerAddress1,
		CustomerAddress2,
		CustomerCity,
		CustomerState,
		CustomerZIP,
		CustomerEmail,
		CustomerPhone,
		CustomerCountryCode,
		CustomerProfileAction,
		CustomerProfileOrderOverrideInd,
		CustomerProfileFromOrderInd,
		OrderDefaultDescription,
		OrderDefaultAmount,
		CustomerAccountType,
		Status,
		CCAccountNum,
		CCExpireDate,
		ECPAccountDDA,
		ECPAccountType,
		ECPAccountRT,
		ECPBankPmtDlv;
		
	}
	static enum ProfileResponse implements java.io.Serializable {
		CustomerBin,
		CustomerMerchantID,
		CustomerName,
		CustomerRefNum,
		CustomerProfileAction,
		ProfileProcStatus,
		CustomerProfileMessage,
		CustomerAddress1,
		CustomerAddress2,
		CustomerCity,
		CustomerState,
		CustomerZIP,
		CustomerEmail,
		CustomerPhone,
		CustomerCountryCode,
		CustomerProfileOrderOverrideInd,
		CustomerProfileFromOrderInd,
		OrderDefaultDescription,
		OrderDefaultAmount,
		CustomerAccountType,
		Status,
		CCAccountNum,
		CCExpireDate,
		ECPAccountDDA,
		ECPAccountType,
		ECPAccountRT,
		ECPBankPmtDlv,
		RespTime;
	}
	static enum NewOrderRequest implements java.io.Serializable {
		IndustryType,
		MessageType,
		MerchantID,
		BIN,
		OrderID,
		TerminalID,
		CardBrand,
		AccountNum,
		CurrencyCode,
		CurrencyExponent,
		Amount,
		Exp,
		AVSname,
		AVSaddress1,
		AVSaddress2,
		AVScity,
		AVSstate,
		AVSzip,
		Comments,
		ShippingRef,
		CardSecValInd,
		CardSecVal,
		CustomerRefNum,
		TxRefNum,
		TxRefIdx,
		OnlineReversalInd,
		CheckDDA,
		BCRtNum,
		BankAccountType;
		
	}
	
	static enum NewOrderResponse implements java.io.Serializable {
		IndustryType,
		MessageType,
		MerchantID,
		TerminalID,
		CardBrand,
		AccountNum,
		OrderID,
		TxRefNum,
		TxRefIdx,
		ProcStatus,
		ApprovalStatus,
		RespCode,
		HostRespCode,
		AVSRespCode,
		CVV2RespCode,
		AuthCode,
		StatusMsg,
		RespMsg,
		CustomerRefNum,
		CustomerName,
		ProfileProcStatus,
		CustomerProfileMessage,
		RespTime,
		RequestedAmount,
		RedeemedAmount,
		RemainingBalance,
		IsoCountryCode,
		OnlineReversalInd,
		CheckDDA,
		BCRtNum,
		BankAccountType;
	}
}
