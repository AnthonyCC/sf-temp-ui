package com.freshdirect.fdstore.mail;

import java.util.Map;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;

public class FDStandingOrderEmail extends FDTransactionalEmail {
	private static final long serialVersionUID = 8314062573191547683L;

	FDStandingOrder standingOrder;
	
	boolean hasUnavailableItems;

	public FDStandingOrderEmail(FDCustomerInfo customer, FDOrderI order, FDStandingOrder standingOrder, boolean hasUnavailableItems) {
		super(customer, order);
		this.standingOrder = standingOrder;
		this.hasUnavailableItems = hasUnavailableItems;
	}

	public FDStandingOrder getStandingOrder() {
		return standingOrder;
	}
	
	public void setStandingOrder(FDStandingOrder standingOrder) {
		this.standingOrder = standingOrder;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void decorateMap(Map map) {
		super.decorateMap(map);
		map.put("standingOrder", standingOrder);
		map.put("hasUnavailableItems", hasUnavailableItems);
	}
}
