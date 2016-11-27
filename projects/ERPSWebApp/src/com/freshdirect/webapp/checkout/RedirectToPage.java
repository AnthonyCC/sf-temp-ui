package com.freshdirect.webapp.checkout;

public class RedirectToPage extends Exception {
	private static final long serialVersionUID = 4906399821004185458L;

	String page;
	
	public RedirectToPage(String page) {
		super();
		
		this.page = page;
	}

	public String getPage() {
		return page;
	}
}
