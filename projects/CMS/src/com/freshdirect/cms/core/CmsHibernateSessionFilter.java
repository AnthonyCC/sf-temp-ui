package com.freshdirect.cms.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author darkeye
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CmsHibernateSessionFilter implements Filter {

	/**
	 * Initialize the filter.
	 * 
	 * @param config the filter config to use.
	 */
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * Perform filtering.
	 * 
	 * @param request the servlet request.
	 * @param response the servlet response.
	 * @param chain the filter chain we're in.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// process as usual
		chain.doFilter(request, response);
		
		// if there is a hibernate session going on, close it
		CmsDaoFactory daoFactory = CmsDaoFactory.getInstance();
		
		if (daoFactory.getThreadSession() != null) {
			daoFactory.closeSession();
		}
	}

	/**
	 * De-initialzie the filter.
	 */
	public void destroy() {
	}

}
