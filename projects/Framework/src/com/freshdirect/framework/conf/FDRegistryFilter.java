package com.freshdirect.framework.conf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Servlet filter to initialize and clean up the thread-specific aspects of
 * {@link FDRegistry}.
 */
public class FDRegistryFilter implements Filter {

	/**
	 * Initialize the filter.
	 * 
	 * @param config
	 *            the filter config to use.
	 */
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * Perform filtering.
	 * 
	 * @param request
	 *            the servlet request.
	 * @param response
	 *            the servlet response.
	 * @param chain
	 *            the filter chain we're in.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		FDRegistry.getInstance().setupThread();

		chain.doFilter(request, response);

		FDRegistry.getInstance().cleanupThread();

	}

	public void destroy() {
	}

}
