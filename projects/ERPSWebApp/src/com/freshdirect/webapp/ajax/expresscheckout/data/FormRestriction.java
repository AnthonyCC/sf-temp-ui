package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.HashMap;
import java.util.Map;

public class FormRestriction {

	private boolean passed = true;
	private String type;
	private String media;
	private Map<String, Object> data = new HashMap<String, Object>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
