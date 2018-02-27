package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.List;
/* Subclass to hold the "states" for every country */
public class FormMetaDataItemCountry extends FormMetaDataItem {

	private List<FormMetaDataItem> states;

	public List<FormMetaDataItem> getStates() {
		return states;
	}

	public void setStates(List<FormMetaDataItem> states) {
		this.states = states;
	}
}
