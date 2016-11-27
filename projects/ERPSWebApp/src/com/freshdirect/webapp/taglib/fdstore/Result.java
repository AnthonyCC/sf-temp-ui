package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.framework.webapp.ActionResult;

/**
 * Customized action result subclass that carries a timeslot object too
 */
public class Result extends ActionResult {
	private FDDeliveryTimeslotModel deliveryTimeslotModel;

	public void setDeliveryTimeslotModel(
			FDDeliveryTimeslotModel deliveryTimeslotModel) {
		this.deliveryTimeslotModel = deliveryTimeslotModel;
	}

	public FDDeliveryTimeslotModel getDeliveryTimeslotModel() {
		return deliveryTimeslotModel;
	}
}
