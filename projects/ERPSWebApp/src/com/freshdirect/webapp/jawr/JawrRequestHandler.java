package com.freshdirect.webapp.jawr;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class JawrRequestHandler extends net.jawr.web.servlet.JawrRequestHandler {

	private static final long serialVersionUID = 1792239658235327941L;
	
	public JawrRequestHandler(ServletContext context, ServletConfig config) throws ServletException {
		super(context, config);
	}

	/**
	 * Adds aggressive caching headers to the response in order to prevent
	 * browsers requesting the same file twice.
	 * 
	 * @param resp
	 *            the response
	 */
	@Override
	protected void setResponseHeaders(HttpServletResponse resp) {
		super.setResponseHeaders(resp);
		resp.setHeader(ETAG_HEADER, "\"" + ETAG_VALUE + "\"");
	}
	
}
