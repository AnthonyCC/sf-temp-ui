package com.freshdirect.fdstore.mail;

import java.util.Map;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDOrderI;

public class FDTransactionalEmail extends FDInfoEmail {

	private FDOrderI order;

	public FDTransactionalEmail(FDCustomerInfo customer, FDOrderI order) {
		super(customer);
		this.order = order;
	}

	public FDOrderI getOrder() {
		return this.order;
	}

	public void setOrder(FDOrderI order) {
		this.order = order;
	}

	/**
	 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
	 */
	protected void decorateMap(Map map) {
		super.decorateMap(map);
		map.put("order", this.getOrder());
	}

}
