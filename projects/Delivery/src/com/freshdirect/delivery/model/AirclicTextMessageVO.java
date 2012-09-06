package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.util.Date;

public class AirclicTextMessageVO implements Serializable  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5385313761673605986L;
	public AirclicTextMessageVO(Date deliveryDate,String route,int stop,
			String message,  String source,  String sender, 
			 String orderId) {
		super();
		
		this.deliveryDate = deliveryDate;
		this.sender = sender;
		this.message = message;
		this.source = source;
		this.route = route;
		this.stop = stop;
		this.orderId = orderId;
	}
	
	public AirclicTextMessageVO(Date deliveryDate,String route,int stop,
			String message,  String source,  String sender, 
			 String orderId, String customerId) {
		this(deliveryDate,route,stop,message,source,sender,orderId);
		this.customerId = customerId;
	}
	public AirclicTextMessageVO()
	{
		super();
	}
	
	private Date createDate;
	private Date deliveryDate;
	private String sender ;
	private String message;
	private String source;
	private String route;
	private int stop;
	private String orderId;
	private String id;
	private String customerId;
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
