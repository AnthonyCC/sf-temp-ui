package com.freshdirect.customer;

import com.freshdirect.framework.core.ModelSupport;

/**
 * @author Aniwesh
 *
 */
public class ErpEWalletTxNotifyModel extends ModelSupport {

	private static final long serialVersionUID = -7861663963588991563L;

	private String transactionId;
	private String status;
	private String eWalletId;
	private String customerId;
	private String orderId;
	private String notifyStatus;
	private String eWalletVendorId;
	
	
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
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
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the notifyStatus
	 */
	public String getNotifyStatus() {
		return notifyStatus;
	}
	/**
	 * @param notifyStatus the notifyStatus to set
	 */
	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}
	/**
	 * @return the eWalletVendorId
	 */
	public String geteWalletVendorId() {
		return eWalletVendorId;
	}
	/**
	 * @param eWalletVendorId the eWalletVendorId to set
	 */
	public void seteWalletVendorId(String eWalletVendorId) {
		this.eWalletVendorId = eWalletVendorId;
	}
	
	
}
