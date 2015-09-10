package com.freshdirect.mobileapi.controller.data;

public class SafetyDetails {
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
	public String getDetail() {
		return entries;
	}
	public void setDetail(String detail) {
		this.entries = detail;
	}
	private String title;
	private String path;
	private String entries;

}
