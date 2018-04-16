package com.freshdirect.webapp.ajax.informational;

import java.io.Serializable;

public class InformationalData implements Serializable{

	private static final long serialVersionUID = 3683174897416458704L;

	String action = ""; //last action attempted
	int viewCount = -1; //current view count
	int viewCountLimit = -1; //business rule limit
	String media = ""; //media content (not path)
	boolean show = false; //current value of FdSessionUser.isShowingInformOrderModify()
	
	boolean success = false;

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	public int getViewCountLimit() {
		return viewCountLimit;
	}
	public void setViewCountLimit(int viewCountLimit) {
		this.viewCountLimit = viewCountLimit;
	}
	
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
