package com.freshdirect.framework.monitor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class WebRequestMonitor implements Filter {

	//private static final Category LOGGER = LoggerFactory.getInstance(WebRequestMonitor.class);

	FilterConfig config;
	
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (config == null) return;
		RequestLogger.logRequest(request);
		chain.doFilter(request, response);	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void destroy() {
	}
		
}
