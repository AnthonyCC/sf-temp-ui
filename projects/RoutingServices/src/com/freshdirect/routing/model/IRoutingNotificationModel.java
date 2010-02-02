package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumRoutingNotification;


public interface IRoutingNotificationModel {
	
	int getNotificationId();
	void setNotificationId(int notificationId);
	
	String getOrderNumber();
	void setOrderNumber(String orderNumber);

	String getCustomerNumber();
	void setCustomerNumber(String customerNumber);

	EnumRoutingNotification getNotificationType();
	void setNotificationType(EnumRoutingNotification notificationType);
	
	IRoutingSchedulerIdentity getSchedulerId();
	void setSchedulerId(IRoutingSchedulerIdentity schedulerId);

}
