package com.freshdirect.webapp.ajax.expresscheckout.location.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PickupLocationData extends LocationData {

	@JsonProperty("popupurl")
	private String popupUrl;

	@JsonProperty("times")
	private List<OpeningHourData> openingHours;

	public String getPopupUrl() {
		return popupUrl;
	}

	public void setPopupUrl(String popupUrl) {
		this.popupUrl = popupUrl;
	}

	public List<OpeningHourData> getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(List<OpeningHourData> openingHours) {
		this.openingHours = openingHours;
	}

}
