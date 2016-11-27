package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
import java.util.Map;

public class ProductAnnotationData implements Serializable {
	private static final long serialVersionUID = 6183853179582472317L;

	private Map<String,String> data;
	
	private String erpsyLink;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getErpsyLink() {
		return erpsyLink;
	}

	public void setErpsyLink(String erpsyLink) {
		this.erpsyLink = erpsyLink;
	}
}
