package com.freshdirect.webapp.ajax.expresscheckout.availability.data;

import java.util.List;

public class AvailabilityRequestData {

	private boolean isEbt;
	private String requestUrl;
	private List<String> removableStockUnavailabilityCartLineIds;

	public boolean isEbt() {
		return isEbt;
	}

	public void setEbt(boolean isEbt) {
		this.isEbt = isEbt;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public List<String> getRemovableStockUnavailabilityCartLineIds() {
		return removableStockUnavailabilityCartLineIds;
	}

	public void setRemovableStockUnavailabilityCartLineIds(List<String> removeStockUnavailabilityCartLineIds) {
		this.removableStockUnavailabilityCartLineIds = removeStockUnavailabilityCartLineIds;
	}

}
