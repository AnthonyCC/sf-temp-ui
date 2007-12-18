package com.freshdirect.framework.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Filter to provide a workaround for a bug in the WebLogic 8.1 implementation
 * of the Servlet API. The bug is fixed by CR259956 in SP6.
 * 
 * Add to web.xml as:
 * 
 * <pre>
 *  &lt;filter&gt;
 * 	  &lt;filter-name&gt;WLContentTypeFilter&lt;/filter-name&gt;
 * 	  &lt;filter-class&gt;com.freshdirect.framework.webapp.WLContentTypeFilter&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *  &lt;filter-mapping&gt;
 * 	  &lt;filter-name&gt;WLContentTypeFilter&lt;/filter-name&gt;
 * 	  &lt;url-pattern&gt;/&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 * </pre>
 * 
 * @see http://edocs.bea.com/wls/docs81/notes/resolved_sp06.html#1905126
 */
public class WLContentTypeFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) servletResponse;
		ResponseWrapper myResponse = new ResponseWrapper(res);
		filterChain.doFilter(servletRequest, myResponse);
	}

	public static class ResponseWrapper extends HttpServletResponseWrapper {

		private boolean gotWriter;

		private boolean gotOutputStream;

		public ResponseWrapper(HttpServletResponse httpServletResponse) {
			super(httpServletResponse);
		}

		public void setContentType(String contentType) {
			if (isCommitted() || gotWriter || gotOutputStream) {
				// skipping response.setContentType
			} else {
				super.setContentType(contentType);
			}
		}

		public PrintWriter getWriter() throws IOException {
			gotWriter = true;
			return super.getWriter();
		}

		public ServletOutputStream getOutputStream() throws IOException {
			gotOutputStream = true;
			return super.getOutputStream();
		}
	}

}
