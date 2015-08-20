package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Rob
 * 
 */
public class DynamicAvailabilityError extends Message {
	
	private List<UnavailableCartLineItem> unavailableItems = new ArrayList<UnavailableCartLineItem>();
	
	public static class UnavailableCartLineItem {

		private double quantity;

		private String description;

		private String configurationDesc;

		

		public double getQuantity() {
			return quantity;
		}

		public void setQuantity(double quantity) {
			this.quantity = quantity;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getConfigurationDesc() {
			return configurationDesc;
		}

		public void setConfigurationDesc(String configurationDesc) {
			this.configurationDesc = configurationDesc;
		}
	
	}

	public List<UnavailableCartLineItem> getUnavailableItems() {
		return unavailableItems;
	}

	public void setUnavailableItems(List<UnavailableCartLineItem> unavailableItems) {
		this.unavailableItems = unavailableItems;
	}
	
	

}
