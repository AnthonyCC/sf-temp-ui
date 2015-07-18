package com.freshdirect.fdstore.content;

import java.util.Date;

public class CMSPageRequest {
	private String pageName;
	private Date date;
	
	public String getPageName() {
		return pageName;
	}
	
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
}
