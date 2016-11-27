package com.freshdirect.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HttpContext {

	private final HttpSession session;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	public HttpContext(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		this.session = session;
		this.request = request;
		this.response = response;
	}

	public HttpSession getSession() {
		return this.session;
	}

	public HttpServletRequest getRequest() {
		return this.request;  
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

}
