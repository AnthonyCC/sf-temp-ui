package com.freshdirect.sms;

import java.io.Serializable;

/**
 * @author smerugu
 *
 */
public class SmsResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private String mobileNumber;
	private String shortCode;
	private String carrierName; 
	private String receivedDate;
	private String message;
	private String eStoreId;

	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String geteStoreId() {
		return eStoreId;
	}
	public void seteStoreId(String eStoreId) {
		this.eStoreId = eStoreId;
	}

}
