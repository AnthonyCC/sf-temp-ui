package com.freshdirect.cms.application.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.service.ResourceInfoServiceI}.
 */
public class ResourceInfoService implements ResourceInfoServiceI {

	private List<String> infoLog = new ArrayList<String>();

	public void addEvent(String event) {
		this.infoLog.add(event);
	}

	public List<String> getInfoLog() {
		return this.infoLog;
	}

	public String getPublishId() {
		for (String entry : infoLog) {
			final String line = entry.toLowerCase();
			if (line.indexOf("publishid") > -1) {
				String[] strings = line.split("publishid: ");
				return strings[strings.length - 1];
			}
		}
		return null;
	}

}
