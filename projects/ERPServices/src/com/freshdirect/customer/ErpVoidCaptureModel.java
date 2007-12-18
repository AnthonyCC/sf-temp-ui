package com.freshdirect.customer;

public class ErpVoidCaptureModel extends ErpPaymentModel {
	
	private String sequenceNumber;
	private String authCode;
	private String merchantId;

	public ErpVoidCaptureModel() {
		super(EnumTransactionType.VOID_CAPTURE);
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String string) {
		sequenceNumber = string;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String string) {
		authCode = string;
	}
	
	public String getMerchantId(){
		return this.merchantId;
	}
	
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

}
