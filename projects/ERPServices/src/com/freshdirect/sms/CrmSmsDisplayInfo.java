package com.freshdirect.sms;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrmSmsDisplayInfo implements Serializable {
	
	private Date timeSent;
	private String alertType;
	private String message;
	private String status;
	private String mobileNumber;
	
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getTimeSent() {
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		if(timeSent!=null)
			return sdf.format(timeSent);
		else
			return "N/A";
	}
	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
