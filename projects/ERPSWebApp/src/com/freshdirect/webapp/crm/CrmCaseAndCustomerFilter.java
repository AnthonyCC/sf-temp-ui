package com.freshdirect.webapp.crm;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.webapp.taglib.crm.CrmSession;

public class CrmCaseAndCustomerFilter implements Filter {
	
	private FilterConfig filterConfig;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String redirectPage = this.filterConfig.getInitParameter("redirectPage");
		String lockedDirs = this.filterConfig.getInitParameter("lockedDirs");
		StringTokenizer st = new StringTokenizer(lockedDirs, ";");
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			if(request.getRequestURI().startsWith(token)){
				if(!CrmSession.verifyCaseAttachment(request.getSession())){
					response.sendRedirect(redirectPage);
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
