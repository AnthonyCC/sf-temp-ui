package com.freshdirect.webapp.ajax.expresscheckout.cart.data;

import java.util.HashMap;
import java.util.Map;

public class CartSubTotalFieldData {

	private String id;
	private String text;
	private String value;
	private Map<String, Object> other = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public Map<String, Object> getOther() {
		return other;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOther(Map<String, Object> other) {
		this.other = other;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
