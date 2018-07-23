package com.freshdirect.webapp.ajax.expresscheckout.location.data;

import java.util.List;

public class FormLocationData {

	private String selected;
	private List<LocationData> addresses;

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public List<LocationData> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<LocationData> addresses) {
		this.addresses = addresses;
	}
}
