package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitForm {

	@JsonProperty("fdform")
	private String formId;

	@JsonProperty("success")
	private boolean success;
	
	private Map<String, Object> result = new HashMap<String, Object>();

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

}
