package com.freshdirect.delivery.model;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverySummaryModel implements Serializable {
	
	public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
	
	private int deliveryAttempts;
	
	private String deliveryStatus;
	
	private String customerContactStatus;
	
	private boolean orderDelivered;
	
	private Date estimatedDlvTime;
	
	private boolean isEarlyDeliveryReq;
	
	private String earlyDlvStatus;
	
	private boolean isDeliveryAccessReq;
	
	private String dlvAccessStatus;
	
	private List<AirclicTextMessageVO> messages = new ArrayList<AirclicTextMessageVO>();
	
	private Map<String, List<String>> exceptions = new HashMap<String, List<String>>();
	
	private List<AirclicCartonInfo> cartonInfo = new ArrayList<AirclicCartonInfo>();
	
	private Date deliveryETAStart;
	
	private Date deliveryETAEnd;
		
	public DeliverySummaryModel(int deliveryAttempts, String deliveryStatus,
			String customerContactStatus, Date estimatedDlvTime,
			List<AirclicTextMessageVO> messages,
			Map<String, List<String>> exceptions, List<AirclicCartonInfo> cartonInfo) {
		super();
		this.deliveryAttempts = deliveryAttempts;
		this.deliveryStatus = deliveryStatus;
		this.customerContactStatus = customerContactStatus;
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
	public String getCustomerContactStatus() {
		return customerContactStatus;
	}

	public void setCustomerContactStatus(String customerContactStatus) {
		this.customerContactStatus = customerContactStatus;
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
		return orderDelivered;
	}

	public void setOrderDelivered(boolean orderDelivered) {
		this.orderDelivered = orderDelivered;
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

	public boolean isEarlyDeliveryReq() {
		return isEarlyDeliveryReq;
	}

	public void setEarlyDeliveryReq(boolean isEarlyDeliveryReq) {
		this.isEarlyDeliveryReq = isEarlyDeliveryReq;
	}

	public String getEarlyDlvStatus() {
		return earlyDlvStatus;
	}

	public void setEarlyDlvStatus(String earlyDlvStatus) {
		this.earlyDlvStatus = earlyDlvStatus;
	}

	public boolean isDeliveryAccessReq() {
		return isDeliveryAccessReq;
	}

	public void setDeliveryAccessReq(boolean isDeliveryAccessReq) {
		this.isDeliveryAccessReq = isDeliveryAccessReq;
	}

	public String getDlvAccessStatus() {
		return dlvAccessStatus;
	}

	public void setDlvAccessStatus(String dlvAccessStatus) {
		this.dlvAccessStatus = dlvAccessStatus;
	}

	public Date getDeliveryETAStart() {
		return deliveryETAStart;
	}

	public void setDeliveryETAStart(Date deliveryETAStart) {
		this.deliveryETAStart = deliveryETAStart;
	}

	public Date getDeliveryETAEnd() {
		return deliveryETAEnd;
	}

	public void setDeliveryETAEnd(Date deliveryETAEnd) {
		this.deliveryETAEnd = deliveryETAEnd;
	}
	
	public String getDeliveryETAWindow(){
		if(this.deliveryETAStart != null && this.deliveryETAEnd != null){
			return serverTimeFormat.format(deliveryETAStart) +" - "+serverTimeFormat.format(deliveryETAEnd);
		}
		
		return null;
	}

	
}
