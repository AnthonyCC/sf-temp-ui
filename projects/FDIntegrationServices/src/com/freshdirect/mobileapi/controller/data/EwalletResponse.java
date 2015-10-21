package com.freshdirect.mobileapi.controller.data;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.response.PaymentMethod;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletResponse extends Message{

	private String eWalletStatus;
	private String callbackUrl;
	private String requestToken;
	private String paringToken;
	private String expressCheckout;
	private String reqDatatype;
	private String merchantCheckoutId;
	private String[] allowedCardTypes;
	private String cancelCallback;
	private String suppressShippingEnable;
	private String loyaltyEnabled;
	private String requestBasicCkt;
	private String version;
	private String reqPairing;
	private PaymentMethod paymentMethod;
	private List<EwalletPaymentMethod> paymentMethods;
	private String preCheckoutTxnId;
	private String checkoutTransactionId;
	/**
	 * @return the eWalletStatus
	 */
	public String geteWalletStatus() {
		return eWalletStatus;
	}
	/**
	 * @param eWalletStatus the eWalletStatus to set
	 */
	public void seteWalletStatus(String eWalletStatus) {
		this.eWalletStatus = eWalletStatus;
	}
	/**
	 * @return the callbackUrl
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}
	/**
	 * @param callbackUrl the callbackUrl to set
	 */
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	/**
	 * @return the requestToken
	 */
	public String getRequestToken() {
		return requestToken;
	}
	/**
	 * @param requestToken the requestToken to set
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	/**
	 * @return the paringToken
	 */
	public String getParingToken() {
		return paringToken;
	}
	/**
	 * @param paringToken the paringToken to set
	 */
	public void setParingToken(String paringToken) {
		this.paringToken = paringToken;
	}
	/**
	 * @return the expressCheckout
	 */
	public String getExpressCheckout() {
		return expressCheckout;
	}
	/**
	 * @param expressCheckout the expressCheckout to set
	 */
	public void setExpressCheckout(String expressCheckout) {
		this.expressCheckout = expressCheckout;
	}
	/**
	 * @return the reqDatatype
	 */
	public String getReqDatatype() {
		return reqDatatype;
	}
	/**
	 * @param reqDatatype the reqDatatype to set
	 */
	public void setReqDatatype(String reqDatatype) {
		this.reqDatatype = reqDatatype;
	}
	/**
	 * @return the merchantCheckoutId
	 */
	public String getMerchantCheckoutId() {
		return merchantCheckoutId;
	}
	/**
	 * @param merchantCheckoutId the merchantCheckoutId to set
	 */
	public void setMerchantCheckoutId(String merchantCheckoutId) {
		this.merchantCheckoutId = merchantCheckoutId;
	}
	/**
	 * @return the allowedCardTypes
	 */
	public String[] getAllowedCardTypes() {
		return allowedCardTypes;
	}
	/**
	 * @param allowedCardTypes the allowedCardTypes to set
	 */
	public void setAllowedCardTypes(String[] allowedCardTypes) {
		this.allowedCardTypes = allowedCardTypes;
	}
	/**
	 * @return the cancelCallback
	 */
	public String getCancelCallback() {
		return cancelCallback;
	}
	/**
	 * @param cancelCallback the cancelCallback to set
	 */
	public void setCancelCallback(String cancelCallback) {
		this.cancelCallback = cancelCallback;
	}
	/**
	 * @return the suppressShippingEnable
	 */
	public String getSuppressShippingEnable() {
		return suppressShippingEnable;
	}
	/**
	 * @param suppressShippingEnable the suppressShippingEnable to set
	 */
	public void setSuppressShippingEnable(String suppressShippingEnable) {
		this.suppressShippingEnable = suppressShippingEnable;
	}
	/**
	 * @return the loyaltyEnabled
	 */
	public String getLoyaltyEnabled() {
		return loyaltyEnabled;
	}
	/**
	 * @param loyaltyEnabled the loyaltyEnabled to set
	 */
	public void setLoyaltyEnabled(String loyaltyEnabled) {
		this.loyaltyEnabled = loyaltyEnabled;
	}
	/**
	 * @return the requestBasicCkt
	 */
	public String getRequestBasicCkt() {
		return requestBasicCkt;
	}
	/**
	 * @param requestBasicCkt the requestBasicCkt to set
	 */
	public void setRequestBasicCkt(String requestBasicCkt) {
		this.requestBasicCkt = requestBasicCkt;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the reqPairing
	 */
	public String getReqPairing() {
		return reqPairing;
	}
	/**
	 * @param reqPairing the reqPairing to set
	 */
	public void setReqPairing(String reqPairing) {
		this.reqPairing = reqPairing;
	}
	/**
	 * @return the paymentMethod
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * @return the paymentMethods
	 */
	public List<EwalletPaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}
	/**
	 * @param paymentMethods the paymentMethods to set
	 */
	public void setPaymentMethods(List<EwalletPaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	/**
	 * @return the preCheckoutTxnId
	 */
	public String getPreCheckoutTxnId() {
		return preCheckoutTxnId;
	}
	/**
	 * @param preCheckoutTxnId the preCheckoutTxnId to set
	 */
	public void setPreCheckoutTxnId(String preCheckoutTxnId) {
		this.preCheckoutTxnId = preCheckoutTxnId;
	}
	/**
	 * @return the checkoutTransactionId
	 */
	public String getCheckoutTransactionId() {
		return checkoutTransactionId;
	}
	/**
	 * @param checkoutTransactionId the checkoutTransactionId to set
	 */
	public void setCheckoutTransactionId(String checkoutTransactionId) {
		this.checkoutTransactionId = checkoutTransactionId;
	}
	
	
}
