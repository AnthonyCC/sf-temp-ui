package com.freshdirect.transadmin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class FDDispatcherServlet extends DispatcherServlet {
	
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rootUri = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1, request.getRequestURI().length());
		request.setAttribute("ROOT_URI",rootUri);
		
		if(com.freshdirect.transadmin.security.SecurityManager.hasAccessToPage(request, rootUri)) {
			super.doService(request, response);
		} else {
			response.sendRedirect("accessdenied.do");
		}
		
	}

}
