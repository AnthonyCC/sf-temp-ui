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
	
	private String header_origin = "Access-Control-Allow-Origin";
	private String header_credentials = "Access-Control-Allow-Credentials";
	private String header_p3p = "P3P";
	private String header_p3p_policy = "CP='This is not a P3P policy!'";

	private final String filterName = this.getClass().getName();

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		String origin = request.getHeader("Origin");
		String allowedOrigins = FDStoreProperties.getCORSDomain();

		if (origin != null && (allowedOrigins.equals("*") || allowedOrigins.contains(origin))) {
			
			response.setHeader(header_origin, origin);
			response.setHeader(header_credentials, "true");
			//P3P Policy workaround for IE third party integration
			if(FDStoreProperties.isP3PPolicyEnabled()) {
				response.setHeader(header_p3p, header_p3p_policy);
			}
		}

		filterChain.doFilter(request, response);
	}

	public String getFilterName(){
		return this.filterName;
	}
}
