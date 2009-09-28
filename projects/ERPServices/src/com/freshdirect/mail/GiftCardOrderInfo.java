package com.freshdirect.mail;

import java.io.Serializable;

import com.freshdirect.framework.mail.EmailSupport;
import com.freshdirect.framework.mail.FTLEmailI;

/**
 * This is the DTO object between Gift Card business module and Gift Card email notification module.
 * Business module has to pass this object while invoking the services of email notification module for
 * sending giftcard purchase related notifications.
 * 
 * @author ksriram
 *
 */
public class GiftCardOrderInfo implements Serializable{
	
	private String gcId;
	private double gcAmount;
	private String gcRedempcode;
	private String gcFor;
	private String gcFrom;
	private String gcMessage;
	private String gcType;
	private String gcRecipientEmail;
	private String gcSenderEmail;
	
	
	public GiftCardOrderInfo(String gcId, double gcAmount,
			String gcRedempcode, String gcFor, String gcFrom, String gcMessage,
			String gcType, String gcRecipientEmail, String gcSenderEmail) {
		super();
		this.gcId = gcId;
		this.gcAmount = gcAmount;
		this.gcRedempcode = gcRedempcode;
		this.gcFor = gcFor;
		this.gcFrom = gcFrom;
		this.gcMessage = gcMessage;
		this.gcType = gcType;
		this.gcRecipientEmail = gcRecipientEmail;
		this.gcSenderEmail = gcSenderEmail;
	}


	public String getGcId() {
		return gcId;
	}


	public void setGcId(String gcId) {
		this.gcId = gcId;
	}


	public double getGcAmount() {
		return gcAmount;
	}


	public void setGcAmount(double gcAmount) {
		this.gcAmount = gcAmount;
	}


	public String getGcRedempcode() {
		return gcRedempcode;
	}


	public void setGcRedempcode(String gcRedempcode) {
		this.gcRedempcode = gcRedempcode;
	}


	public String getGcFor() {
		return gcFor;
	}


	public void setGcFor(String gcFor) {
		this.gcFor = gcFor;
	}


	public String getGcFrom() {
		return gcFrom;
	}


	public void setGcFrom(String gcFrom) {
		this.gcFrom = gcFrom;
	}


	public String getGcMessage() {
		return gcMessage;
	}


	public void setGcMessage(String gcMessage) {
		this.gcMessage = gcMessage;
	}


	public String getGcType() {
		return gcType;
	}


	public void setGcType(String gcType) {
		this.gcType = gcType;
	}


	public GiftCardOrderInfo() {
		super();
	}


	/**
	 * @return the gcRecipientEmail
	 */
	public String getGcRecipientEmail() {
		return gcRecipientEmail;
	}


	/**
	 * @param gcRecipientEmail the gcRecipientEmail to set
	 */
	public void setGcRecipientEmail(String gcRecipientEmail) {
		this.gcRecipientEmail = gcRecipientEmail;
	}


	/**
	 * @return the gcSenderEmail
	 */
	public String getGcSenderEmail() {
		return gcSenderEmail;
	}


	/**
	 * @param gcSenderEmail the gcSenderEmail to set
	 */
	public void setGcSenderEmail(String gcSenderEmail) {
		this.gcSenderEmail = gcSenderEmail;
	}
	
	
	

}
