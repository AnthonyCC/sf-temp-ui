package com.freshdirect.fdstore.brandads.model;

import java.io.Serializable;

public class HLBrandProductAdResponseErrorCode implements Serializable {

	private String code;
	private String text;
	private String details;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
