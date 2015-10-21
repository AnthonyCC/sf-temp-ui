package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletRequest extends Message{

	private String eWalletType;
	private String callBackUrl;
	private String oauthToken;
	private String oauthVerifer;
	private String pairingToken;
	private String pairingVerifer;
	private String checkoutUrl;
	private String transCode;
	private String ewalletCardId;
	private String originUrl;
	private String precheckoutTransactionId;
			
	/**
	 * @return the eWalletType
	 */
	public String geteWalletType() {
		return eWalletType;
	}

	/**
	 * @param eWalletType the eWalletType to set
	 */
	public void seteWalletType(String eWalletType) {
		this.eWalletType = eWalletType;
	}

	/**
	 * @return the callBackUrl
	 */
	public String getCallBackUrl() {
		return callBackUrl;
	}

	/**
	 * @param callBackUrl the callBackUrl to set
	 */
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	/**
	 * @return the oauthToken
	 */
	public String getOauthToken() {
		return oauthToken;
	}

	/**
	 * @param oauthToken the oauthToken to set
	 */
	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

	/**
	 * @return the oauthVerifer
	 */
	public String getOauthVerifer() {
		return oauthVerifer;
	}

	/**
	 * @param oauthVerifer the oauthVerifer to set
	 */
	public void setOauthVerifer(String oauthVerifer) {
		this.oauthVerifer = oauthVerifer;
	}

	/**
	 * @return the pairingToken
	 */
	public String getPairingToken() {
		return pairingToken;
	}

	/**
	 * @param pairingToken the pairingToken to set
	 */
	public void setPairingToken(String pairingToken) {
		this.pairingToken = pairingToken;
	}

	/**
	 * @return the pairingVerifer
	 */
	public String getPairingVerifer() {
		return pairingVerifer;
	}

	/**
	 * @param pairingVerifer the pairingVerifer to set
	 */
	public void setPairingVerifer(String pairingVerifer) {
		this.pairingVerifer = pairingVerifer;
	}

	/**
	 * @return the checkoutUrl
	 */
	public String getCheckoutUrl() {
		return checkoutUrl;
	}

	/**
	 * @param checkoutUrl the checkoutUrl to set
	 */
	public void setCheckoutUrl(String checkoutUrl) {
		this.checkoutUrl = checkoutUrl;
	}

	/**
	 * @return the transCode
	 */
	public String getTransCode() {
		return transCode;
	}

	/**
	 * @param transCode the transCode to set
	 */
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	/**
	 * @return the ewalletCardId
	 */
	public String getEwalletCardId() {
		return ewalletCardId;
	}

	/**
	 * @param ewalletCardId the ewalletCardId to set
	 */
	public void setEwalletCardId(String ewalletCardId) {
		this.ewalletCardId = ewalletCardId;
	}

	/**
	 * @return the originUrl
	 */
	public String getOriginUrl() {
		return originUrl;
	}

	/**
	 * @param originUrl the originUrl to set
	 */
	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}

	/**
	 * @return the precheckoutTransactionId
	 */
	public String getPrecheckoutTransactionId() {
		return precheckoutTransactionId;
	}

	/**
	 * @param precheckoutTransactionId the precheckoutTransactionId to set
	 */
	public void setPrecheckoutTransactionId(String precheckoutTransactionId) {
		this.precheckoutTransactionId = precheckoutTransactionId;
	}

	
}
