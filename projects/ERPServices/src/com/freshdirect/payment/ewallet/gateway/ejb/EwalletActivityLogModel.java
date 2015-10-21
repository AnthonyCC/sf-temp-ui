package com.freshdirect.payment.ewallet.gateway.ejb;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * This class is model class for the table "MIS"."EWALLETAUDITLOG"
 * 
 * @author garooru
 *
 */
public class EwalletActivityLogModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 328533187357196022L;
	private String eWalletID;
	private String customerId;
	private String request;
	private String response;
	private String transactionType;
	private Timestamp creationTimeStamp;
	private String status;
	private String orderId;
	private String transactionId;
	/**
	 * @return the eWalletID
	 */
	public String geteWalletID() {
		return eWalletID;
	}
	/**
	 * @param eWalletID the eWalletID to set
	 */
	public void seteWalletID(String eWalletID) {
		this.eWalletID = eWalletID;
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
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @return the creationTimeStamp
	 */
	public Timestamp getCreationTimeStamp() {
		return creationTimeStamp;
	}
	/**
	 * @param creationTimeStamp the creationTimeStamp to set
	 */
	public void setCreationTimeStamp(Timestamp creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
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
		
}
