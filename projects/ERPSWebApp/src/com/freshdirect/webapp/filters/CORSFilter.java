package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDStoreProperties;

public class CORSFilter extends AbstractFilter {
	
  private String header = "Access-Control-Allow-Origin";

	private final String filterName = this.getClass().getName();

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		
    response.setHeader(header, FDStoreProperties.getCORSDomain());
		
		filterChain.doFilter(request, response);
	}
	
	public String getFilterName(){
		return this.filterName;
	}
}
