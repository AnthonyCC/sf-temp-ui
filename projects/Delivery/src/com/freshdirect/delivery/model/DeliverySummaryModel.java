package com.freshdirect.delivery.model;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.delivery.EnumDeliveryMenuOption;

public class DeliverySummaryModel implements Serializable {
	
	public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
	
	private int deliveryAttempts;
	
	private String deliveryStatus;
	
	private boolean isCustomerContacted;
	
	private boolean isOrderDelivered;
	
	private Date estimatedDlvTime;
	
	private List<AirclicTextMessageVO> messages = new ArrayList<AirclicTextMessageVO>();
	
	private Map<String, List<String>> exceptions = new HashMap<String, List<String>>();
	
	private List<AirclicCartonInfo> cartonInfo = new ArrayList<AirclicCartonInfo>();
	
		
	public DeliverySummaryModel(int deliveryAttempts, String deliveryStatus,
			boolean isCustomerContacted, Date estimatedDlvTime,
			List<AirclicTextMessageVO> messages,
			Map<String, List<String>> exceptions, List<AirclicCartonInfo> cartonInfo) {
		super();
		this.deliveryAttempts = deliveryAttempts;
		this.deliveryStatus = deliveryStatus;
		this.isCustomerContacted = isCustomerContacted;
		this.estimatedDlvTime = estimatedDlvTime;
		this.messages = messages;
		this.exceptions = exceptions;
		this.cartonInfo = cartonInfo;
	}
	
	public DeliverySummaryModel() {
		super();
	}
	public int getDeliveryAttempts() {
		return deliveryAttempts;
	}
	public void setDeliveryAttempts(int deliveryAttempts) {
		this.deliveryAttempts = deliveryAttempts;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public boolean isCustomerContacted() {
		return isCustomerContacted;
	}
	public void setCustomerContacted(boolean isCustomerContacted) {
		this.isCustomerContacted = isCustomerContacted;
	}
	public Date getEstimatedDlvTime() {
		return estimatedDlvTime;
	}
	public void setEstimatedDlvTime(Date estimatedDlvTime) {
		this.estimatedDlvTime = estimatedDlvTime;
	}
	public List<AirclicTextMessageVO> getMessages() {
		return messages;
	}
	public void setMessages(List<AirclicTextMessageVO> messages) {
		this.messages = messages;
	}
	public Map<String, List<String>> getExceptions() {
		return exceptions;
	}
	public void setExceptions(Map<String, List<String>> exceptions) {
		this.exceptions = exceptions;
	}
	public List<AirclicCartonInfo> getCartonInfo() {
		return cartonInfo;
	}
	public void setCartonInfo(List<AirclicCartonInfo> cartonInfo) {
		this.cartonInfo = cartonInfo;
	}

	public boolean isOrderDelivered() {
		return isOrderDelivered;
	}

	public void setOrderDelivered(boolean isOrderDelivered) {
		this.isOrderDelivered = isOrderDelivered;
	}
	
	public String getContainsExceptions() {
		if(exceptions != null && exceptions.size() > 0){
			return "Yes";
		}
		return "No";
	}
	
	public String getAvailableMessages() {
		if(messages != null && messages.size() > 0){
			return "Yes";
		}
		return "No";
	}

	public String getDeliveryTime(){
		if(this.estimatedDlvTime != null){
			return serverTimeFormat.format(estimatedDlvTime);
		}
		
		return "";
	}
	
	
}
