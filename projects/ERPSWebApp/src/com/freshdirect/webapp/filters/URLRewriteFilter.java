package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.URLRewriteManager;
import com.freshdirect.fdstore.URLRewriteRule;

public class URLRewriteFilter extends AbstractFilter {
	
	private final String filterName = this.getClass().getName();

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		if(!this.isFilterApplied(request)) {
			String fromURL = request.getRequestURI();
			String queryString = request.getQueryString();
			URLRewriteRule rule = URLRewriteManager.getInstance().getRedirect(fromURL);
			if(rule != null) {
				response.sendRedirect(rule.getRedirect());
				return;
			}else{
				//[DP17-84]-FREE TRIAL DP Opt-in
				if(fromURL.equalsIgnoreCase("/pdp.jsp") && null !=queryString){
					redirectForFreeTrialDP (queryString, response);
				}				
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	public String getFilterName(){
		return this.filterName;
	}
	
	private String redirectForFreeTrialDP (String queryString,HttpServletResponse response){
		String redirectURL = null;
		if(FDStoreProperties.isDlvPassFreeTrialOptinFeatureEnabled()){
			if(null !=queryString && queryString.contains("=mkt_dlv_pass_3mnth")){//product id of free-trial dp.
				redirectURL = "/freetrial.jsp";
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", redirectURL);
			
			}
		}
		return redirectURL;
	}
}
