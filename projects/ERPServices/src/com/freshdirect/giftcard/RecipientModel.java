package com.freshdirect.giftcard;

import java.util.Random;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.FormatterUtil;

public class RecipientModel extends ModelSupport {

	private final static Random RND = new Random();
	/** Random ID, not persisted */
	private final int randomId = RND.nextInt();
	
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipentName) {
		this.recipientName = recipentName;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipentEmail) {
		this.recipientEmail = recipentEmail;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public EnumGCDeliveryMode getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(EnumGCDeliveryMode deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getPersonalMessage() {
		return personalMessage;
	}

	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}

	public String getFormattedAmount() {
		return FormatterUtil.formatToTwoDecimal(this.getAmount());
	}
	private String senderName=null;
	private String senderEmail=null;
	private String recipientName=null;
	private String recipientEmail=null;
	private String templateId=null;
	private double amount=0;
	private EnumGCDeliveryMode deliveryMode=null;
	private String personalMessage=null;
	
	public RecipientModel(){		
	}
	
	public int getRandomId() {
		return this.randomId;
	}
}
