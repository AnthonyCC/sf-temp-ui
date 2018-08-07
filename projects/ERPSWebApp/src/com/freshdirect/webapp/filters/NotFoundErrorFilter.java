package com.freshdirect.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.freshdirect.fdstore.FDNotFoundException;

public class NotFoundErrorFilter extends AbstractFilter {

	private final String filterName = this.getClass().getName();

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		try {
			filterChain.doFilter(request, response);
		} catch (ServletException se) {
			Throwable cause = se.getRootCause();
			Throwable cause1 = null;
			Throwable cause2 = null;
			if (cause != null) {
				cause1 = cause.getCause();
			}
			if (cause1 != null) {
				cause2 = cause1.getCause();
			}
			if (cause != null && cause instanceof FDNotFoundException) {
				response.sendRedirect("/notfound.jsp");
			} else if (cause1 != null && cause1 instanceof FDNotFoundException) {
				response.sendRedirect("/notfound.jsp");
			} else if (cause2 != null && cause2 instanceof FDNotFoundException) {
				response.sendRedirect("/notfound.jsp");
			} else {
				throw se;
			}
		}
	}

	@Override
	public String getFilterName() {
		return this.filterName;
	}

}
