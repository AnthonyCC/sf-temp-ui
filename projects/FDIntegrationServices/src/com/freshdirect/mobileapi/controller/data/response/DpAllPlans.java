package com.freshdirect.mobileapi.controller.data.response;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Message;

public class DpAllPlans extends Message{

	private Map<String, Object> deliveryPasses = new HashMap<String, Object>();

	public Map<String, Object> getDeliveryPasses() {
		return deliveryPasses;
	}

	public void setDeliveryPasses(Map<String, Object> deliveryPasses) {
		this.deliveryPasses = deliveryPasses;
	}
}
