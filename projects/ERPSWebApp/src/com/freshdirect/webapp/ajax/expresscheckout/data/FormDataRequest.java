package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FormDataRequest {

	@JsonProperty("fdform")
	private String formId;

	@JsonProperty("formdata")
	private Map<String, Object> formData;

	@JsonProperty("edited")
	private String edited;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String fdFormId) {
		this.formId = fdFormId;
	}

	public String getEdited() {
		return edited;
	}

	public void setEdited(String edited) {
		this.edited = edited;
	}

	public Map<String, Object> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, Object> formData) {
		this.formData = formData;
	}

}
