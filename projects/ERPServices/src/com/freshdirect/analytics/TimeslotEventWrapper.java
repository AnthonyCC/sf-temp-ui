package com.freshdirect.analytics;


import com.freshdirect.analytics.TimeslotEventModel;

/**
 * 
 * @author tbalumuri
 *
 */

public class TimeslotEventWrapper implements java.io.Serializable {
	
	private String address;
	private String reservationId;
	private String orderId;
	private String customerId;
	private EventType eventType;
	private int responseTime;
	private String comments;
	private TimeslotEventModel event;
	
	public String getAddress() {
		return address;
	}

	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public TimeslotEventModel getEvent() {
		return event;
	}

	public void setEvent(TimeslotEventModel event) {
		this.event = event;
	}
	

}