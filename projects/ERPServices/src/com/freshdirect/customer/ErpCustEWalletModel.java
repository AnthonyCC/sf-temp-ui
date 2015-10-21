package com.freshdirect.customer;

import com.freshdirect.framework.core.ModelSupport;

/**
 * @author Aniwesh Vatsal
 *
 */
public class ErpCustEWalletModel extends ModelSupport {

	private static final long serialVersionUID = -7861663963588991563L;

	private String eWalletId;
	private String longAccessToken;
	private String customerId;
	/**
	 * @return the eWalletId
	 */
	public String geteWalletId() {
		return eWalletId;
	}

	/**
	 * @param eWalletId the eWalletId to set
	 */
	public void seteWalletId(String eWalletId) {
		this.eWalletId = eWalletId;
	}

	/**
	 * @return the longAccessToken
	 */
	public String getLongAccessToken() {
		return longAccessToken;
	}

	/**
	 * @param longAccessToken the longAccessToken to set
	 */
	public void setLongAccessToken(String longAccessToken) {
		this.longAccessToken = longAccessToken;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
