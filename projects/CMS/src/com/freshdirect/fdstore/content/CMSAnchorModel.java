package com.freshdirect.fdstore.content;

public class CMSAnchorModel extends CMSComponentModel{
	private String url;
	private String target;
	private String text;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}