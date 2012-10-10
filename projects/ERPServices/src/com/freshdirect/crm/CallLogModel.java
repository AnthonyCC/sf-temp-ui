package com.freshdirect.crm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CallLogModel implements Serializable {
	
	private String callerId;
	private String callerGUIId;
	private String orderNumber;
	private Date startTime;
	private int duration;
	private String callOutcome;
	private String phoneNumber;
	private String menuOption;
	private int talkTime;	
	
	public CallLogModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getCallerGUIId() {
		return callerGUIId;
	}

	public void setCallerGUIId(String callerGUIId) {
		this.callerGUIId = callerGUIId;
	}

	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getCallOutcome() {
		return callOutcome;
	}
	public void setCallOutcome(String callOutcome) {
		this.callOutcome = callOutcome;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMenuOption() {
		return menuOption;
	}
	public void setMenuOption(String menuOption) {
		this.menuOption = menuOption;
	}
	public int getTalkTime() {
		return talkTime;
	}
	public void setTalkTime(int talkTime) {
		this.talkTime = talkTime;
	}
	
	public String getScanTime(){
		if(this.startTime != null){
			return new SimpleDateFormat("MM/dd/yyyy hh:mm aaa").format(this.startTime);
		}
		return "";
	}

	@Override
	public String toString() {
		return "CallLogModel [callerId=" + callerId + ", callerGUIId="
				+ callerGUIId + ", orderNumber=" + orderNumber
				+ ", phoneNumber=" + phoneNumber + "]";
	}
	
}
