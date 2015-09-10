package com.freshdirect.mobileapi.model.data;

import java.util.ArrayList;
import java.util.List;

public class HelpTopic {
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List getEntries() {
		return entries;
	}
	public void setEntries(Object entries) {
		this.entries.add(entries);
	}
	private String title;
	private String path;
	private List entries = new ArrayList();

}
