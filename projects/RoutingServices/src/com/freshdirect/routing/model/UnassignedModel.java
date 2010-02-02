package com.freshdirect.routing.model;

import java.util.Date;

public class UnassignedModel extends BaseModel implements IUnassignedModel {

	private IOrderModel order;
	
	private IDeliverySlot slot;

	public IOrderModel getOrder() {
		return order;
	}

	public void setOrder(IOrderModel order) {
		this.order = order;
	}

	public IDeliverySlot getSlot() {
		return slot;
	}

	public void setSlot(IDeliverySlot slot) {
		this.slot = slot;
	}
}
