package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Rob
 * 
 */
public class SpecialRestrictedItemsError extends Message {
	
		private List<CartLineItem> items = new ArrayList<CartLineItem>();
				
		public List<CartLineItem> getItems() {
			return items;
		}

		public void setItems(List<CartLineItem> items) {
			this.items = items;
		}

		public void addCartLineItem(CartLineItem item) {
			this.items.add(item);
		}

		private String errorCode;

		private String message;

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	/**
	 * @author Rob
	 * 
	 */
	public static class CartLineItem {

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

}
