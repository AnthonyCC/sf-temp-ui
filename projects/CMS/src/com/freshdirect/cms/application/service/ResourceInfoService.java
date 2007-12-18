package com.freshdirect.cms.application.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.service.ResourceInfoServiceI}.
 */
public class ResourceInfoService implements ResourceInfoServiceI {

	private List infoLog = new ArrayList();

	public void addEvent(String event) {
		this.infoLog.add(event);
	}

	public List getInfoLog() {
		return this.infoLog;
	}

	public String getPublishId() {
		for (Iterator i = infoLog.iterator(); i.hasNext();) {
			String line = ((String) i.next()).toLowerCase();
			if (line.indexOf("publishid") > -1) {
				String[] strings = line.split("publishid: ");
				return strings[strings.length - 1];
			}
		}
		return null;
	}

}
