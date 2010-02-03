package com.freshdirect.routing.model;


public interface IUnassignedModel  {
	IOrderModel getOrder();
	void setOrder(IOrderModel order);
	IDeliverySlot getSlot();
	void setSlot(IDeliverySlot slot);


	String getIsForced();

	void setIsForced(String isForced);

	String getIsChefsTable();
	void setIsChefsTable(String isChefsTable);
}
