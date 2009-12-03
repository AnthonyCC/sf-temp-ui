package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Rob
 * 
 */
public class AtpError extends Message {

	private Date firstAvailableDate;

	public String getFirstAvailableDate() {
		return (firstAvailableDate != null ? formatter.format(firstAvailableDate) : null);
	}

	public void setFirstAvailableDate(String firstAvailableDate)
			throws ParseException {
		this.firstAvailableDate = (firstAvailableDate != null ? formatter.parse(firstAvailableDate) : null);
	}

	public void setFirstAvailableDate(Date firstAvailableDate) {
		this.firstAvailableDate = firstAvailableDate;
	}

	private List<Group> groups = new ArrayList<Group>();

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group) {
		this.groups.add(group);
	}

	/**
	 * @author Rob
	 * 
	 */
	public static class Group {
		private String name;

		private List<CartLineItem> items = new ArrayList<CartLineItem>();

		public void addCartLineItem(CartLineItem item) {
			this.items.add(item);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<CartLineItem> getItems() {
			return items;
		}

		public void setItems(List<CartLineItem> items) {
			this.items = items;
		}

	}

	/**
	 * @author Rob
	 * 
	 */
	public static class ItemAvailabilityError {
		private String errorCode;

		private String message;

		private String[] args;;

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

		public String[] getArgs() {
			return args;
		}

		public void setArgs(String[] args) {
			this.args = args;
		}

	}

	/**
	 * @author Rob
	 * 
	 */
	public static class CartLineItem {

		private double quantity;

		private String description;

		private String configurationDesc;

		private ItemAvailabilityError error;

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

		public ItemAvailabilityError getError() {
			return error;
		}

		public void setError(ItemAvailabilityError error) {
			this.error = error;
		}

	}

}
