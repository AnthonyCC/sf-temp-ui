package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.URLRewriteManager;
import com.freshdirect.fdstore.URLRewriteRule;

public class URLRewriteFilter extends AbstractFilter {
	
	private final String filterName = this.getClass().getName();

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		if(!this.isFilterApplied(request)) {
			String fromURL = request.getRequestURI();
			
			URLRewriteRule rule = URLRewriteManager.getInstance().getRedirect(fromURL);
			if(rule != null) {
				response.sendRedirect(rule.getRedirect());
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	public String getFilterName(){
		return this.filterName;
	}
}
