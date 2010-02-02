package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public interface IUnassignedModel  {
	IOrderModel getOrder();
	void setOrder(IOrderModel order);
	IDeliverySlot getSlot();
	void setSlot(IDeliverySlot slot);
	
}
