package com.freshdirect.payment.gateway.ejb;

import java.util.Date;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.payment.gateway.BankAccountType;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.TransactionType;

public class FDGatewayActivityLogModel implements java.io.Serializable{
	
	private GatewayType gatewayType;
	private TransactionType transactionType;
	private boolean isRequestProcessed;
	private boolean isApproved;
	private boolean isDeclined;
	private boolean isProcessingError;
	private String statusCode;
	private String statusMsg;
	private String authCode;
	private boolean isAVSMatch;
	private boolean isCVVMatch;
	private String avsResponse;
	private String cvvResponse;
	private String responseCode;
	private String responseCodeAlt;
	private String customerId;
	private String profileId;
	private String gatewayOrderID;
	private String txRefNum;
	private String txRefIdx;
	private double amount;
	private String merchant;
	private CreditCardType cardType;
	private String accountNumLast4;
	private Date expirationDate;
	private String customerName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipCode;
	private String countryCode;
	private BankAccountType bankAccountType;
	private PaymentMethodType paymentType;
	private String eWalletId;
	private String eWalletTxId;
	private EnumEStoreId eStoreId;
	private String deviceId;
	
	/**
	 * @return the eWalletTxId
	 */
	public String geteWalletTxId() {
		return eWalletTxId;
	}
	/**
	 * @param eWalletTxId the eWalletTxId to set
	 */
	public void seteWalletTxId(String eWalletTxId) {
		this.eWalletTxId = eWalletTxId;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public boolean isRequestProcessed() {
		return isRequestProcessed;
	}
	public void setRequestProcessed(boolean isRequestProcessed) {
		this.isRequestProcessed = isRequestProcessed;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	public boolean isDeclined() {
		return isDeclined;
	}
	public void setDeclined(boolean isDeclined) {
		this.isDeclined = isDeclined;
	}
	public boolean isProcessingError() {
		return isProcessingError;
	}
	public void setProcessingError(boolean isProcessingError) {
		this.isProcessingError = isProcessingError;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public boolean isAVSMatch() {
		return isAVSMatch;
	}
	public void setAVSMatch(boolean isAVSMatch) {
		this.isAVSMatch = isAVSMatch;
	}
	public boolean isCVVMatch() {
		return isCVVMatch;
	}
	public void setCVVMatch(boolean isCVVMatch) {
		this.isCVVMatch = isCVVMatch;
	}
	public String getAvsResponse() {
		return avsResponse;
	}
	public void setAvsResponse(String avsResponse) {
		this.avsResponse = avsResponse;
	}
	public String getCvvResponse() {
		return cvvResponse;
	}
	public void setCvvResponse(String cvvResponse) {
		this.cvvResponse = cvvResponse;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseCodeAlt() {
		return responseCodeAlt;
	}
	public void setResponseCodeAlt(String responseCodeAlt) {
		this.responseCodeAlt = responseCodeAlt;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getGatewayOrderID() {
		return gatewayOrderID;
	}
	public void setGatewayOrderID(String gatewayOrderID) {
		this.gatewayOrderID = gatewayOrderID;
	}
	public String getTxRefNum() {
		return txRefNum;
	}
	public void setTxRefNum(String txRefNum) {
		this.txRefNum = txRefNum;
	}
	public String getTxRefIdx() {
		return txRefIdx;
	}
	public void setTxRefIdx(String txRefIdx) {
		this.txRefIdx = txRefIdx;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public CreditCardType getCardType() {
		return cardType;
	}
	public void setCardType(CreditCardType cardType) {
		this.cardType = cardType;
	}
	public String getAccountNumLast4() {
		return accountNumLast4;
	}
	public void setAccountNumLast4(String accountNumLast4) {
		this.accountNumLast4 = accountNumLast4;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public BankAccountType getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(BankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public GatewayType getGatewayType() {
		return gatewayType;
	}
	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}	
	public PaymentMethodType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentMethodType paymentType) {
		this.paymentType = paymentType;
	}
	public String geteWalletId() {
		return eWalletId;
	}
	public void seteWalletId(String eWalletId) {
		this.eWalletId = eWalletId;
	}
	
	public void setEStoreId(EnumEStoreId eStoreId) {
		this.eStoreId=eStoreId;
	}
	
	public EnumEStoreId getEStoreId() {
		return eStoreId;
	}
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
}
