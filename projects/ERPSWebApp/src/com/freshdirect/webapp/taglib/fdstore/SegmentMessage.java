package com.freshdirect.webapp.taglib.fdstore;

import java.util.Date;

public class SegmentMessage {
	
	private String marketingPromoValue;
	private boolean location1;
	private boolean location2;
	private String greeting;
	private String message;
	private String messageLink;
	private boolean centerMessage;
	
	public SegmentMessage() {}

	public SegmentMessage(
		String marketingPromoValue,
		boolean location1,
		boolean location2 ,
		String greeting,
		String message,
		String messageLink,
		boolean centerMessage) {

		this.marketingPromoValue = marketingPromoValue;
		this.location1 = location1;
		this.location2 = location2;
		this.greeting = greeting;
		this.message = message;
		this.messageLink = messageLink;
		this.centerMessage = centerMessage;
	}
	

	public void setCenterMessage(boolean centerMessage) {
		this.centerMessage = centerMessage;
	}

	public void setMarketingPromoValue(String marketingPromoValue) {
		this.marketingPromoValue = marketingPromoValue;
	}

	public void setLocation1(boolean location1) {
		this.location1 = location1;
	}

	public void setLocation2(boolean location2) {
		this.location2 = location2;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageLink(String messageLink) {
		this.messageLink = messageLink;
	}

	public boolean isLocation1() {
		return location1;
	}

	public boolean isLocation2() {
		return location2;
	}

	public boolean isCenterMessage() {
		return centerMessage;
	}

	public String getMarketingPromoValue() {
		return marketingPromoValue;
	}

	public String getGreeting() {
		return greeting;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageLink() {
		return messageLink;
	}

}
