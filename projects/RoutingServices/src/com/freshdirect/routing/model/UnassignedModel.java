package com.freshdirect.routing.model;

import java.util.Date;

public class UnassignedModel extends BaseModel implements IUnassignedModel {

	private IOrderModel order;
	
	private IDeliverySlot slot;
	
	private String isForced;
	
	private String isChefsTable;
	
	

	public String getIsForced() {
		return isForced;
	}

	public void setIsForced(String isForced) {
		this.isForced = isForced;
	}

	public String getIsChefsTable() {
		return isChefsTable;
	}

	public void setIsChefsTable(String isChefsTable) {
		this.isChefsTable = isChefsTable;
	}

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
