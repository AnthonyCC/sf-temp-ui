package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumRoutingNotification;

public class RoutingNotificationModel extends BaseModel implements IRoutingNotificationModel  {
	
	private String orderNumber;	
	private String customerNumber;
		
	private EnumRoutingNotification notificationType;
	
	private IRoutingSchedulerIdentity schedulerId;
	
	public IRoutingSchedulerIdentity getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(IRoutingSchedulerIdentity schedulerId) {
		this.schedulerId = schedulerId;
	}

	private int notificationId;	

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	
	public EnumRoutingNotification getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(EnumRoutingNotification notificationType) {
		this.notificationType = notificationType;
	}
	

	
}
