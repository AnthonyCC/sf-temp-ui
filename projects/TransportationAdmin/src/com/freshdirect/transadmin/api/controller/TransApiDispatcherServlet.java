package com.freshdirect.transadmin.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

@SuppressWarnings("serial")
public class TransApiDispatcherServlet extends DispatcherServlet {
	
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rootUri = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length());
		System.out.println("ROOT_URI"+rootUri);
		
		super.doService(request, response);
		
	}

}
