package com.freshdirect.webapp.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractFilter implements Filter {
	
	protected FilterConfig config;

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
	
	public boolean isFilterApplied(HttpServletRequest request){
		boolean filterApplied = request.getAttribute(this.getFilterName()) != null;
		if(!filterApplied){
			request.setAttribute(this.getFilterName(), Boolean.TRUE);
		}
		
		return filterApplied;
	}

	public void destroy() {
		//this is intensionally empty to provide a default implementation.
	}
	
	public abstract String getFilterName();

}
